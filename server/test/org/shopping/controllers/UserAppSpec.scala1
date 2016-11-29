package org.shopping.controllers

import com.google.inject.AbstractModule
import org.junit.runner._
import org.shopping.config.RunModule
import org.shopping.repo.Oauth2Repo
import org.shopping.dto._
import org.shopping.services._
import org.specs2.mock.Mockito
import org.specs2.runner._
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json._
import play.api.test._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._
import scala.concurrent.duration._
import scala.language.postfixOps
import scalaoauth2.provider.GrantHandlerResult

@RunWith(classOf[JUnitRunner])
class UserAppSpec extends PlaySpecification with Mockito {

  def waitFor[T](f: Future[T], duration: FiniteDuration = 1000.milli)(implicit ec: ExecutionContext): T = Await.result(f, duration)

  class TestModule(m: UserService) extends AbstractModule {
    override def configure() = {
      bind(classOf[UserService]).toInstance(m)
      bind(classOf[Oauth2Repo]).toInstance(mock[Oauth2Repo])
      bind(classOf[ListService]).toInstance(mock[ListService])
      bind(classOf[ProductService]).toInstance(mock[ProductService])
      bind(classOf[MainService]).toInstance(mock[MainService])
    }
  }

  def app(m: UserService = mock[UserService]) = {
    new GuiceApplicationBuilder()
      .disable[RunModule]
      .configure(
        Map(
          "evolutions" -> "disabled",
          "slick.dbs.default.driver" -> "slick.driver.PostgresDriver$",
          "slick.dbs.default.db.driver" -> "org.postgresql.Driver",
          "slick.dbs.default.db.url" -> s"jdbc:postgresql://localhost:5432/mytest",
          "slick.dbs.default.db.user" -> "postgres",
          "slick.dbs.default.db.password" -> "root"
        ))
      .bindings(new TestModule(m))
      .build()
  }

  def newComp = mock[UserService]

  "Application" should {

    "have external static files attached to root" in {
      val a = app()
      running(a) {
        route(a, FakeRequest(GET, "/docs/index.html")) must beSome
      }
    }

    "have register route" in {
      val service = newComp
      service.getUserByToken(any) returns Future.successful(Right(UserDTO("login", "pass", "id", "nick")))
      service.login(any) returns Future.successful(Right(GrantHandlerResult(tokenType = "1", accessToken = "at", expiresIn = None, refreshToken = None, scope = None)))
      service.registerUser(any[RegisterRequestDTO]) answers (u => result(u.asInstanceOf[RegisterRequestDTO]))
      val a = app(service)
      running(a) {
        val page = route(a, FakeRequest(POST, "/api/register")
          .withJsonBody(Json.parse(
            """
            {
            "login":"test@test.com",
            "password":"123456"
            }
            """)))
        page must beSome
        val res = Await.result(page.get, Duration.Inf)
        val json = contentAsJson(page.get)
        (json \ "login").get === JsString("login")
      }
    }

  }
}
