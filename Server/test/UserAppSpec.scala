import org.junit.runner._
import org.planner.modules._
import org.planner.modules.core.UserModule
import org.planner.modules.dto._
import org.planner.util.Gen._
import org.specs2.mock.Mockito
import org.specs2.runner._
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json._
import play.api.test._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._
import scala.concurrent.duration._
import scala.language.postfixOps
import scalaoauth2.provider.GrantHandlerResult

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
@RunWith(classOf[JUnitRunner])
class UserAppSpec extends PlaySpecification with Mockito {

  def waitFor[T](f: Future[T], duration: FiniteDuration = 1000.milli)(implicit ec: ExecutionContext): T = Await.result(f, duration)

  def app(m: UserModule = mock[UserModule]) = {
    new GuiceApplicationBuilder()
      .configure(Map(
      "evolutionplugin" -> "disabled"
      ))
      .overrides(bind[UserModule].toInstance(m))
      .build()
  }

  def newComp = mock[UserModule]

  "Application" should {

    "have external static files attached to root" in {
      running(app()) {
        route(FakeRequest(GET, "/docs/index.html")) must beSome
      }
    }

    "render login page" in {
      running(app()) {
        val page = route(FakeRequest(GET, "/login")).get
        status(page) must equalTo(OK)
        contentType(page) must beSome.which(_ == "text/html")
        contentAsString(page) must contain("login")
      }
    }

    "have login route" in {
      val service = newComp
      running(app(service)) {
        service.login(any) returns Future.successful(Right(GrantHandlerResult(tokenType = "1", accessToken = "at", expiresIn = None, refreshToken = None, scope = None)))
        val page = route(FakeRequest(POST, "/login")
          .withFormUrlEncodedBody("email" -> "test@test.com", "password" -> "12345"))
        page must beSome
        waitFor(page.get)
        status(page.get) must equalTo(SEE_OTHER)
      }
    }

    "have register route" in {
      val service = newComp
      service.registerUser(any[RegisterDTO]) answers (u => result(u.asInstanceOf[RegisterDTO]))
      running(app(service)) {
        val page = route(FakeRequest(POST, "/api/register")
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
        println(json)
        (json \ "login").get === JsString("test@test.com")
      }
    }

    "have add group route" in {
      val service = newComp
      service.addGroup(any[GroupDTO]) answers {
        u =>
          val dto = u.asInstanceOf[GroupDTO].copy(id = guido)
          result(dto)
      }
      running(app(service)) {
        val page = route(FakeRequest(POST, "/api/group")
          .withJsonBody(Json.parse(
          """
            {
            "name":"group name",
            "projectId" : "pid"
            }
          """)))
        page must beSome
        val res = Await.result(page.get, Duration.Inf)
        val json = contentAsJson(page.get)
        json \ "name" === JsDefined(JsString("group name"))
        json \ "projectId" === JsDefined(JsString("pid"))
        (json \ "id").get.as[JsString].value must contain("-")
      }

    }

  }
}
