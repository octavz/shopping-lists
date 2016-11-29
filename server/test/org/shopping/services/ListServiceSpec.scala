package org.shopping.services

import org.junit.runner._
import org.scalamock.scalatest.MockFactory
import org.scalatest.junit.JUnitRunner
import org.scalatestplus.play._
import org.shopping.dto._
import org.shopping.models._
import org.shopping.repo._
import org.shopping.services.impl._
import org.shopping.util.Gen._
import org.shopping.util.Time._
import play.api.http.Status

import scala.concurrent._
import scala.concurrent.duration._
import scalaoauth2.provider.AuthInfo

@RunWith(classOf[JUnitRunner])
class ListServiceSpec extends PlaySpec with MockFactory {

  implicit val authInfo = AuthInfo(user =
    User(id = guid, login = guid, password = guid, created = now(), updated = now(),
      lastLogin = nowo, providerToken = guido, nick = guid), Some("1"), None, None)

  case class MockedContext(listService: DefaultListService, userRepo: UserRepo, listRepo: ListRepo, productRepo: ProductRepo)

  def service(userRepo: UserRepo = mock[UserRepo], listRepo: ListRepo = mock[ListRepo],
    productRepo: ProductRepo = mock[ProductRepo]) = {
    val ret = new DefaultListService(userRepo, listRepo, productRepo)
    MockedContext(ret, userRepo, listRepo, productRepo)
  }

  def genString(size: Int): String = (for (i <- 1 to size) yield "a").mkString

  def genListDef(userId: String) =
    ListWithItems(
      ListDef(id = guid, userId = userId, name = guid, description = guido, createdClient = now(), created = now(), updated = now()),
      Nil)

  "List service" must {

    "implement insertList and call repo" in {
      val m = service()
      val dto = ListDTO(id = guido, name = guid, description = guido, userId = Some("userId"), created = 1000, items = None)
      (m.listRepo.insertList _).expects(*).once().onCall { a: ListDef => repo(a) }
      val s = Await.result(m.listService.insertList(dto), Duration.Inf)
      s must be('right)
    }

    "implement updateList and call repo" in {
      val m = service()
      val dto = ListDTO(id = guido, name = guid, description = guido, userId = Some("userId"), created = 1000, items = None)

      (m.listRepo.updateList _).expects(*).once().onCall { a: ListDef => repo(a) }
      (m.listRepo.getListDefById _).expects(*).once().returns(repo(Some(genListDef(authInfo.user.id))))
      (m.listRepo.getListUsers _).expects(*).once().returns(repo(Seq(authInfo.user.id)))
      val s = Await.result(m.listService.updateList(dto), Duration.Inf)
      s must be('right)
    }

    "return right error when repo crashes" in {
      val m = service()
      val dto = ListDTO(id = guido, name = guid, description = guido, userId = Some("userId"), created = 1000, items = None)

      (m.listRepo.insertList _).expects(*).once().returns(Future.failed(new Exception("test")))
      val s = Await.result(m.listService.insertList(dto), Duration.Inf)
      s must be('left)
      val ErrorDTO(code, message) = s.merge.asInstanceOf[ErrorDTO]
      code mustBe Status.INTERNAL_SERVER_ERROR
      message mustBe "insertList"
    }

    "implement get user lists" in {
      val m = service()
      (m.listRepo.getUserLists _).expects(authInfo.user.id, 0, 100).once().returns(repo((Seq(), 1)))
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
      (m.listRepo.getUserLists _).expects(authInfo.user.id, 0, 100).once().returns(repo((Seq(ListWithItems(listDef, Nil)), 1)))

      val s = Await.result(m.listService.getUserLists(m.listService.userId, 0, 100), Duration.Inf)

      s must be('right)
      val ret = s.merge.asInstanceOf[ListsDTO]
      ret.items.size mustBe 1
      ret.items.head.id.get mustBe listDef.id
      ret.items.head.name mustBe listDef.name
      ret.items.head.description mustBe listDef.description
    }

    "get user list should handle repo errors" in {
      val m = service()
      (m.listRepo.getUserLists _).expects(authInfo.user.id, 0, 100).once().returns(repoErr("Test error"))
      val s = Await.result(m.listService.getUserLists(authInfo.user.id, 0, 100), Duration.Inf)
      s must be('left)
      val ErrorDTO(code, message) = s.merge.asInstanceOf[ErrorDTO]
      code mustBe Status.INTERNAL_SERVER_ERROR
      message mustBe "getUserLists"
    }

    "get user lists and handle future failure in getUserLists" in {
      val m = service()
      (m.listRepo.getUserLists _).expects(authInfo.user.id, 0, 100).once().returns(Future.failed(new RuntimeException("test future")))
      val s = Await.result(m.listService.getUserLists(authInfo.user.id, 0, 100), Duration.Inf)
      s must be('left)
      val ErrorDTO(code, message) = s.merge.asInstanceOf[ErrorDTO]
      code mustBe Status.INTERNAL_SERVER_ERROR
      message mustBe "getUserLists"
    }

    "clone if owned by current user in updateList" in {
      val m = service()
      val listId = "listId"
      val listDef = ListDef(id = guid, userId = authInfo.user.id, name = guid,
        description = guido, createdClient = now(), created = now(), updated = now())
      val listProduct = ListDefProduct(listDef.id, "p1", None, 0, 0, "", now(), now())
      val list = ListDTO(id = Some(listId), name = guid, description = guido, userId = None, created = 1000,
        items = Some(Seq(new ListItemDTO(listProduct))))

      (m.listRepo.getListDefById _).expects(*).once().returns(repo(Some(ListWithItems(listDef, Seq(listProduct)))))
      (m.listRepo.replaceListItems _).expects(*, *).once().returns(repo(Seq(listProduct)))
      (m.listRepo.updateList _).expects(*).once().returns(repo(listDef))
      (m.listRepo.getListUsers _).expects(*).once().returns(repo(Seq(authInfo.user.id)))
      (m.productRepo.insertProducts _).expects(*).once().onCall { lst: Seq[Product] => repo(lst) }
      val s = Await.result(m.listService.updateList(list), Duration.Inf)

      s must be('right)
      val ret = s.merge.asInstanceOf[ListDTO]
      ret.items.size mustBe 1
    }

    "clone if not owned by current user in updateList" in {
      val m = service()
      val listId = "listId"
      val listDef = ListDef(id = guid, userId = "1", name = guid, description = guido, createdClient = now(), created = now(), updated = now())
      val list = ListDTO(id = Some(listId), name = guid, description = guido, userId = Some("userId"), created = 1000, items = None)

      (m.listRepo.getListDefById _).expects(*).once().returns(repo(Some(ListWithItems(listDef, Nil))))
      (m.listRepo.insertList _).expects(*).once().returns(repo(listDef))
      (m.listRepo.updateList _).expects(*).once().returns(repo(listDef))
      (m.listRepo.getListUsers _).expects(*).once().returns(repo(Seq(authInfo.user.id)))

      val s = Await.result(m.listService.updateList(list), Duration.Inf)

      s must be('right)
      val ret = s.merge.asInstanceOf[ListDTO]
      ret.items.size mustBe 1
    }

    "not call replace items if no items updateList" in {
      val m = service()
      val listId = "listId"
      val listDef = ListDef(id = guid, userId = authInfo.user.id, name = guid,
        description = guido, createdClient = now(), created = now(), updated = now())
      val listProduct = ListDefProduct(listDef.id, "p1", None, 0, 0, "", now(), now())
      val list = ListDTO(id = Some(listId), name = guid, description = guido,
        userId = None, created = 1000, items = None)

      (m.listRepo.getListDefById _).expects(*).once().returns(repo(Some(ListWithItems(listDef, Seq(listProduct)))))
      (m.listRepo.updateList _).expects(*).once().returns(repo(listDef))
      (m.listRepo.getListUsers _).expects(*).once().returns(repo(Seq(authInfo.user.id)))

      val s = Await.result(m.listService.updateList(list), Duration.Inf)

      s must be('right)
      val ret = s.merge.asInstanceOf[ListDTO]
      ret.items.size mustBe 1
    }

  }
}


