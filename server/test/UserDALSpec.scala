import org.junit.runner._
import org.shopping.dal.impl.{SlickUserDAL, TestCaching}
import org.shopping.db._
import org.shopping.util.Gen._
import org.shopping.util.Time._
import org.specs2.runner._
import scala.concurrent._
import scala.concurrent.duration._

/**
  * .
  * main test class for DefaultAssetServiceComponent
  * it mocks AssetRepoComponent
  */
@RunWith(classOf[JUnitRunner])
class UserDALSpec extends BaseDALSpec {

  "User DAL" should {

    "insertSession, findSessionById, deleteSessionByUser" in {
      test { env =>
        val dao = new SlickUserDAL(env.dbConfigProvider/*, new TestCaching*/)
        val schema = DB(env.dbConfig.driver)
        import schema._
        import env.dbConfig.driver.api._
        val us = UserSession(id = guid, userId = "1")
        val res = Await.result(dao.insertSession(us), Duration.Inf)
        val ret = Await.result(dao.findSessionById(us.id), Duration.Inf)
        val v = ret.asInstanceOf[Option[UserSession]]
        v must beSome
        v === Some(us)
        val retDelete = Await.result(dao.deleteSessionByUser(us.userId), Duration.Inf)
        retDelete.asInstanceOf[Int] === 1
      }
    }

    "insertUser,getUserById, getUserByEmail" in {
      test { env =>
        val dao = new SlickUserDAL(env.dbConfigProvider/*, new TestCaching*/)
        val schema = DB(env.dbConfig.driver)
        import schema._
        import env.dbConfig.driver.api._
        val usr = newUser
        val res = Await.result(dao.insertUser(usr), Duration.Inf)
        val ret = Await.result(dao.getUserById(usr.id), Duration.Inf)
        val v = ret.asInstanceOf[User]
        v === usr
        val ret1 = Await.result(dao.getUserByEmail(usr.login), Duration.Inf)
        val v1 = ret.asInstanceOf[User]
        v1 === usr
      }
    }

  }
}


