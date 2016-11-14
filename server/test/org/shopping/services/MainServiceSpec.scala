package org.shopping.services

import org.junit.runner._
import org.shopping.dto.RegisterRequestDTO
import org.shopping.models.{User, UserSession}
import org.shopping.repo._
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
class MainServiceSpec extends Specification with Mockito {

  val duration = Duration.Inf

  val defaultUser = User(id = guid, login = guid, password = guid, created = now(), updated = now(),
    lastLogin = nowo, providerToken = guido, nick = guid)

  case class MockContext(mainService: DefaultMainService, userService: UserService, listService: ListService, productService: ProductService)

  def service(userService: UserService = mock[UserService], listService: ListService = mock[ListService], productService: ProductService = mock[ProductService]) = {
    val ret = new DefaultMainService(userService, listService, productService)
    ret.setAuth(AuthInfo[User](user = defaultUser, Some("1"), None, None))
    MockContext(ret, userService, listService, productService)
  }

  def genString(size: Int): String = (for (i <- 1 to size) yield "a").mkString

  def newUser = User(id = guid, login = guid, providerToken = None, created = now(), updated = now(), lastLogin = None, password = guid, nick = guid)

  "Main service" should {

    "implement sync" in {
      success
    }

  }
}
