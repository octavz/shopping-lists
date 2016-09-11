import org.junit.runner._
import org.shopping.dal.Oauth2DAL
import org.shopping.db.User
import org.shopping.dto._
import org.shopping.modules._
import org.shopping.modules.core.ListModule
import org.shopping.util.Gen._
import org.shopping.util.Time._
import org.specs2.mock.Mockito
import org.specs2.runner._
import play.api.Application
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json._
import play.api.test._

import scala.concurrent._
import scala.concurrent.duration._
import scala.language.postfixOps
import scalaoauth2.provider.AccessToken

/**
  * Add your spec here.
  * You can mock out a whole application including requests, plugins etc.
  * For more information, consult the wiki.
  */
@RunWith(classOf[JUnitRunner])
class ProjectAppSpec extends PlaySpecification with Mockito {

  def waitFor[T](f: Future[T], duration: FiniteDuration = 1000.milli)(implicit ec: ExecutionContext): T = Await.result(f, duration)

  def anUser = User(id = guid, login = guid, providerToken = None, created = now, updated = now, lastLogin = None, password = guid, nick = guid)

  //  def app(m: ProjectController = mock[ProjectController], u: User = anUser) = FakeApplication(
  //    additionalConfiguration = Map(
  //      "evolutionplugin" -> "disabled",
  //      "db.default.driver" -> "org.h2.Driver",
  //      "db.default.url" -> "jdbc:h2:mem:test;MODE=PostgreSQL;DB_CLOSE_DELAY=-1"),
  //    withoutPlugins = Seq("com.typesafe.plugin.RedisPlugin"),
  //    withGlobal = Some(
  //      new GlobalSettings {
  //        override def getControllerInstance[A](clazz: Class[A]) = clazz match {
  //          case c if c.isAssignableFrom(classOf[ProjectController]) => m.asInstanceOf[A]
  //          case _ => super.getControllerInstance(clazz)
  //        }
  //      }
  //    ))


  case class MockContext(app: Application, projectModule: ListModule, dalAuth: Oauth2DAL, user: User)

  def app(mp: ListModule = mock[ListModule], dalAuth: Oauth2DAL = mock[Oauth2DAL], u: User = anUser): MockContext = {
    dalAuth.findAccessToken(anyString) returns Future.successful(Some(AccessToken("token", None, None, None, new java.util.Date())))
    //auth.isAccessTokenExpired(any[AccessToken]) returns false
    dalAuth.findAuthInfoByAccessToken(any[AccessToken]) returns Future.successful(Some(authInfo))

    val ret = new GuiceApplicationBuilder()
      .configure(Map(
        "evolutionplugin" -> "disabled"
      ))
      .overrides(bind[ListModule].toInstance(mp))
      .overrides(bind[Oauth2DAL].toInstance(dalAuth))
      .build()
    MockContext(ret, mp, dalAuth, u)
  }

  implicit val authInfo = new AuthData(anUser, Some("1"), None, None)

  "Project controller" should {

    "have create list route and authorize" in {
      val module = app()
      println("spec:" + module.projectModule)
      module.projectModule.insertList(any[ListDTO]) answers (p => result(p.asInstanceOf[ListDTO]))
      running(module.app) {
        val page = route(module.app, FakeRequest(POST, "/api/project")
          .withHeaders("Authorization" -> "OAuth token")
          .withJsonBody(Json.parse(
            """
            {
            "name":"project",
            "desc":"123456"
            }
            """)))
        page must beSome
        status(page.get) === OK
        Await.ready(page.get, Duration.Inf)
        there was one(module.projectModule).setAuth(authInfo)
        there was one(module.projectModule).insertList(any[ListDTO])
        val json = contentAsJson(page.get)
        json \ "name" === JsDefined(JsString("project"))
        json \ "desc" === JsDefined(JsString("123456"))
      }
    }

    "get all projects" in {
      val module = app()
      val p = ListDTO(id = guido, name = guid, desc = guido, userId = Some("userId"))
      module.projectModule.getUserLists("id", 0, 10) returns result(ProjectListDTO(items = List(p), total = 1))
      running(module.app) {
        val page = route(module.app, FakeRequest(GET, "/api/user/id/projects?offset=0&count=10").withHeaders("Authorization" -> "OAuth token"))
        page must beSome
        status(page.get) === OK
        Await.ready(page.get, Duration.Inf)
        there was one(module.projectModule).setAuth(authInfo)
        there was one(module.projectModule).getUserLists("id", 0, 10)
        val json = contentAsJson(page.get)
        val arr = (json \ "items").as[JsArray].value
        arr.size === 1
        arr.head \ "id" === JsDefined(JsString(p.id.get))
        arr.head \ "name" === JsDefined(JsString(p.name))
        arr.head \ "desc" === JsDefined(JsString(p.desc.get))
      }

    }

    "update project" in {
      val module = app()
      val p = ListDTO(id = guido, name = guid, desc = guido, userId = Some("userId"))
      module.projectModule.updateList(any) returns result(p)
      running(module.app) {
        val page = route(module.app, FakeRequest(PUT, "/api/project/id").withHeaders("Authorization" -> "OAuth token").withJsonBody(Json.parse(
          s"""
                {
                "name":"${p.name}",
                "desc":"${p.desc}"
                }
              """)))
        Await.ready(page.get, Duration.Inf)
        val json = contentAsJson(page.get)
        page must beSome
        status(page.get) === OK
        Await.ready(page.get, Duration.Inf)
        there was one(module.projectModule).setAuth(authInfo)
        there was one(module.projectModule).updateList(any)
        json \ "name" === JsDefined(JsString(p.name))
        json \ "desc" === JsDefined(JsString(p.desc.get))
      }
    }

  }
}
