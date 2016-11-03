package org.shopping.services

import org.junit.runner._
import org.shopping.dal._
import org.shopping.db._
import org.shopping.dto._
import org.shopping.models.{ListDef, ListDefProduct, User}
import org.shopping.services.impl._
import org.shopping.util.Gen._
import org.shopping.util.Time._
import org.specs2.mock._
import org.specs2.mutable._
import org.specs2.runner._
import play.api.http.Status

import scala.concurrent._
import scala.concurrent.duration._
import scalaoauth2.provider.AuthInfo

@RunWith(classOf[JUnitRunner])
class ListServiceSpec extends Specification with Mockito {

  val authInfo = AuthInfo(user =
    User(id = guid, login = guid, password = guid, created = now(), updated = now(),
      lastLogin = nowo, providerToken = guido, nick = guid), Some("1"), None, None)

  case class MockedContext(listService: DefaultListService, dalUser: UserRepo, dalList: ListRepo)

  def module(dalUser: UserRepo = mock[UserRepo], dalList: ListRepo = mock[ListRepo]) = {
    val ret = new DefaultListService(dalUser, dalList)
    ret.setAuth(authInfo)
    MockedContext(ret, dalUser, dalList)
  }

  def genString(size: Int): String = (for (i <- 1 to size) yield "a").mkString

  def genListDef(
    userId: String) = ListDef(id = guid, userId = userId, name = guid, description = guido, createdClient = now(), created = now(), updated = now())

  "List module" should {

    "implement insertList and call dal" in {
      val m = module()
      val dto = ListDTO(id = guido, name = guid, description = guido, userId = Some("userId"), created = 1000)

      m.dalList.insertList(any[ListDef]) answers (a => dal(a.asInstanceOf[ListDef]))

      val s = Await.result(m.listService.insertList(dto), Duration.Inf)
      there was one(m.dalList).insertList(any[ListDef])
      s must beRight
    }

    "implement updateList and call dal" in {
      val m = module()
      val dto = ListDTO(id = guido, name = guid, description = guido, userId = Some("userId"), created = 1000)

      m.dalList.updateList(any[ListDef]) answers (a => dal(a.asInstanceOf[ListDef]))
      m.dalList.getListDefById(any) returns dal(Some(genListDef(authInfo.user.id)))
      m.dalList.getListUsers(any) returns dal(Seq(authInfo.user.id))
      val s = Await.result(m.listService.updateList(dto), Duration.Inf)
      there was one(m.dalList).updateList(any[ListDef])
      there was one(m.dalList).getListDefById(dto.id.get)
      s must beRight
    }

    "return right error when dal crashes" in {
      val m = module()
      val dto = ListDTO(id = guido, name = guid, description = guido, userId = Some("userId"), created = 1000)

      m.dalList.insertList(any[ListDef]) returns Future.failed(new Exception("test"))
      val s = Await.result(m.listService.insertList(dto), Duration.Inf)
      there was one(m.dalList).insertList(any[ListDef])
      s must beLeft
      val ErrorDTO(code, message) = s.merge.asInstanceOf[ResultError]
      code === Status.INTERNAL_SERVER_ERROR
      message === "test"
    }

    "implement get user lists" in {
      val m = module()
      m.dalList.getUserLists(anyString, any, any) returns dal((Seq(), 1))
      val s = m.listService.getUserLists(m.listService.userId, 0, 100)
      s must not be null
    }

    "get user lists and call dal" in {
      val m = module()
      val listDef = ListDef(
        id = guid,
        userId = "1",
        name = guid,
        description = guido,
        createdClient = now(),
        created = now(),
        updated = now())
      m.dalList.getUserLists(anyString, any, any) returns dal((Seq(listDef), 1))

      val s = Await.result(m.listService.getUserLists(m.listService.userId, 0, 100), Duration.Inf)

      there was one(m.dalList).getUserLists(m.listService.authData.user.id, 0, 100)
      s must beRight
      val ret = s.merge.asInstanceOf[ListsDTO]
      ret.items.size === 1
      ret.items.head.id.get === listDef.id
      ret.items.head.name === listDef.name
      ret.items.head.description === listDef.description
    }

    "get user list should handle dal errors" in {
      val m = module()
      m.dalList.getUserLists(anyString, any, any) returns dalErr("Test error")
      val s = Await.result(m.listService.getUserLists(m.listService.authData.user.id, 0, 100), Duration.Inf)
      there was one(m.dalList).getUserLists(m.listService.authData.user.id, 0, 100)
      s must beLeft
      val ErrorDTO(code, message) = s.merge.asInstanceOf[ResultError]
      code === Status.INTERNAL_SERVER_ERROR
      message === "Test error"
    }

    "get user lists and handle future failure" in {
      val m = module()

      m.dalList.getUserLists(anyString, any, any) returns Future.failed(new RuntimeException("test future"))

      val s = Await.result(m.listService.getUserLists(m.listService.authData.user.id, 0, 100), Duration.Inf)
      there was one(m.dalList).getUserLists(m.listService.authData.user.id, 0, 100)
      s must beLeft
      val ErrorDTO(code, message) = s.merge.asInstanceOf[ResultError]
      code === Status.INTERNAL_SERVER_ERROR
      message === "test future"
    }

    "add items to list will not clone if owned" in {
      val m = module()
      val listId = "listId"
      val items = ListItemsDTO(Seq(ListItemDTO("prodId", 10, None)), None)

      val listDef = ListDef(id = guid, userId = authInfo.user.id, name = guid,
        description = guido, createdClient = now(), created = now(), updated = now())
      val listProduct = ListDefProduct(listDef.id, "p1", None, 0, 0, now(), now())

      m.dalList.getListDefById(any) returns Future.successful(Some(listDef))
      m.dalList.insertList(any[ListDef]) answers (a => dal(a.asInstanceOf[ListDef]))
      m.dalList.addListDefProducts(any, any) returns Future.successful(Seq(listProduct))
      m.dalList.getListProductsByList(listId) returns Future.successful(Seq(listProduct))

      val s = Await.result(m.listService.addListItems(listId, items), Duration.Inf)

      there was no(m.dalList).insertList(any)
      there was one(m.dalList).addListDefProducts(any, any)
      s must beRight
      val ret = s.merge.asInstanceOf[ListItemsDTO]
      ret.items.size === 1
    }

    "add items to list will clone not owned" in {
      val m = module()
      val listId = "listId"
      val items = ListItemsDTO(Seq(ListItemDTO("prodId", 10, None)), None)

      val listDef = ListDef(id = guid, userId = "1", name = guid,
        description = guido, createdClient = now(), created = now(), updated = now())
      val listProduct = ListDefProduct(listDef.id, "p1", None, 0, 0, now(), now())
      m.dalList.getListDefById(any) returns Future.successful(Some(listDef))
      m.dalList.insertList(any[ListDef]) answers (a => dal(a.asInstanceOf[ListDef]))
      m.dalList.addListDefProducts(any, any) returns Future.successful(Seq(listProduct))
      m.dalList.getListProductsByList(listId) returns Future.successful(Seq(listProduct))

      val s = Await.result(m.listService.addListItems(listId, items), Duration.Inf)

      there was one(m.dalList).insertList(any)
      there was one(m.dalList).addListDefProducts(any, any)
      s must beRight
      val ret = s.merge.asInstanceOf[ListItemsDTO]
      ret.items.size === 1
    }

  }
}


