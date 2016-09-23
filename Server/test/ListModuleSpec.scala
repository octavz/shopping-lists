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
class ListModuleSpec extends Specification with Mockito {

  val authInfo = AuthInfo(user =
    User(id = guid, login = guid, password = guid, created = now, updated = now,
      lastLogin = nowo, providerToken = guido, nick = guid), Some("1"), None, None)

  case class MockedContext(listModule: DefaultListService, dalUser: UserDAL, dalList: ListDAL)

  def module(dalUser: UserDAL = mock[UserDAL], dalList: ListDAL = mock[ListDAL]) = {
    val ret = new DefaultListService(dalUser, dalList)
    ret.setAuth(authInfo)
    MockedContext(ret, dalUser, dalList)
  }

  /**
    * generates  strings to be used in test
    *
    * @param size the size of the string
    * @return the generated string
    */
  def genString(size: Int): String = (for (i <- 1 to size) yield "a").mkString

  def genList(userId: String) = new SList(id = guid, userId = userId, name = guid, description = guido, created = now, updated = now)

  "List module" should {

    "implement insertList and call dal" in {
      val m = module()
      val dto = ListDTO(id = guido, name = guid, desc = guido, userId = Some("userId"))

      m.dalList.insertList(any[SList]) answers (a => dal(a.asInstanceOf[SList]))

      val s = Await.result(m.listModule.insertList(dto), Duration.Inf)
      there was one(m.dalList).insertList(any[SList])
      s must beRight
    }

    "implement updateList and call dal" in {
      val m = module()
      val dto = ListDTO(id = guido, name = guid, desc = guido, userId = Some("userId"))

      m.dalList.updateLists(any[SList]) answers (a => dal(a.asInstanceOf[SList]))
      m.dalList.getListById(any) returns dal(Some(genList(authInfo.user.id)))
      m.dalList.getListUsers(any) returns dal(Seq(dto.userId.get))
      val s = Await.result(m.listModule.updateList(dto), Duration.Inf)
      there was one(m.dalList).updateLists(any[SList])
      there was one(m.dalList).getListById(dto.id.get)
      s must beRight
    }

    "return right error when dal crashes" in {
      val m = module()
      val dto = ListDTO(id = guido, name = guid, desc = guido, userId = Some("userId"))

      m.dalList.insertList(any[SList]) returns Future.failed(new Exception("test"))
      val s = Await.result(m.listModule.insertList(dto), Duration.Inf)
      there was one(m.dalList).insertList(any[SList])
      s must beLeft
      val ErrorDTO(code, message) = s.merge.asInstanceOf[ResultError]
      code === Status.INTERNAL_SERVER_ERROR
      message === "test"
    }

    "implement get user lists" in {
      val m = module()
      m.dalList.getUserLists(anyString, any, any) returns dal((Seq(), 1))
      val s = m.listModule.getUserLists(m.listModule.userId, 0, 100)
      s must not be null
    }

    "get user lists and call dal" in {
      val m = module()
      val p1 = new SList(id = guid, userId = "1", name = guid, description = guido, created = now, updated = now)
      m.dalList.getUserLists(anyString, any, any) returns dal((Seq(p1), 1))

      val s = Await.result(m.listModule.getUserLists(m.listModule.userId, 0, 100), Duration.Inf)

      there was one(m.dalList).getUserLists(m.listModule.authData.user.id, 0, 100)
      s must beRight
      val ret = s.merge.asInstanceOf[ListsDTO]
      ret.items.size === 1
      ret.items.head.id.get === p1.id
      ret.items.head.name === p1.name
      ret.items.head.desc === p1.description
    }

    "get user list should handle dal errors" in {
      val m = module()
      m.dalList.getUserLists(anyString, any, any) returns dalErr("Test error")
      val s = Await.result(m.listModule.getUserLists(m.listModule.authData.user.id, 0, 100), Duration.Inf)
      there was one(m.dalList).getUserLists(m.listModule.authData.user.id, 0, 100)
      s must beLeft
      val ErrorDTO(code, message) = s.merge.asInstanceOf[ResultError]
      code === Status.INTERNAL_SERVER_ERROR
      message === "Test error"
    }

    "get user lists and handle future failure" in {
      val m = module()
      m.dalList.getUserLists(anyString, any, any) returns Future.failed(new RuntimeException("test future"))
      val s = Await.result(m.listModule.getUserLists(m.listModule.authData.user.id, 0, 100), Duration.Inf)
      there was one(m.dalList).getUserLists(m.listModule.authData.user.id, 0, 100)
      s must beLeft
      val ErrorDTO(code, message) = s.merge.asInstanceOf[ResultError]
      code === Status.INTERNAL_SERVER_ERROR
      message === "test future"
    }
  }
}


