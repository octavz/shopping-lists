package org.shopping.services

import org.junit.runner._
import org.shopping.dto._
import org.shopping.models.User
import org.shopping.services.impl._
import org.shopping.util.Gen._
import org.shopping.util.Time._
import org.specs2.mock._
import org.specs2.mutable._
import org.specs2.runner._

import scala.concurrent.Await
import scala.concurrent.duration._
import scalaoauth2.provider.AuthInfo

@RunWith(classOf[JUnitRunner])
class MainServiceSpec extends Specification with Mockito {

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

    def mock(m: MockContext) = {
      m.userService.getUserById(anyString)(any) returns result(userDto)
      m.userService.updateUser(any[UserDTO])(any) returns result(userDto)
      m.listService.getUserLists(anyString, anyInt, anyInt)(any) returns result(ListsDTO(Seq(newListDTO("listId1"), newListDTO("listId2"))))
      m.listService.insertLists(any[ListsDTO])(any) returns result(ListsDTO(Seq(newListDTO(guid))))
      m.listService.updateLists(any[ListsDTO])(any) returns result(ListsDTO(Seq(newListDTO(guid))))
      m.productService.insertProduct(any[ProductDTO])(any) returns result(newProductDTO(guid))
      m.productService.insertProductPrice(any[ProductPriceDTO])(any) returns result(newProductPriceDTO(guid))
      m
    }

    def assertSync(implicit fret: Result[SyncDTO]) = {
      val ret = Await.result(fret, Duration.Inf)
      ret should beRight
      val res = ret.right.get
      res.userData must beSome
      res.userData.get.id === userModel.id
      res.listsMeta must beSome
      res.listsMeta.get.items.size === 2
    }

    "when sync with empty values return existing state" in {
      val s = mock(service())
      val data = SyncDTO(userData = None, listsMeta = None, products = None, prices = None)
      implicit val res = s.mainService.sync(data)
      assertSync
      there was one(s.userService).getUserById(userDto.id)(authInfo)
      there was one(s.listService).getUserLists(userDto.id, 0, 1000)(authInfo)
      there was no(s.productService).insertProduct(any)(any)
      there was no(s.productService).insertProductPrice(any)(any)
    }

    "when sync with some userData call userService update and save" in {
      val s = mock(service())
      val dto = UserDTO(login = "login", password = "pass", id = "id", nick = "nick")
      val data = SyncDTO(userData = Some(dto), listsMeta = None,  products = None, prices = None)
      implicit val res = s.mainService.sync(data)
      assertSync
      there was one(s.userService).updateUser(dto)(authInfo)
      there was one(s.listService).getUserLists(userDto.id, 0, 1000)(authInfo)
      there was no(s.productService).insertProduct(any)(any)
      there was no(s.productService).insertProductPrice(any)(any)
    }

    "when sync with lists meta call listService" in {
      val s = mock(service())
      val l2 = newListDTO()
      val l3 = newListDTO().copy(id = None)

      val data = SyncDTO(userData = None, listsMeta = Some(ListsDTO(items = Seq(l2, l3))),  products = None, prices = None)
      implicit val res = s.mainService.sync(data)
      assertSync
      there was one(s.userService).getUserById(userDto.id)(authInfo)
      there was one(s.listService).getUserLists(userDto.id, 0, 1000)(authInfo)
      there was one(s.listService).insertLists(ListsDTO(Seq(l3)))(authInfo)
      there was one(s.listService).updateLists(ListsDTO(Seq(l2)))(authInfo)
      there was no(s.productService).insertProduct(any)(any)
      there was no(s.productService).insertProductPrice(any)(any)
    }

  }
}
