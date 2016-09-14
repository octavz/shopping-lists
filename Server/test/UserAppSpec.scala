import com.google.inject.AbstractModule
import org.junit.runner._
import org.shopping.config.RunModule
import org.shopping.dal.Oauth2DAL
import org.shopping.dto._
import org.shopping.modules._
import org.shopping.modules.core.{ListModule, UserModule}
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

  class TestModule(m: UserModule) extends AbstractModule {
    override def configure() = {
      bind(classOf[UserModule]).toInstance(m)
      bind(classOf[Oauth2DAL]).toInstance(mock[Oauth2DAL])
      bind(classOf[ListModule]).toInstance(mock[ListModule])
    }
  }

  def app(m: UserModule = mock[UserModule]) = {
    new GuiceApplicationBuilder()
      .disable[RunModule]
      .configure(Map(
        "evolutionplugin" -> "disabled"
      ))
      .bindings(new TestModule(m))
      .build()
  }

  def newComp = mock[UserModule]

  "Application" should {

    "have external static files attached to root" in {
      val a = app()
      running(a) {
        route(a, FakeRequest(GET, "/docs/index.html")) must beSome
      }
    }

    "render login page" in {
      val a = app()
      running(a) {
        val page = route(a, FakeRequest(GET, "/login")).get
        status(page) must equalTo(OK)
        contentType(page) must beSome.which(_ == "text/html")
        contentAsString(page) must contain("login")
      }
    }

    "have login route" in {
      val service = newComp
      val a = app(service)
      running(a) {
        service.login(any) returns Future.successful(Right(GrantHandlerResult(tokenType = "1", accessToken = "at", expiresIn = None, refreshToken = None, scope = None)))
        val page = route(a, FakeRequest(POST, "/login")
          .withFormUrlEncodedBody("email" -> "test@test.com", "password" -> "12345"))
        page must beSome
        waitFor(page.get)
        status(page.get) must equalTo(SEE_OTHER)
      }
    }

    "have register route" in {
      val service = newComp
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
        println(json)
        (json \ "login").get === JsString("test@test.com")
      }
    }

  }
}
