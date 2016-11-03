package org.shopping.services.impl

import java.util.Date

import com.google.inject.Inject
import org.shopping.dal._
import org.shopping.dto.{RegisterRequestDTO, UserDTO, UsersDTO}
import org.shopping.models.UserSession
import org.shopping.services.{UserService, _}
import org.shopping.util.Gen
import play.api.http.Status

import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent._
import scalaoauth2.provider._

class DefaultUserService @Inject()(dalUser: UserRepo, dalAuth: Oauth2Repo) extends UserService {

  override def createSession(accessToken: String): Result[String] = {
    dalAuth.findAuthInfoByAccessToken(scalaoauth2.provider.AccessToken(accessToken, None, None, None, new Date())) flatMap {
      authInfo =>
        if (authInfo.isEmpty) {
          resultError(Status.NOT_FOUND, "Session not found")
        } else {
          val model = UserSession(userId = authInfo.get.user.id, id = accessToken)
          val f = for {
            fDelete <- dalUser.deleteSessionByUser(model.userId)
            fInsert <- dalUser.insertSession(model)
          } yield resultSync(model.id)

          f recover { case e: Throwable =>
            e.printStackTrace()
            resultExSync(e,"createSession",Status.INTERNAL_SERVER_ERROR)
          }
        }
    }
  }

  override def login(request: AuthorizationRequest): Future[Either[OAuthError, GrantHandlerResult]] = {
    val ret = TokenEndpoint.handleRequest(request, dalAuth)
    ret flatMap {
      case r@Right(v) =>
        createSession(v.accessToken) map (_ => r)
      case _ => ret
    }
  }

  override def getUserById(id: String): Result[UserDTO] = {
    try {
      dalUser.getUserById(id) map {
        u =>
          resultSync(new UserDTO(u))
      } recover {
        case e: Throwable => resultExSync(e, "getUserById")
      }
    }
    catch {
      case e: Throwable => resultEx(e, "getUserById")
    }
  }

  def registerUser(u: RegisterRequestDTO): Result[RegisterRequestDTO] = {
    try {
      val model = u.toModel(Gen.guid)
      val f = dalUser.getUserByEmail(u.login) flatMap {
        case Some(_) => resultError(Status.INTERNAL_SERVER_ERROR, "Email already exists")
        case _ => dalUser.insertUser(model) map (a => resultSync(new RegisterRequestDTO(a)))
      }

      f recover { case e: Throwable => resultExSync(e, "registerUser") }
    } catch {
      case e: Throwable => resultEx(e, "registerUser")
    }
  }

  override def getUserByToken(token: String): Result[UserDTO] = {
    val f: Result[UserDTO] = for {
      at <- dalAuth.findAccessToken(token)
      data <- dalAuth.findAuthInfoByAccessToken(at.getOrElse(throw new Exception("Token not found")))
    } yield data match {
      case Some(info) => resultSync(new UserDTO(info.user))
      case _ => resultErrorSync(404, "User not found by access token")
    }

    f recover {
      case e: Throwable => resultExSync(e, "getUserByToken")
    }

  }

  override def searchUsers(email: Option[String], nick: Option[String]): Result[UsersDTO] = {
    val f = dalUser.searchUsers(email, nick).map(lst => resultSync(UsersDTO(lst.map(u => new UserDTO(u)))))
    f.recover {
      case e: Throwable => resultExSync(e, "searchUsers")
    }
  }

}

