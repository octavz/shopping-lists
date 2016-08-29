import org.junit.runner._

import org.planner.dal.Oauth2DAL
import org.planner.db.User
import org.planner.modules._
import org.planner.modules.core.ProjectModule
import org.planner.modules.dto._
import org.planner.util.Gen._
import org.planner.util.Time._
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

  def anUser = User(id = guid, login = guid, providerToken = None, created = now, userId = None, groupId = None, updated = now, lastLogin = None, password = guid, nick = guid)

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


  case class MockContext(app: Application, projectModule: ProjectModule, dalAuth: Oauth2DAL, user: User)

  def app(mp: ProjectModule = mock[ProjectModule], dalAuth: Oauth2DAL = mock[Oauth2DAL], u: User = anUser): MockContext = {
    dalAuth.findAccessToken(anyString) returns Future.successful(Some(AccessToken("token", None, None, None, new java.util.Date())))
    //auth.isAccessTokenExpired(any[AccessToken]) returns false
    dalAuth.findAuthInfoByAccessToken(any[AccessToken]) returns Future.successful(Some(authInfo))

    val ret = new GuiceApplicationBuilder()
      .configure(Map(
      "evolutionplugin" -> "disabled"
    ))
      .overrides(bind[ProjectModule].toInstance(mp))
      .overrides(bind[Oauth2DAL].toInstance(dalAuth))
      .build()
    MockContext(ret, mp, dalAuth, u)
  }

  implicit val authInfo = new AuthData(anUser, Some("1"), None, None)

  "Project controller" should {

    "have create project route and authorize" in {
      val module = app()
      println("spec:" + module.projectModule)
      module.projectModule.insertProject(any[ProjectDTO]) answers (p => result(p.asInstanceOf[ProjectDTO]))
      running(module.app) {
        val page = route(FakeRequest(POST, "/api/project")
          .withHeaders("Authorization" -> "OAuth token")
          .withJsonBody(Json.parse(
          """
            {
            "name":"project",
            "desc":"123456",
            "parent":"parent",
            "public" : true 
            }
          """)))
        page must beSome
        status(page.get) === OK
        Await.ready(page.get, Duration.Inf)
        there was one(module.projectModule).setAuth(authInfo)
        there was one(module.projectModule).insertProject(any[ProjectDTO])
        val json = contentAsJson(page.get)
        json \ "name" === JsDefined(JsString("project"))
        json \ "desc" === JsDefined(JsString("123456"))
        json \ "parent" === JsDefined(JsString("parent"))
      }
    }

    "get all projects" in {
      val module = app()
      val p = ProjectDTO(id = guido, name = guid, desc = guido, parent = guido, public = true, perm = Some(1), groupId = Some("groupId"), userId = Some("userId"))
      module.projectModule.getUserProjects("id", 0, 10) returns result(ProjectListDTO(items = List(p), total = 1))
      running(module.app) {
        val page = route(FakeRequest(GET, "/api/user/id/projects?offset=0&count=10").withHeaders("Authorization" -> "OAuth token"))
        page must beSome
        status(page.get) === OK
        Await.ready(page.get, Duration.Inf)
        there was one(module.projectModule).setAuth(authInfo)
        there was one(module.projectModule).getUserProjects("id", 0, 10)
        val json = contentAsJson(page.get)
        val arr = (json \ "items").as[JsArray].value
        arr.size === 1
        arr(0) \ "id" === JsDefined(JsString(p.id.get))
        arr(0) \ "name" === JsDefined(JsString(p.name))
        arr(0) \ "desc" === JsDefined(JsString(p.desc.get))
        arr(0) \ "parent" === JsDefined(JsString(p.parent.get))
        arr(0) \ "public" === JsDefined(JsBoolean(true))
        arr(0) \ "perm" === JsDefined(JsNumber(p.perm.get))
      }

    }

    "update project" in {
      val module = app()
      val p = ProjectDTO(id = guido, name = guid, desc = guido, parent = guido, public = true, perm = Some(1), groupId = Some("groupId"), userId = Some("userId"))
      module.projectModule.updateProject(any) returns result(p)
      running(module.app) {
        val page = route(FakeRequest(PUT, "/api/project/id").withHeaders("Authorization" -> "OAuth token").withJsonBody(Json.parse(
          s"""
                {
                "name":"${p.name}",
                "desc":"${p.desc}",
                "parent":"${p.parent}",
                "public" : true
                }
              """)))
        Await.ready(page.get, Duration.Inf)
        val json = contentAsJson(page.get)
        page must beSome
        status(page.get) === OK
        Await.ready(page.get, Duration.Inf)
        there was one(module.projectModule).setAuth(authInfo)
        there was one(module.projectModule).updateProject(any)
        json \ "name" === JsDefined(JsString(p.name))
        json \ "desc" === JsDefined(JsString(p.desc.get))
        json \ "parent" === JsDefined(JsString(p.parent.get))
      }
    }

  }
}
