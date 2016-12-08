package org.shopping.controllers

import org.shopping.BaseSpec
import org.shopping.config.RunModule
import org.shopping.dto._
import org.shopping.models.User
import org.shopping.repo.Oauth2Repo
import org.shopping.services._
import org.shopping.util.Gen._
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json._
import play.api.test.Helpers._
import play.api.test._
import play.api.{Application, Play}

import scala.concurrent._
import scala.concurrent.duration._
import scala.language.postfixOps
import scalaoauth2.provider.AccessToken


class ListControllerTest extends BaseSpec{


  case class MockContext(app: Application, listService: ListService, authRepo: Oauth2Repo, user: User)

  def app(mp: ListService = mock[ListService], authRepo: Oauth2Repo = mock[Oauth2Repo], u: User = anUser): MockContext = {
    (authRepo.findAccessToken _).expects(*).returns(Future.successful(Some(AccessToken("token", None, None, None, new java.util.Date()))))
    //auth.isAccessTokenExpired(any[AccessToken]) returns false
    (authRepo.findAuthInfoByAccessToken _).expects(*).returns(Future.successful(Some(authInfo)))

    val ret = new GuiceApplicationBuilder()
      .disable(classOf[RunModule])
      .configure(testConf)
      .overrides(bind[ListService].toInstance(mp))
      .overrides(bind[UserService].toInstance(mock[UserService]))
      .overrides(bind[ProductService].toInstance(mock[ProductService]))
      .overrides(bind[MainService].toInstance(mock[MainService]))
      .overrides(bind[Oauth2Repo].toInstance(authRepo))
      .build()
    MockContext(ret, mp, authRepo, u)
  }

  "List controller" must {

    "have create list route and authorize" in {
      val service = app()
      (service.listService.insertList(_: ListDTO)(_: AuthData)).expects(*, *).once()
        .onCall { (l: ListDTO, _: AuthData) =>
        result(l)
      }
      running(service.app) {
        val page = route(service.app, FakeRequest("POST", "/api/list")
          .withHeaders("Authorization" -> "OAuth token")
          .withJsonBody(Json.parse(
            """{"name":"list", "description":"123456", "created" : 10000}""")))
        assert(page.isDefined)
        val json = contentAsJson(page.get)
        status(page.get) must be(OK)
        Await.ready(page.get, Duration.Inf)
        json \ "name" must be(JsDefined(JsString("list")))
        json \ "description" must be(JsDefined(JsString("123456")))
      }
    }

    "have a route for get all lists and call the list service" in {
      val service = app()
      val p = ListDTO(id = guido, name = guid, description = guido, userId = Some("userId"), created = 1000, items = None)
      (service.listService.getUserLists(_: String, _: Int, _: Int)(_: AuthData))
        .expects("id", 0, 10, *).once()
        .returns(result(ListsDTO(items = List(p), total = 1)))
      running(service.app) {
        val page = route(service.app, FakeRequest(GET, "/api/user/id/lists?offset=0&count=10").withHeaders("Authorization" -> "OAuth token"))
        assert(page.isDefined)
        status(page.get) === OK
        Await.ready(page.get, Duration.Inf)
        val json = contentAsJson(page.get)
        val arr = (json \ "items").as[JsArray].value
        arr.size === 1
        arr.head \ "id" === JsDefined(JsString(p.id.get))
        arr.head \ "name" === JsDefined(JsString(p.name))
        arr.head \ "description" === JsDefined(JsString(p.description.get))
      }
    }

    "have a route for update lists and call service" in {
      val service = app()
      val p = ListDTO(id = guido, name = guid, description = guido, userId = Some("userId"), created = 1000, items = None)
      (service.listService.updateList(_: ListDTO)(_: AuthData)).expects(*, *).once().returns(result(p))
      running(service.app) {
        val page = route(service.app, FakeRequest(PUT, "/api/list/id")
          .withHeaders("Authorization" -> "OAuth token")
          .withJsonBody(Json.parse(
            s"""{"name":"${p.name}", "description":"${p.description}", "created":${p.created}}""")))
        Await.ready(page.get, Duration.Inf)
        val json = contentAsJson(page.get)
        assert(page.isDefined)
        status(page.get) === OK
        Await.ready(page.get, Duration.Inf)
        json \ "name" === JsDefined(JsString(p.name))
        json \ "description" === JsDefined(JsString(p.description.get))
      }
    }

  }
}
