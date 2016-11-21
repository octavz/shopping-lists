package org.shopping.services

import org.junit.runner._
import org.shopping.repo._
import org.shopping.db._
import org.shopping.dto.RegisterRequestDTO
import org.shopping.models.{User, UserSession}
import org.shopping.services.impl._
import org.shopping.util.ErrorMessages
import org.shopping.util.Gen._
import org.shopping.util.Time._
import org.specs2.mock._
import org.specs2.mutable._
import org.specs2.runner._

import scala.concurrent._
import scala.concurrent.duration._
import scalaoauth2.provider.AuthInfo

@RunWith(classOf[JUnitRunner])
class UserServiceSpec extends Specification with Mockito {

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

      m.userRepo.insertSession(any[UserSession]) returns Future.successful(us)
      m.userRepo.deleteSessionByUser(us.userId) returns Future.successful(1)
      m.authRepo.findAuthInfoByAccessToken(any[scalaoauth2.provider.AccessToken]) returns Future.successful(Some(
        AuthInfo(user = newUser.copy(id = us.userId), clientId = Some("1"), scope = None, redirectUri = None)))

      val s = Await.result(m.userService.createSession(us.id), duration)
      there was one(m.userRepo).insertSession(any[UserSession])
      s must beRight
      s.errCode === 0
      s.value.get === us.id
    }

    "implement get user by id and call repo" in {
      val id = guid
      val m = userService()

      m.userRepo.getUserById(id) returns Future.successful(Some(newUser.copy(id = id)))
      val s = Await.result(m.userService.getUserById(id), duration)
      there was one(m.userRepo).getUserById(id)
    }

    "implement register and call repo" in {
      val u = RegisterRequestDTO(login = guid, password = guid)
      val m = userService()
      m.userRepo.insertUser(any[User]) answers (a => repo(a.asInstanceOf[User]))
      m.userRepo.getUserByEmail(any[String]) returns repo(None)
      val s = Await.result(m.userService.registerUser(u), duration)
      there was one(m.userRepo).getUserByEmail(anyString)
      there was one(m.userRepo).insertUser(any[User])
      s must beRight
    }

    "not call insert if email already exists" in {
      val u = RegisterRequestDTO(login = guid, password = guid)
      val m = userService()
      m.userRepo.insertUser(any[User]) answers (a => repo(a.asInstanceOf[User]))
      m.userRepo.getUserByEmail(any[String]) returns repo(Some(newUser))

      val s = Await.result(m.userService.registerUser(u), duration)

      there was one(m.userRepo).getUserByEmail(anyString)
      there was no(m.userRepo).insertUser(any[User])
      s must beLeft
      s.errCode === 500
      s.errMessage === ErrorMessages.EMAIL_ALREADY_EXISTS
    }

  }
}
