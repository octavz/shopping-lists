package org.shopping.controllers

import org.junit.runner._
import org.shopping.config.RunModule
import org.shopping.dal.Oauth2Repo
import org.shopping.dto._
import org.shopping.models.User
import org.shopping.services._
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

@RunWith(classOf[JUnitRunner])
class ListAppSpec extends PlaySpecification with Mockito {

  def waitFor[T](f: Future[T], duration: FiniteDuration = 1000.milli)(implicit ec: ExecutionContext): T = Await.result(f, duration)

  def anUser = User(id = guid, login = guid, providerToken = None, created = now(), updated = now(), lastLogin = None, password = guid, nick = guid)

  case class MockContext(app: Application, listModule: ListService, dalAuth: Oauth2Repo, user: User)

  def app(mp: ListService = mock[ListService], dalAuth: Oauth2Repo = mock[Oauth2Repo], u: User = anUser): MockContext = {
    dalAuth.findAccessToken(anyString) returns Future.successful(Some(AccessToken("token", None, None, None, new java.util.Date())))
    //auth.isAccessTokenExpired(any[AccessToken]) returns false
    dalAuth.findAuthInfoByAccessToken(any[AccessToken]) returns Future.successful(Some(authInfo))

    val ret = new GuiceApplicationBuilder()
        .disable(classOf[RunModule])
      .configure(
        Map(
          "evolutions" -> "disabled",
          "slick.dbs.default.driver" -> "slick.driver.PostgresDriver$",
          "slick.dbs.default.db.driver" -> "org.postgresql.Driver",
          "slick.dbs.default.db.url" -> s"jdbc:postgresql://localhost:5432/mytest",
          "slick.dbs.default.db.user" -> "postgres",
          "slick.dbs.default.db.password" -> "root"
        ))
      .overrides(bind[ListService].toInstance(mp))
      .overrides(bind[UserService].toInstance(mock[UserService]))
      .overrides(bind[ProductService].toInstance(mock[ProductService]))
      .overrides(bind[Oauth2Repo].toInstance(dalAuth))
      .build()
    MockContext(ret, mp, dalAuth, u)
  }

  implicit val authInfo = new AuthData(anUser, Some("1"), None, None)

  "List controller" should {

    "have create list route and authorize" in {
      val module = app()
      module.listModule.insertList(any[ListDTO]) answers (p => result(p.asInstanceOf[ListDTO]))
      running(module.app) {
        val page = route(module.app, FakeRequest(POST, "/api/list")
          .withHeaders("Authorization" -> "OAuth token")
          .withJsonBody(Json.parse(
            """
            {
            "name":"list",
            "description":"123456",
            "created" : 10000
            }
            """)))
        page must beSome
        status(page.get) === OK
        Await.ready(page.get, Duration.Inf)
        there was one(module.listModule).setAuth(authInfo)
        there was one(module.listModule).insertList(any[ListDTO])
        val json = contentAsJson(page.get)
        json \ "name" === JsDefined(JsString("list"))
        json \ "description" === JsDefined(JsString("123456"))
      }
    }

    "get all lists" in {
      val module = app()
      val p = ListDTO(id = guido, name = guid, description = guido, userId = Some("userId"), created = 1000)
      module.listModule.getUserLists("id", 0, 10) returns result(ListsDTO(items = List(p), total = 1))
      running(module.app) {
        val page = route(module.app, FakeRequest(GET, "/api/user/id/lists?offset=0&count=10").withHeaders("Authorization" -> "OAuth token"))
        page must beSome
        status(page.get) === OK
        Await.ready(page.get, Duration.Inf)
        there was one(module.listModule).setAuth(authInfo)
        there was one(module.listModule).getUserLists("id", 0, 10)
        val json = contentAsJson(page.get)
        val arr = (json \ "items").as[JsArray].value
        arr.size === 1
        arr.head \ "id" === JsDefined(JsString(p.id.get))
        arr.head \ "name" === JsDefined(JsString(p.name))
        arr.head \ "description" === JsDefined(JsString(p.description.get))
      }

    }

    "update lists" in {
      val module = app()
      val p = ListDTO(id = guido, name = guid, description = guido, userId = Some("userId"), created = 1000)
      module.listModule.updateList(any) returns result(p)
      running(module.app) {
        val page = route(module.app, FakeRequest(PUT, "/api/list/id").withHeaders("Authorization" -> "OAuth token").withJsonBody(Json.parse(
          s"""
                {
                "name":"${p.name}",
                "description":"${p.description}",
                "created":${p.created}
                }
              """)))
        Await.ready(page.get, Duration.Inf)
        val json = contentAsJson(page.get)
        page must beSome
        status(page.get) === OK
        Await.ready(page.get, Duration.Inf)
        there was one(module.listModule).setAuth(authInfo)
        there was one(module.listModule).updateList(any)
        json \ "name" === JsDefined(JsString(p.name))
        json \ "description" === JsDefined(JsString(p.description.get))
      }
    }

  }
}
