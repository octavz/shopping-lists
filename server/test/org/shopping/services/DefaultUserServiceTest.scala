package org.shopping.services

import org.junit.runner._
import org.scalamock.scalatest.MockFactory
import org.scalatestplus.play.PlaySpec
import org.shopping.repo._
import org.shopping.db._
import org.shopping.dto.RegisterRequestDTO
import org.shopping.models.{User, UserSession}
import org.shopping.services.impl._
import org.shopping.util.ErrorMessages
import org.shopping.util.Gen._
import org.shopping.util.Time._

import scala.concurrent._
import scala.concurrent.duration._
import scalaoauth2.provider.AuthInfo

class DefaultUserServiceTest extends PlaySpec with MockFactory {

  val duration = Duration.Inf

  implicit val authData = AuthInfo[User](user =
    User(id = guid, login = guid, password = guid, created = now(), updated = now(),
      lastLogin = nowo, providerToken = guido, nick = guid), Some("1"), None, None)

  case class MockContext(userService: DefaultUserService, userRepo: UserRepo, authRepo: Oauth2Repo)

  def userService(userRepo: UserRepo = mock[UserRepo], authRepo: Oauth2Repo = mock[Oauth2Repo]): MockContext = {
    val ret = new DefaultUserService(userRepo, authRepo)
    MockContext(ret, userRepo, authRepo)
  }

  def genString(size: Int): String = (for (i <- 1 to size) yield "a").mkString

  def newUser = User(id = guid, login = guid, providerToken = None, created = now(), updated = now(), lastLogin = None, password = guid, nick = guid)

  "User service" should {

    "implement createSession" in {
      val us = UserSession(userId = guid, id = guid)
      val m = userService()

      (m.userRepo.insertSession _).expects(*).once().returns(repo(us))
      (m.userRepo.deleteSessionByUser _).expects(us.userId).never().returns(repo(1))
      (m.authRepo.findAuthInfoByAccessToken _).expects(*).once()
        .returns(repo(Some(AuthInfo(user = newUser.copy(id = us.userId), clientId = Some("1"), scope = None, redirectUri = None))))

      val s = Await.result(m.userService.createSession(us.id), duration)
      s must be('right)
      s.errCode === 0
      s.value.get === us.id
    }

    "implement getUserById and call repo" in {
      val id = guid
      val m = userService()

      (m.userRepo.getUserById _).expects(id).once().returns(repo(Some(newUser.copy(id = id))))
      val s = Await.result(m.userService.getUserById(id), duration)
      s must be('right)
    }

    "implement register and call repo" in {
      val u = RegisterRequestDTO(login = guid, password = guid)
      val m = userService()
      (m.userRepo.insertUser _).expects(*).once().onCall { a: User => repo(a)}
      (m.userRepo.getUserByEmail _).expects(*).once().returns(repo(None))
      val s = Await.result(m.userService.registerUser(u), duration)
      s must be('right)
    }

    "not call insert if email already exists" in {
      val u = RegisterRequestDTO(login = guid, password = guid)
      val m = userService()
      (m.userRepo.insertUser _).expects(*).never()
      (m.userRepo.getUserByEmail _).expects(*).once().returns(repo(Some(newUser)))

      val s = Await.result(m.userService.registerUser(u), duration)

      s must be('left)
      s.errCode === 500
      s.errMessage === ErrorMessages.EMAIL_ALREADY_EXISTS
    }

  }
}
