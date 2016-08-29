package org.planner.controllers

import javax.ws.rs.{PathParam, QueryParam}

import com.google.inject.Inject
import com.wordnik.swagger.annotations._
import org.planner._
import org.planner.modules.core.UserModule
import org.planner.modules.dto._
import play.api.libs.json.{JsObject, Json}
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._

@Api(value = "/api/user", description = "User operations")
class UserController @Inject()(userModule: UserModule) extends BaseController(userModule) {

  @ApiOperation(value = "Issue access token", notes = """{"token_type": "Bearer","access_token": "MDEwNTBkNDgtNDhkNC00YmNhLWJiMjktMzVhMTJkMjMwNDBk","expires_in": 3600,"refresh_token": "NzVmYjQ4ZDMtMjY3NS00NDA4LTkyZTgtNmNjOTNlNjRhNDZl"}""", response = classOf[String], httpMethod = "POST", nickname = "createAccessToken")
  def accessToken = Action.async {
    implicit request =>
      issueAccessToken(dalAuth)
  }

  def login = Action {
    Ok(views.html.users.login.render())
  }

  //  val loginForm = Form(
  //    mapping(
  //      "email" -> email,
  //      "password" -> nonEmptyText(5)
  //    )(LoginForm.apply)(LoginForm.unapply)
  //  )

  @ApiOperation(value = "Login user", notes = "Login user", response = classOf[String], httpMethod = "POST", nickname = "login")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "username", required = true, dataType = "String", paramType = "form", defaultValue = "aaa@aaa.com")
    , new ApiImplicitParam(name = "password", required = true, dataType = "String", paramType = "form", defaultValue = "123456")
    , new ApiImplicitParam(name = "client_id", required = true, dataType = "String", paramType = "form", defaultValue = "1")
    , new ApiImplicitParam(name = "grant_type", required = true, dataType = "String", paramType = "form", defaultValue = "password")
    , new ApiImplicitParam(name = "client_secret", required = true, dataType = "String", paramType = "form", defaultValue = "secret")
  ))
  def loginPost = Action.async {
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

  @ApiOperation(value = "Register user", notes = "Create new user", response = classOf[ProjectDTO], httpMethod = "POST", nickname = "registerUser")
  @ApiImplicitParams(Array(new ApiImplicitParam(value = "User to be registered", required = true, dataType = "org.planner.modules.dto.UserDTO", paramType = "body")))
  def register = Action.async {
    implicit request =>
      request.body.asJson.map {
        json => try {
          val dto = json.as[RegisterDTO]
          userModule.registerUser(dto).toResponse
        } catch {
          case e: Throwable =>
            Future.successful(BadRequest(s"Wrong json: ${
              e.getMessage
            }"))
        }
      }.getOrElse(Future.successful(BadRequest("Wrong json")))
  }

  @ApiOperation(value = "Add user group", notes = "Add user group", response = classOf[GroupDTO], httpMethod = "POST", nickname = "createGroup")
  @ApiImplicitParams(Array(new ApiImplicitParam(value = "New group", required = true, dataType = "GroupDTO", paramType = "body")))
  def addGroup() = Action.async {
    implicit request =>
      request.body.asJson.map {
        json => try {
          val dto = json.as[GroupDTO]
          userModule.addGroup(dto).toResponse
        } catch {
          case e: Throwable =>
            Future.successful(BadRequest(s"Wrong json: ${
              e.getMessage
            }"))
        }
      }.getOrElse(Future.successful(BadRequest("Wrong json")))
  }

  @ApiOperation(value = "Add user group", notes = "Get user by id", response = classOf[RegisterDTO], httpMethod = "GET", nickname = "getUserById")
  def getUserById(
                   @ApiParam(value = "user id", required = true, allowMultiple = false) @PathParam("userId") userId: String) = Action.async {
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

  @ApiOperation(value = "Add user group", notes = "Get user by session", response = classOf[RegisterDTO], httpMethod = "GET", nickname = "getUserBySession")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "Authorization", value = "authorization", defaultValue = "OAuth token", required = true, dataType = "string", paramType = "header")
  ))
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

  @ApiOperation(value = "Search user by email or/and nick", notes = "Search users", response = classOf[List[UserDTO]], httpMethod = "GET", nickname = "searchUsers")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "Authorization", value = "authorization", defaultValue = "OAuth token", required = true, dataType = "string", paramType = "header")
  ))
  def searchUsers(
                   @ApiParam(value = "user email", required = false, allowMultiple = false) @QueryParam("email") email: Option[String],
                   @ApiParam(value = "user nick", required = false, allowMultiple = false) @QueryParam("nick") nick: Option[String]) = Action.async {
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

  @ApiOperation(value = "Add user to group", notes = "Add user to group", response = classOf[BooleanDTO], httpMethod = "POST", nickname = "addUserToGroup")
  @ApiImplicitParams(Array(new ApiImplicitParam(value = "Array containing user ids", required = true, dataType = "List[String]", paramType = "body")))
  def addUsersToGroup(groupId: String) = Action.async {
    implicit request =>
      authorize {
        implicit authInfo =>
          request.body.asJson.map {
            json => try {
              val userIds = json.as[List[String]]
              userModule.addUsersToGroup(groupId, userIds).toResponse
            } catch {
              case e: Throwable =>
                Future.successful(BadRequest(s"Wrong json: ${
                  e.getMessage
                }"))
            }
          }.getOrElse(Future.successful(BadRequest("Wrong json")))
      }
  }
}
