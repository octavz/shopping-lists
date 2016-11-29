package org.shopping.services

import org.scalamock.scalatest.MockFactory
import org.scalatestplus.play._
import org.shopping.dto._
import org.shopping.models.User
import org.shopping.services.impl._
import org.shopping.util.Gen._
import org.shopping.util.Time._

import scala.concurrent.Await
import scala.concurrent.duration._
import scalaoauth2.provider.AuthInfo

class MainServiceSpec extends PlaySpec with MockFactory {

  val duration = Duration.Inf

  private val userModel = User(id = "id_user", login = guid, password = guid, created = now(), updated = now(),
    lastLogin = nowo, providerToken = guido, nick = guid)

  private val userDto = new UserDTO(userModel)

  implicit val authInfo = AuthInfo[User](user = userModel, Some("1"), None, None)

  case class MockContext(mainService: DefaultMainService, userService: UserService, listService: ListService,
    productService: ProductService)

  private def service(userService: UserService = mock[UserService], listService: ListService = mock[ListService],
    productService: ProductService = mock[ProductService]) = {
    val ret = new DefaultMainService(userService, listService, productService)
    MockContext(ret, userService, listService, productService)
  }

  private def newUser = User(id = guid, login = guid, providerToken = None, created = now(), updated = now(),
    lastLogin = None, password = guid, nick = guid)

  private def newListDTO(id: String = guid) = ListDTO(id = Some(id),
    name = s"name $id",
    description = Some(s"desc $id"),
    userId = Some(userModel.id),
    created = now(),
    items = Some(Seq(newListItemDTO(), newListItemDTO())))

  private def newListItemDTO(id: String = guid) =
    ListItemDTO(productId = Some(id), quantity = 1, description = Some(s"desc $id"))

  private def newProductDTO(id: String = guid) =
    ProductDTO(id = Some(id), name = s"name $id", tags = "tags", description = Some(s"desc $id"))

  private def newProductPriceDTO(pid: String = guid) =
    ProductPriceDTO(productId = pid, supplierId = s"s-$pid", price = BigDecimal(100))

  "Main service" should {

    def assertSync(implicit fret: Result[SyncDTO]) = {
      val ret = Await.result(fret, Duration.Inf)
      ret must be('right)
      val res = ret.right.get
      assert(res.userData.isDefined)
      res.userData.get.id.get === userModel.id
      assert(res.listsMeta.isDefined)
      res.listsMeta.get.items.size === 2
    }

    "return existing state when sync with empty values " in {
      val m = service()
      val data = SyncDTO(userData = None, listsMeta = None, products = None, prices = None)
      (m.userService.getUserById(_: String)(_: AuthData)).expects(userDto.id, authInfo).once().returns(result(userDto))
      (m.listService.getUserLists(_: String, _: Int, _: Int)(_: AuthData)).expects(userDto.id, 0, 1000, authInfo).once()
        .returns(result(ListsDTO(Seq(newListDTO("listId1"), newListDTO("listId2")))))

      implicit val res = m.mainService.sync(data)
      assertSync
    }

    "call userService update and save when sync with some userData " in {
      val m = service()
      val dto = UpdateUserDTO(login = Some("login"), password = Some("pass"), id = Some("id"), nick = Some("nick"))
      val data = SyncDTO(userData = Some(dto), listsMeta = None, products = None, prices = None)
      (m.userService.getUserById(_: String)(_: AuthData)).expects(userDto.id, authInfo).never().returns(result(userDto))
      (m.listService.getUserLists(_: String, _: Int, _: Int)(_: AuthData)).expects(userDto.id, 0, 1000, authInfo).once()
        .returns(result(ListsDTO(Seq(newListDTO("listId1"), newListDTO("listId2")))))
      (m.userService.updateUser(_: UpdateUserDTO)(_: AuthData)).expects(*, *).once().returns(result(userDto))
      implicit val res = m.mainService.sync(data)
      assertSync
    }

    "call listService when sync with lists" in {
      val m = service()
      val l2 = newListDTO()
      val l3 = newListDTO().copy(id = None)

      (m.userService.getUserById(_: String)(_: AuthData)).expects(userDto.id, authInfo).once().returns(result(userDto))
      (m.listService.getUserLists(_: String, _: Int, _: Int)(_: AuthData)).expects(userDto.id, 0, 1000, authInfo).once()
        .returns(result(ListsDTO(Seq(newListDTO("listId1"), newListDTO("listId2")))))
      (m.listService.updateLists(_: ListsDTO)(_: AuthData)).expects(*, *).once().returns(result(ListsDTO(Seq(newListDTO(guid)))))
      (m.listService.insertLists(_: ListsDTO)(_: AuthData)).expects(*, *).once().returns(result(ListsDTO(Seq(newListDTO(guid)))))

      val data = SyncDTO(userData = None, listsMeta = Some(ListsDTO(items = Seq(l2, l3))), products = None, prices = None)
      implicit val res = m.mainService.sync(data)
      assertSync
    }

  }
}
