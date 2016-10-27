import org.junit.runner._
import org.shopping.dal._
import org.shopping.db._
import org.shopping.dto._
import org.shopping.modules._
import org.shopping.modules.core.impl._
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
    User(id = guid, login = guid, password = guid, created = now, updated = now,
      lastLogin = nowo, providerToken = guido, nick = guid), Some("1"), None, None)

  case class MockedContext(listService: DefaultListService, dalUser: UserDAL, dalList: ListDAL)

  def module(dalUser: UserDAL = mock[UserDAL], dalList: ListDAL = mock[ListDAL]) = {
    val ret = new DefaultListService(dalUser, dalList)
    ret.setAuth(authInfo)
    MockedContext(ret, dalUser, dalList)
  }

  def genString(size: Int): String = (for (i <- 1 to size) yield "a").mkString

  def genListDef(
    userId: String) = ListDef(id = guid, userId = userId, name = guid, description = guido, created = now, updated = now)

  "List module" should {

    "implement insertList and call dal" in {
      val m = module()
      val dto = ListDTO(id = guido, name = guid, description = guido, userId = Some("userId"), created = 1000)

      m.dalList.insertList(any[FullList]) answers (a => dal(a.asInstanceOf[FullList]))

      val s = Await.result(m.listService.insertList(dto), Duration.Inf)
      there was one(m.dalList).insertList(any[FullList])
      s must beRight
    }

    "implement updateList and call dal" in {
      val m = module()
      val dto = ListDTO(id = guido, name = guid, description = guido, userId = Some("userId"), created = 1000)

      m.dalList.updateList(any[ListDef]) answers (a => dal(a.asInstanceOf[ListDef]))
      m.dalList.getListDefById(any) returns dal(Some(genListDef(authInfo.user.id)))
      m.dalList.getListUsers(any) returns dal(Seq(dto.userId.get))
      val s = Await.result(m.listService.updateList(dto), Duration.Inf)
      there was one(m.dalList).updateList(any[ListDef])
      there was one(m.dalList).getListDefById(dto.id.get)
      s must beRight
    }

    "return right error when dal crashes" in {
      val m = module()
      val dto = ListDTO(id = guido, name = guid, description = guido, userId = Some("userId"), created = 1000)

      m.dalList.insertList(any[FullList]) returns Future.failed(new Exception("test"))
      val s = Await.result(m.listService.insertList(dto), Duration.Inf)
      there was one(m.dalList).insertList(any[FullList])
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
        created = now(),
        updated = now())
      val inst = ListInst(id = guid,
        listDefId = listDef.id,
        userId = listDef.userId,
        created = listDef.created,
        createdClient = 1000,
        updated = listDef.updated)
      val p1 = FullList(listDef, inst)
      m.dalList.getUserLists(anyString, any, any) returns dal((Seq(p1), 1))

      val s = Await.result(m.listService.getUserLists(m.listService.userId, 0, 100), Duration.Inf)

      there was one(m.dalList).getUserLists(m.listService.authData.user.id, 0, 100)
      s must beRight
      val ret = s.merge.asInstanceOf[ListsDTO]
      ret.items.size === 1
      ret.items.head.id.get === p1.inst.id
      ret.items.head.name === p1.listDef.name
      ret.items.head.description === p1.listDef.description
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
  }
}


