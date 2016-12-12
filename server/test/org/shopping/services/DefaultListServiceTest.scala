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
import play.api.libs.json.Json

import scala.concurrent._
import scala.concurrent.duration._
import scalaoauth2.provider.AuthInfo

@RunWith(classOf[JUnitRunner])
class DefaultListServiceTest extends PlaySpec with MockFactory {

  implicit val authInfo = AuthInfo(user =
    User(id = guid, login = guid, password = guid, created = now(), updated = now(),
      lastLogin = nowo, providerToken = guido, nick = guid), Some("1"), None, None)

  case class MockedContext(listService: DefaultListService, userRepo: UserRepo, listRepo: ListRepo, productRepo: ProductRepo)

  private def service(userRepo: UserRepo = mock[UserRepo], listRepo: ListRepo = mock[ListRepo],
              productRepo: ProductRepo = mock[ProductRepo]) = {
    val ret = new DefaultListService(userRepo, listRepo, productRepo)
    MockedContext(ret, userRepo, listRepo, productRepo)
  }

  def genString(size: Int): String = "a" * size

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

    "correctly convert a ListItem to Product" in {
      val dto = ListItemDTO(productId = Some("pid"), quantity = 2, description = Some("desc"), status = 2, clientTag = Some("test"), bought = 1)
      val model = dto.toModel("listId", "prodId")
      model.description === dto.description
      model.clientTag === dto.clientTag
      model.listDefId === "listId"
      model.productId === "pid"
      model.quantity === model.quantity
      model.bought === dto.bought
    }

    "correctly update existing list when items don't exist" in {
      val m = service()
      val listId = "861154c2-e311-438e-97cf-420ffe7913ce"
      val listDef = ListDef(id = listId, userId = authInfo.user.id, name = guid,
        description = guido, createdClient = now(), created = now(), updated = now())
      val p1 = Product(id = guid, userId = authInfo.user.id, name = "apa", tags = "apa")
      val p2 = Product(id = guid, userId = authInfo.user.id, name = "bere", tags = "bere")

      val listProduct = ListDefProduct(listDef.id, "p1", None, 0, 0, "", now(), now())
      (m.listRepo.getListUsers _).expects(*).once().returns(repo(Seq(authInfo.user.id)))
      (m.listRepo.getListDefById _).expects(listId).once().returns(repo(Some(ListWithItems(listDef, Seq(listProduct)))))
      (m.productRepo.insertProducts _).expects(where { model: Seq[Product] =>
        val apa = model.find(_.name.contains("apa"))
        val beer = model.find(_.name.contains("bere"))
        model.size == 2 && apa.isDefined && beer.isDefined
      }).once()
        .onCall { _: Seq[Product] => repo(Seq(p1, p2)) }

      (m.listRepo.replaceListItems _).expects(where { (lid: String, model: Seq[ListDefProduct]) =>
        val apa = model.find(_.description.contains("apa"))
        val beer = model.find(_.description.contains("bere"))
        lid == listId && model.size == 2 && apa.isDefined && beer.isDefined
      }).once().returns(repo(Seq(listProduct)))
      (m.listRepo.updateList _).expects(*).once().returns(repo(listDef))

      val list = Json.parse(
        s"""{"id": "$listId",
              "name": "l1", "userId": "41af8c9f-08d7-4144-9ab4-cdf605e5da5c",
              "created": 1480683428, "status": 0,
              "clientTag": "82f47c8a-3f0b-4c69-bf5c-6af3e24fc5f8",
              "items": [
                {"quantity": 1, "description": "apa", "status": 0, "clientTag": "d249094f-a3a5-4828-91ba-bc7011f579da", "bought": 0},
                {"quantity": 2, "description": "bere", "status": 0, "clientTag": "54238caf-3784-4589-a3a6-a02072a3d439", "bought": 0}
                ]}""").as[ListDTO](JsonDTOFormats.list)
      val s = Await.result(m.listService.updateList(list), Duration.Inf)

      s must be('right)
      val res = s.merge.asInstanceOf[ListDTO]
      res === list
    }

  }
}


