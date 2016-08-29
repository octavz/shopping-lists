import org.junit.runner._
import org.planner.dal.impl.{SlickUserDAL, TestCaching}
import org.planner.db._
import org.planner.util.Gen._
import org.planner.util.Time._
import org.specs2.runner._
import play.api.db.evolutions.Evolutions
import play.api.test.WithApplication
import scala.concurrent._
import scala.concurrent.duration._
import ExecutionContext.Implicits.global

/**
 * .
 * main test class for DefaultAssetServiceComponent
 * it mocks AssetRepoComponent
 */
@RunWith(classOf[JUnitRunner])
class UserDALSpec extends BaseDALSpec {

  import TestDB._
  import TestDB.profile.api._

  def newDao = new SlickUserDAL(new TestCaching)

  "User DAL" should {

    //
    "insertSession, findSessionById, deleteSessionByUser" in  {
      test {
        val dao = newDao
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

    "insertUser,getUserById, getUserByEmail" in  {
      test {
        val dao = newDao
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

    "insertGroupWithUser" in  {
      test {
        val dao = newDao
        val model = Group(id = guid, projectId = "1", name = guid, updated = now, created = now, userId = "1", groupId = None)
        val res = Await.result(dao.insertGroupWithUser(model, "1"), Duration.Inf)
        dbSync(Groups.filter(_.id === model.id).result).size === 1
        dbSync(GroupsUsers.filter(_.groupId === model.id).result).size === 1
      }
    }

    "insertGroup, insertGroupsUser" in  {
      test {
        val dao = newDao
        val model = Group(id = guid, projectId = "1", name = guid, updated = now, created = now, userId = "1", groupId = None)
        val res = Await.result(dao.insertGroup(model), Duration.Inf)
        dbSync(Groups.filter(_.id === model.id).result).size === 1
        val res1 = Await.result(dao.insertGroupsUser(GroupsUser(groupId = model.id, userId = "1")), Duration.Inf)
        dbSync(GroupsUsers.filter(_.groupId === model.id).result).size === 1
      }
    }

    "get user user group ids" in  {
      test {
        val dao = newDao
        val groups = Await.result(dao.getUserGroupsIds("1"), Duration.Inf)
        println(groups)
        groups.size === 4
      }
    }
  }
}


