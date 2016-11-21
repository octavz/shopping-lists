package org.shopping.controllers


import com.google.inject.Inject
import org.shopping._
import org.shopping.dto._
import org.shopping.services.UserService
import play.api.libs.json.{JsObject, Json}
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._
import scalaoauth2.provider.AuthorizationRequest

class UserController @Inject()(userService: UserService) extends BaseController(userService) {

  def accessToken = Action.async {
    implicit request =>
      issueAccessToken(authRepo)
  }

  //  val loginForm = Form(
  //    mapping(
  //      "email" -> email,
  //      "password" -> nonEmptyText(5)
  //    )(LoginForm.apply)(LoginForm.unapply)
  //  )

  private def auth(login: String, password: String,
    grantType: Option[String] = None,
    clientId: Option[String] = None,
    clientSecret: Option[String] = None) = AuthorizationRequest(headers = Map.empty
    , params = Map(
      "grant_type" -> Seq(grantType.getOrElse("password"))
      , "client_id" -> Seq(clientId.getOrElse("1"))
      , "client_secret" -> Seq(clientSecret.getOrElse("secret"))
      , "username" -> Seq(login)
      , "password" -> Seq(password)))

  def login = Action.async {
    implicit request =>
      request.body.asJson.map { json =>
        val dto = json.as[LoginRequestDTO]

        userService.login(auth(login = dto.login, password = dto.password, grantType = dto.grantType, clientId = dto.clientId, clientSecret = dto.clientSecret)) flatMap {
          case Right(r) =>
            if (request.accepts("text/html")) {
              Future.successful(Redirect("/public.html").withCookies(Cookie("access_token", r.accessToken)))
            } else {
              userService.getUserByToken(r.accessToken) map {
                case Right(u) =>
                  Ok(Json.obj("accessToken" -> r.accessToken) ++ Json.toJson(u.copy(password = "")).as[JsObject])
                case Left(errDto) => BadRequest(err(errDto.message))
              }
            }
          case _ =>
            Future.successful(Unauthorized(err(401, "Unauthorized")))
        }
      }.getOrElse(Future.successful(BadRequest(err(400, "Wrong json"))))
  }

  def loginPostForm = Action.async {
    implicit request =>
      userService.login(request) flatMap {
        case Right(r) =>
          if (request.accepts("text/html")) {
            Future.successful(Redirect("/public.html").withCookies(Cookie("access_token", r.accessToken)))
          } else {
            userService.getUserByToken(r.accessToken) map {
              u =>
                Ok(Json.obj("accessToken" -> r.accessToken) ++ Json.toJson(u).as[JsObject])
            }
          }
        case _ =>
          Future.successful(Unauthorized(err(401, "Unauthorized")))
      }
  }

  def register = Action.async {
    implicit request =>
      request.body.asJson.map {
        json => try {
          val dto = json.as[RegisterRequestDTO]
          userService.registerUser(dto).toResponse
        } catch {
          case e: Throwable =>
            Future.successful(BadRequest(err(400, s"Wrong json: ${e.getMessage}")))
        }
      }.getOrElse(Future.successful(BadRequest(err(400, "Wrong json"))))
  }

  private def internalLogin(login: String, password: String) =
    userService.login(auth(login, password)) flatMap {
      case Right(r) =>
        userService.getUserByToken(r.accessToken) map {
          case Right(u) =>
            Ok(Json.obj("accessToken" -> r.accessToken) ++ Json.toJson(u.copy(password = "")).as[JsObject])
          case Left(err) => InternalServerError(Json.toJson(err))
        }
      case Left(oerr) =>
        Future.successful(Unauthorized(err(oerr.statusCode, oerr.description)))
    }

  def registerAndLogin = Action.async {
    implicit request =>
      request.body.asJson.map {
        json => try {
          val dto = json.as[RegisterRequestDTO]
          val registerResult = userService.registerUser(dto)
          registerResult flatMap {
            case Left(_) => registerResult.toResponse
            case _ => internalLogin(dto.login, dto.password)
          }
        } catch {
          case e: Throwable =>
            Future.successful(BadRequest(err(400, s"Wrong json: ${e.getMessage}")))
        }
      }.getOrElse(Future.successful(BadRequest(err(400, "Wrong json"))))
  }

  def getUserById(userId: String) = Action.async {
    implicit request =>
      try {
        authorize {
          implicit authInfo =>
            userService.getUserById(userId).toResponse
        }
      } catch {
        case e: Throwable => Future.successful(BadRequest(err(400, s"Wrong json: ${e.getMessage}")))
      }
  }

  def getUserBySession = Action.async {
    implicit request =>
      try {
        authorize {
          implicit authInfo =>
            userService.getUserById(authInfo.user.id).toResponse
        }
      } catch {
        case e: Throwable =>
          Future.successful(BadRequest(err(400, s"Wrong json: ${e.getMessage}")))
      }
  }

  def searchUsers(email: Option[String], nick: Option[String]) = Action.async {
    implicit request =>
      try {
        authorize {
          implicit authInfo =>
            userService.searchUsers(email, nick).toResponse
        }
      } catch {
        case e: Throwable =>
          Future.successful(BadRequest(err(400, s"Wrong json: ${e.getMessage}")))
      }
  }

}
