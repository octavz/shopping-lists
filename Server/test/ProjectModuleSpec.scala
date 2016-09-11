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

/** .
  * main test class for DefaultAssetServiceComponent
  * it mocks AssetRepoComponent
  */
@RunWith(classOf[JUnitRunner])
class ProjectModuleSpec extends Specification with Mockito {

  val authInfo = AuthInfo(user =
    User(id = guid, login = guid, password = guid, created = now, updated = now,
      lastLogin = nowo, providerToken = guido, nick = guid), Some("1"), None, None)

  case class MockedContext(projectModule: DefaultProjectModule, dalUser: UserDAL, dalProject: ListDAL)

  def module(dalUser: UserDAL = mock[UserDAL], dalProject: ListDAL = mock[ListDAL]) = {
    val ret = new DefaultProjectModule(dalUser, dalProject)
    ret.setAuth(authInfo)
    MockedContext(ret, dalUser, dalProject)
  }

  /**
    * generates  strings to be used in test
    *
    * @param size the size of the string
    * @return the generated string
    */
  def genString(size: Int): String = (for (i <- 1 to size) yield "a").mkString

  def genProject(userId: String) = new SList(id = guid, userId = userId, name = guid, description = guido, created = now, updated = now)

  "Project module" should {

    "implement insertProject and call dal" in {
      val m = module()
      val dto = ListDTO(id = guido, name = guid, desc = guido, userId = Some("userId"))

      m.dalProject.insertList(any[SList]) answers (a => dal(a.asInstanceOf[SList]))

      val s = Await.result(m.projectModule.insertList(dto), Duration.Inf)
      there was one(m.dalProject).insertList(any[SList])
      s must beRight
    }

    "implement updateProject and call dal" in {
      val m = module()
      val dto = ListDTO(id = guido, name = guid, desc = guido, userId = Some("userId"))

      m.dalProject.updateLists(any[SList]) answers (a => dal(a.asInstanceOf[SList]))
      m.dalProject.getListById(any) returns dal(Some(genProject(authInfo.user.id)))
      val s = Await.result(m.projectModule.updateList(dto), Duration.Inf)
      there was one(m.dalProject).updateLists(any[SList])
      there was one(m.dalProject).getListById(dto.id.get)
      s must beRight
    }

    "return right error when dal crashes" in {
      val m = module()
      val dto = ListDTO(id = guido, name = guid, desc = guido, userId = Some("userId"))

      m.dalProject.insertList(any[SList]) returns Future.failed(new Exception("test"))
      val s = Await.result(m.projectModule.insertList(dto), Duration.Inf)
      there was one(m.dalProject).insertList(any[SList])
      s must beLeft
      val (code, message) = s.merge.asInstanceOf[ResultError]
      code === Status.INTERNAL_SERVER_ERROR
      message === "test"
    }

    "implement get user projects" in {
      val m = module()
      m.dalProject.getUserLists(anyString, any, any) returns dal((Seq(), 1))
      val s = m.projectModule.getUserLists(m.projectModule.userId, 0, 100)
      s must not be null
    }

    "get user projects and call dal" in {
      val m = module()
      val p1 = new SList(id = guid, userId = "1", name = guid, description = guido, created = now, updated = now)
      m.dalProject.getUserLists(anyString, any, any) returns dal((Seq(p1), 1))

      val s = Await.result(m.projectModule.getUserLists(m.projectModule.userId, 0, 100), Duration.Inf)

      there was one(m.dalProject).getUserLists(m.projectModule.authData.user.id, 0, 100)
      s must beRight
      val ret = s.merge.asInstanceOf[ProjectListDTO]
      ret.items.size === 1
      ret.items.head.id.get === p1.id
      ret.items.head.name === p1.name
      ret.items.head.desc === p1.description
    }

    "get user project should handle dal errors" in {
      val m = module()
      m.dalProject.getUserLists(anyString, any, any) returns dalErr("Test error")
      val s = Await.result(m.projectModule.getUserLists(m.projectModule.authData.user.id, 0, 100), Duration.Inf)
      there was one(m.dalProject).getUserLists(m.projectModule.authData.user.id, 0, 100)
      s must beLeft
      val (code, message) = s.merge.asInstanceOf[ResultError]
      code === Status.INTERNAL_SERVER_ERROR
      message === "Test error"
    }

    "get user projects and handle future failure" in {
      val m = module()
      m.dalProject.getUserLists(anyString, any, any) returns Future.failed(new RuntimeException("test future"))
      val s = Await.result(m.projectModule.getUserLists(m.projectModule.authData.user.id, 0, 100), Duration.Inf)
      there was one(m.dalProject).getUserLists(m.projectModule.authData.user.id, 0, 100)
      s must beLeft
      val (code, message) = s.merge.asInstanceOf[ResultError]
      code === Status.INTERNAL_SERVER_ERROR
      message === "test future"
    }
  }
}


