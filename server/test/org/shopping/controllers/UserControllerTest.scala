package org.shopping.controllers

import com.google.inject.AbstractModule
import org.shopping.BaseSpec
import org.shopping.config.RunModule
import org.shopping.dto._
import org.shopping.repo.Oauth2Repo
import org.shopping.services._
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json._
import play.api.test.Helpers._
import play.api.test._

import scala.concurrent._
import scala.concurrent.duration._
import scala.language.postfixOps
import scalaoauth2.provider.GrantHandlerResult

class UserControllerTest extends BaseSpec {

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
      .configure(testConf)
      .bindings(new TestModule(m))
      .build()
  }

  private def newComp = mock[UserService]

  "User controller" should {

    "have external static files attached to root" in {
      val a = app()
      running(a) {
        val r = route(a, FakeRequest("GET", "/docs/index.html"))
        assert(r.isDefined)
      }
    }

    "have register route" in {
      val service = newComp
      (service.getUserByToken _).expects(*).once().returns(Future.successful(Right(UserDTO("login", "pass", "id", "nick"))))
      (service.login _).expects(*).once(). returns (Future.successful(Right(GrantHandlerResult(tokenType = "1", accessToken = "at", expiresIn = None, refreshToken = None, scope = None))))
      (service.registerUser(_:RegisterRequestDTO))
        .expects(*).once
        .onCall{ u: RegisterRequestDTO => result(u)}
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
        assert(page.isDefined)
        val res = Await.result(page.get, Duration.Inf)
        val json = contentAsJson(page.get)
        (json \ "login").get === JsString("login")
      }
    }

  }
}
