import org.planner.modules.dto.{GroupDTO, RegisterDTO}
import play.api.http.Status
import org.planner.util.Gen._
import org.planner.util.Time._
import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._
import scala.concurrent._
import scala.concurrent.duration._
import org.specs2.mock._

import org.planner.modules._
import org.planner.modules.core.impl._
import org.planner.dal._
import org.planner.db._

import scalaoauth2.provider.AuthInfo

/** .
  * main test class for DefaultAssetServiceComponent
  * it mocks AssetRepoComponent
  */
@RunWith(classOf[JUnitRunner])
class UserModuleSpec extends Specification with Mockito {

  val duration = Duration.Inf

  case class MockContext(userModule: DefaultUserModule, dalUser: UserDAL, dalAuth: Oauth2DAL)

  def userModule(dalUser: UserDAL = mock[UserDAL], dalAuth: Oauth2DAL = mock[Oauth2DAL]): MockContext = {
    val ret = new DefaultUserModule(dalUser, dalAuth)
    ret.setAuth(AuthInfo[User](user =
      User(id = guid, login = guid, password = guid, created = now, updated = now, userId = None, groupId = None,
        lastLogin = nowo, providerToken = guido, nick = guid), Some("1"), None, None))
    MockContext(ret, dalUser, dalAuth)
  }

  /**
   * generates  strings to be used in test
   * @param size the size of the string
   * @return the generated string
   */
  def genString(size: Int): String = (for (i <- 1 to size) yield "a").mkString

  def newUser = User(id = guid, login = guid, providerToken = None, created = now, userId = None, groupId = None, updated = now, lastLogin = None, password = guid, nick = guid)

  "User module" should {

    "implement createSession" in {
      val us = UserSession(userId = guid, id = guid)
      val m = userModule()

      m.dalUser.insertSession(any[UserSession]) returns Future.successful(us)
      m.dalUser.deleteSessionByUser(us.userId) returns Future.successful(1)
      m.dalAuth.findAuthInfoByAccessToken(any[scalaoauth2.provider.AccessToken]) returns Future.successful(Some(
        AuthInfo(user = newUser.copy(id = us.userId), clientId = Some("1"), scope = None, redirectUri = None)))

      val s = Await.result(m.userModule.createSession(us.id), duration)
      there was one(m.dalUser).insertSession(any[UserSession])
      s must beRight
      s.errCode === 0
      s.value.get === us.id
    }

    "implement get user by id and call dal" in {
      val id = guid
      val m = userModule()

      m.dalUser.getUserById(id) returns Future.successful(newUser.copy(id = id))
      val s = Await.result(m.userModule.getUserById(id), duration)
      there was one(m.dalUser).getUserById(id)
    }

    "implement register and call dal" in {
      val u = RegisterDTO(login = guid, password = guid)
      val m = userModule()
      m.dalUser.insertUser(any[User]) answers (a => dal(a.asInstanceOf[User]))
      m.dalUser.getUserByEmail(any[String]) returns dal(None)
      val s = Await.result(m.userModule.registerUser(u), duration)
      there was one(m.dalUser).getUserByEmail(anyString)
      there was one(m.dalUser).insertUser(any[User])
      s must beRight
    }

    "not call insert if email already exists" in {
      val u = RegisterDTO(login = guid, password = guid)
      val m = userModule()
      m.dalUser.insertUser(any[User]) answers (a => dal(a.asInstanceOf[User]))
      m.dalUser.getUserByEmail(any[String]) returns dal(Some(newUser))

      val s = Await.result(m.userModule.registerUser(u), duration)

      there was one(m.dalUser).getUserByEmail(anyString)
      there was no(m.dalUser).insertUser(any[User])
      s must beLeft
      s.errCode === 500
      s.errMessage must contain("Email already exists")
    }

    "implement add group and call dal" in {
      val dto = GroupDTO(id = None, name = guid, projectId = guid)
      val m = userModule()
      m.dalUser.insertGroupWithUser(any, any) answers (a => a match {
        case Array(g: Group, uid: String) =>
          uid === m.userModule.authData.user.id
          dal(g)
      })
      val s = Await.result(m.userModule.addGroup(dto), duration)
      there was one(m.dalUser).insertGroupWithUser(any, any)
      s must beRight
    }

    "handle add group dal error" in {
      val u = GroupDTO(id = None, name = guid, projectId = guid)
      val m = userModule()
      m.dalUser.insertGroupWithUser(any, any) returns dalErr("dal test error")
      val s = Await.result(m.userModule.addGroup(u), duration)
      there was one(m.dalUser).insertGroupWithUser(any, any)
      s must beLeft
      val (code, msg) = s.merge
      code === Status.INTERNAL_SERVER_ERROR
      msg === "dal test error"
    }

    "handle add group future error" in {
      val m = userModule()
      val u = GroupDTO(id = None, name = guid, projectId = guid)
      m.dalUser.insertGroupWithUser(any[Group], any) returns Future.failed(new Exception("test"))
      val s = Await.result(m.userModule.addGroup(u), duration)
      there was one(m.dalUser).insertGroupWithUser(any, any)
      s must beLeft
      val (code, msg) = s.merge
      code === Status.INTERNAL_SERVER_ERROR
      msg === "test"
    }

    "work wih chaining" in {

      val f1: Future[Either[String, Int]] = Future.successful(Right(1))

      val res = f1 >>= (v0 => Future.successful(Right((v0, "test")))) >>= { case (a, b) => Future.successful(Right(s"$b$a")) }
      Await.result(res, Duration.Inf) === Right("test1")
    }
  }
}
