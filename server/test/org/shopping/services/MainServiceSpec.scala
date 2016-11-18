package org.shopping.services

import org.junit.runner._
import org.shopping.dto._
import org.shopping.models.User
import org.shopping.services.impl._
import org.shopping.util.Gen
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

  private val userModel = User(id = guid, login = guid, password = guid, created = now(), updated = now(),
    lastLogin = nowo, providerToken = guido, nick = guid)

  private val userDto = new UserDTO(userModel)

  case class MockContext(mainService: DefaultMainService, userService: UserService, listService: ListService,
    productService: ProductService)

  private def service(userService: UserService = mock[UserService], listService: ListService = mock[ListService],
    productService: ProductService = mock[ProductService]) = {
    val ret = new DefaultMainService(userService, listService, productService)
    ret.setAuth(AuthInfo[User](user = userModel, Some("1"), None, None))
    MockContext(ret, userService, listService, productService)
  }

  private def newUser = User(id = guid,
    login = guid,
    providerToken = None,
    created = now(),
    updated = now(),
    lastLogin = None,
    password = guid,
    nick = guid)

  private def newListDTO(id: String = guid) = ListDTO(
    id = Some(id),
    name = s"name $id",
    description = Some(s"desc $id"),
    userId = Some(userModel.id),
    created = now()
  )

  private def newListItemDTO(id: String = guid) =
    ListItemDTO(productId = Some(id), quantity = 1, description = Some(s"desc $id"))

  private def newListItemsDTO(ids: String*): ListItemsDTO =
    ListItemsDTO(items = ids.map(newListItemDTO), meta = Some(ListMetadata(guid, Nil)))

  private def newProductDTO(id: String = guid) =
    ProductDTO(id = Some(id), name = s"name $id", tags = "tags", description = Some(s"desc $id"))

  private def newProductPriceDTO(pid: String = guid) =
    ProductPriceDTO(productId = pid, supplierId = s"s-$pid", price = BigDecimal(100))

  "Main service" should {

    def mock(m: MockContext) = {
      m.userService.getUserById(anyString) returns result(userDto)
      m.userService.updateUser(any[UserDTO]) returns result(userDto)
      m.listService.getUserLists(anyString, anyInt, anyInt) returns result(ListsDTO(Seq(newListDTO(guid))))
      m.listService.updateLists(any[ListsDTO]) returns result(ListsDTO(Seq(newListDTO(guid))))
      m.listService.addListItems(any[ListItemsDTO]) returns result(newListItemsDTO(guid, guid))
      m.listService.getListItems(anyString) returns result(newListItemsDTO(guid, guid))
      m.productService.insertProduct(any[ProductDTO]) returns result(newProductDTO(guid))
      m.productService.insertProductPrice(any[ProductPriceDTO]) returns result(newProductPriceDTO(guid))
      m
    }

    "implement sync" in {
      val s = mock(service())
      val data = SyncDTO(userData = None, listsMeta = None, lists = None, products = None, prices = None)
      val ret = Await.result(s.mainService.sync(data), Duration.Inf)
      ret should beRight
    }

  }
}
