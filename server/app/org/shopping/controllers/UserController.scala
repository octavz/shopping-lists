package org.shopping.controllers


import com.google.inject.Inject
import org.shopping._
import org.shopping.modules.core.UserService
import org.shopping.dto._
import play.api.libs.json.{JsObject, Json}
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._
import scalaoauth2.provider.AuthorizationRequest

class UserController @Inject()(userModule: UserService) extends BaseController(userModule) {

  def accessToken = Action.async {
    implicit request =>
      issueAccessToken(dalAuth)
  }

  def loginGet = Action {
    Ok(views.html.users.login.render())
  }

  //  val loginForm = Form(
  //    mapping(
  //      "email" -> email,
  //      "password" -> nonEmptyText(5)
  //    )(LoginForm.apply)(LoginForm.unapply)
  //  )

  def login = Action.async {
    implicit request =>
      request.body.asJson.map { json =>
        val dto = json.as[LoginRequestDTO]
        val auth = AuthorizationRequest(headers = Map.empty
          , params = Map(
            "grant_type" -> Seq(dto.grantType.getOrElse("password"))
            , "client_id" -> Seq(dto.clientId.getOrElse("1"))
            , "client_secret" -> Seq(dto.clientSecret.getOrElse("secret"))
            , "username" -> Seq(dto.userName)
            , "password" -> Seq(dto.password)))

        userModule.login(auth) flatMap {
          case Right(r) =>
            if (request.accepts("text/html")) {
              Future.successful(Redirect("/public.html").withCookies(Cookie("access_token", r.accessToken)))
            } else {
              userModule.getUserByToken(r.accessToken) map {
                u =>
                  Ok(Json.obj("accessToken" -> r.accessToken) ++ Json.toJson(u).as[JsObject])
              }
            }
          case _ =>
            if (request.accepts("text/html")) {
              Future.successful(Ok(views.html.users.login.render()))
            } else {
              Future.successful(Unauthorized)
            }
        }
      }.getOrElse(Future.successful(BadRequest("Wrong json")))
  }

  def loginPostForm = Action.async {
    implicit request =>
      userModule.login(request) flatMap {
        case Right(r) =>
          if (request.accepts("text/html")) {
            Future.successful(Redirect("/public.html").withCookies(Cookie("access_token", r.accessToken)))
          } else {
            userModule.getUserByToken(r.accessToken) map {
              u =>
                Ok(Json.obj("accessToken" -> r.accessToken) ++ Json.toJson(u).as[JsObject])
            }
          }
        case _ =>
          if (request.accepts("text/html")) {
            Future.successful(Ok(views.html.users.login.render()))
          } else {
            Future.successful(Unauthorized)
          }
      }
  }

  def register = Action.async {
    implicit request =>
      request.body.asJson.map {
        json => try {
          val dto = json.as[RegisterRequestDTO]
          userModule.registerUser(dto).toResponse
        } catch {
          case e: Throwable =>
            Future.successful(BadRequest(s"Wrong json: ${
              e.getMessage
            }"))
        }
      }.getOrElse(Future.successful(BadRequest("@aaa.comWrong json")))
  }

  def getUserById(userId: String) = Action.async {
    implicit request =>
      try {
        userModule.getUserById(userId).toResponse
      } catch {
        case e: Throwable =>
          Future.successful(BadRequest(s"Wrong json: ${
            e.getMessage
          }"))
      }
  }

  def getUserBySession = Action.async {
    implicit request =>
      try {
        authorize {
          implicit authInfo =>
            userModule.getUserById(authInfo.user.id).toResponse
        }
      } catch {
        case e: Throwable =>
          Future.successful(BadRequest(s"Wrong json: ${
            e.getMessage
          }"))
      }
  }

  def searchUsers(email: Option[String], nick: Option[String]) = Action.async {
    implicit request =>
      try {
        authorize {
          implicit authInfo =>
            userModule.searchUsers(email, nick).toResponse
        }
      } catch {
        case e: Throwable =>
          Future.successful(BadRequest(s"Wrong json: ${
            e.getMessage
          }"))
      }
  }

}