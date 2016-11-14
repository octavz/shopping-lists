package org.shopping.services

import org.junit.runner._
import org.shopping.repo._
import org.shopping.dto._
import org.shopping.models.{ListDef, ListDefProduct, Product, User}
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

  case class MockedContext(listService: DefaultListService, userRepo: UserRepo, listRepo: ListRepo, productRepo: ProductRepo)

  def service(userRepo: UserRepo = mock[UserRepo], listRepo: ListRepo = mock[ListRepo],
    productRepo: ProductRepo = mock[ProductRepo]) = {
    val ret = new DefaultListService(userRepo, listRepo, productRepo)
    productRepo.insertProducts(any[Seq[Product]]) answers (a => repo(a.asInstanceOf[Seq[Product]]))
    ret.setAuth(authInfo)
    MockedContext(ret, userRepo, listRepo, productRepo)
  }

  def genString(size: Int): String = (for (i <- 1 to size) yield "a").mkString

  def genListDef(
    userId: String) = ListDef(id = guid, userId = userId, name = guid, description = guido, createdClient = now(), created = now(), updated = now())

  "List service" should {

    "implement insertList and call repo" in {
      val m = service()
      val dto = ListDTO(id = guido, name = guid, description = guido, userId = Some("userId"), created = 1000)

      m.listRepo.insertList(any[ListDef]) answers (a => repo(a.asInstanceOf[ListDef]))

      val s = Await.result(m.listService.insertList(dto), Duration.Inf)
      there was one(m.listRepo).insertList(any[ListDef])
      s must beRight
    }

    "implement updateList and call repo" in {
      val m = service()
      val dto = ListDTO(id = guido, name = guid, description = guido, userId = Some("userId"), created = 1000)

      m.listRepo.updateList(any[ListDef]) answers (a => repo(a.asInstanceOf[ListDef]))
      m.listRepo.getListDefById(any) returns repo(Some(genListDef(authInfo.user.id)))
      m.listRepo.getListUsers(any) returns repo(Seq(authInfo.user.id))
      val s = Await.result(m.listService.updateList(dto), Duration.Inf)
      there was one(m.listRepo).updateList(any[ListDef])
      there was one(m.listRepo).getListDefById(dto.id.get)
      s must beRight
    }

    "return right error when repo crashes" in {
      val m = service()
      val dto = ListDTO(id = guido, name = guid, description = guido, userId = Some("userId"), created = 1000)

      m.listRepo.insertList(any[ListDef]) returns Future.failed(new Exception("test"))
      val s = Await.result(m.listService.insertList(dto), Duration.Inf)
      there was one(m.listRepo).insertList(any[ListDef])
      s must beLeft
      val ErrorDTO(code, message) = s.merge.asInstanceOf[ErrorDTO]
      code === Status.INTERNAL_SERVER_ERROR
      message === "insertList"
    }

    "implement get user lists" in {
      val m = service()
      m.listRepo.getUserLists(anyString, any, any) returns repo((Seq(), 1))
      val s = m.listService.getUserLists(m.listService.userId, 0, 100)
      s must not be null
    }

    "get user lists and call repo" in {
      val m = service()
      val listDef = ListDef(
        id = guid,
        userId = "1",
        name = guid,
        description = guido,
        createdClient = now(),
        created = now(),
        updated = now())
      m.listRepo.getUserLists(anyString, any, any) returns repo((Seq(listDef), 1))

      val s = Await.result(m.listService.getUserLists(m.listService.userId, 0, 100), Duration.Inf)

      there was one(m.listRepo).getUserLists(m.listService.authData.user.id, 0, 100)
      s must beRight
      val ret = s.merge.asInstanceOf[ListsDTO]
      ret.items.size === 1
      ret.items.head.id.get === listDef.id
      ret.items.head.name === listDef.name
      ret.items.head.description === listDef.description
    }

    "get user list should handle repo errors" in {
      val m = service()
      m.listRepo.getUserLists(anyString, any, any) returns repoErr("Test error")
      val s = Await.result(m.listService.getUserLists(m.listService.authData.user.id, 0, 100), Duration.Inf)
      there was one(m.listRepo).getUserLists(m.listService.authData.user.id, 0, 100)
      s must beLeft
      val ErrorDTO(code, message) = s.merge.asInstanceOf[ErrorDTO]
      code === Status.INTERNAL_SERVER_ERROR
      message === "getUserLists"
    }

    "get user lists and handle future failure" in {
      val m = service()

      m.listRepo.getUserLists(anyString, any, any) returns Future.failed(new RuntimeException("test future"))

      val s = Await.result(m.listService.getUserLists(m.listService.authData.user.id, 0, 100), Duration.Inf)
      there was one(m.listRepo).getUserLists(m.listService.authData.user.id, 0, 100)
      s must beLeft
      val ErrorDTO(code, message) = s.merge.asInstanceOf[ErrorDTO]
      code === Status.INTERNAL_SERVER_ERROR
      message === "getUserLists"
    }

    "add items to list will not clone if owned" in {
      val m = service()
      val listId = "listId"
      val items = ListItemsDTO(Seq(ListItemDTO(Some("prodId"), 10, None)), Some(ListMetadata(listId, Nil)))

      val listDef = ListDef(id = guid, userId = authInfo.user.id, name = guid,
        description = guido, createdClient = now(), created = now(), updated = now())
      val listProduct = ListDefProduct(listDef.id, "p1", None, 0, 0, now(), now())

      m.listRepo.getListDefById(any) returns repo(Some(listDef))
      m.listRepo.insertList(any[ListDef]) returns repo(listDef)
      m.listRepo.replaceListItems(any, any) returns repo(Seq(listProduct))
      m.listRepo.getListProductsByList(listDef.id) returns repo(Seq(listProduct))

      m.listRepo.updateBatchedBought(any, any) returns repo(1)
      val s = Await.result(m.listService.addListItems(items), Duration.Inf)

      there was no(m.listRepo).insertList(any)
      there was one(m.listRepo).replaceListItems(any, any)
      s must beRight
      val ret = s.merge.asInstanceOf[ListItemsDTO]
      ret.items.size === 1
    }

    "add items to list will clone not owned" in {
      val m = service()
      val listId = "listId"
      val items = ListItemsDTO(Seq(ListItemDTO(Some("prodId"), 10, None)), Some(ListMetadata(listId, Nil)))

      val listDef = ListDef(id = guid, userId = "1", name = guid,
        description = guido, createdClient = now(), created = now(), updated = now())
      val listProduct = ListDefProduct(listDef.id, "p1", None, 0, 0, now(), now())
      m.listRepo.getListDefById(any) returns repo(Some(listDef))
      m.listRepo.insertList(any[ListDef]) returns repo(listDef)
      m.listRepo.replaceListItems(any, any) returns repo(Seq(listProduct))
      m.listRepo.getListProductsByList(listDef.id) returns repo(Seq(listProduct))

      m.listRepo.updateBatchedBought(any, any) returns repo(1)
      val s = Await.result(m.listService.addListItems(items), Duration.Inf)

      there was one(m.listRepo).insertList(any)
      there was one(m.listRepo).replaceListItems(any, any)
      s must beRight
      val ret = s.merge.asInstanceOf[ListItemsDTO]
      ret.items.size === 1
    }

  }
}


