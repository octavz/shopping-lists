package org.shopping.services.impl

import java.util.Date

import com.google.inject.Inject
import org.shopping.repo._
import org.shopping.dto.{RegisterRequestDTO, UserDTO, UsersDTO}
import org.shopping.models.UserSession
import org.shopping.services.{UserService, _}
import org.shopping.util.{ErrorMessages, Gen}
import play.api.http.Status

import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent._
import scalaoauth2.provider._

class DefaultUserService @Inject()(userRepo: UserRepo, authRepo: Oauth2Repo) extends UserService {

  override def createSession(accessToken: String): Result[String] =
    authRepo.findAuthInfoByAccessToken(
      scalaoauth2.provider.AccessToken(token = accessToken,
        refreshToken = None,
        scope = None,
        expiresIn = None,
        createdAt = new Date())) flatMap {
      authInfo =>
        if (authInfo.isEmpty) {
          error(Status.NOT_FOUND -> ErrorMessages.NOT_FOUND)
        } else {
          val model = UserSession(userId = authInfo.get.user.id, id = accessToken)
          val f = for {
          //            fDelete <- userRepo.deleteSessionByUser(model.userId)
            fInsert <- userRepo.insertSession(model)
          } yield resultSync(model.id)

          f recover { case e: Throwable =>
            exSync(e)
          }
        }
    }

  override def login(request: AuthorizationRequest): Future[Either[OAuthError, GrantHandlerResult]] = {
    val ret = TokenEndpoint.handleRequest(request, authRepo)
    ret flatMap {
      case r@Right(v) =>
        createSession(v.accessToken) map (_ => r)
      case _ => ret
    }
  }

  override def getUserById(id: String): Result[UserDTO] = {
    try {
      userRepo.getUserById(id) map {
        case Some(u) => resultSync(new UserDTO(u))
        case _ => errorSync(404, ErrorMessages.NOT_FOUND)
      } recover {
        case e: Throwable => exSync(e)
      }
    }
    catch {
      case e: Throwable => ex(e)
    }
  }

  def registerUser(u: RegisterRequestDTO): Result[RegisterRequestDTO] = {
    try {
      val model = u.toModel(Gen.guid)
      val f = userRepo.getUserByEmail(u.login) flatMap {
        case Some(_) => error(Status.INTERNAL_SERVER_ERROR -> ErrorMessages.EMAIL_ALREADY_EXISTS)
        case _ => userRepo.insertUser(model) map (a => resultSync(new RegisterRequestDTO(a)))
      }

      f recover { case e: Throwable => exSync(e) }
    } catch {
      case e: Throwable => ex(e)
    }
  }

  override def getUserByToken(token: String): Result[UserDTO] = {
    val f: Result[UserDTO] = for {
      optAccessToken <- authRepo.findAccessToken(token)
      data <- optAccessToken match {
        case Some(at) => authRepo.findAuthInfoByAccessToken(at)
        case _ => Future.successful(None)
      }
    } yield data match {
      case Some(info) => resultSync(new UserDTO(info.user))
      case _ => errorSync(404, ErrorMessages.NOT_FOUND)
    }

    f recover {
      case e: Throwable => exSync(e)
    }
  }

  override def searchUsers(email: Option[String], nick: Option[String]): Result[UsersDTO] = {
    userRepo
      .searchUsers(email, nick)
      .map(lst => resultSync(UsersDTO(lst.map(new UserDTO(_)))))
      .recover {
        case e: Throwable => exSync(e)
      }
  }

  override def updateUser(dto: UserDTO): Result[UserDTO] =
    userRepo.getUserById(userId) flatMap {
      case Some(u) =>
        userRepo
          .updateUser(u.copy(nick = dto.nick, password = dto.password))
          .map(r => resultSync(new UserDTO(r)))
      case None => error(404 -> ErrorMessages.NOT_FOUND)
    }
}

