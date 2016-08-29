import org.junit.runner._
import org.planner.dal.impl.{SlickProjectDAL, TestCaching}
import org.planner.db.{Group, Project, User}
import org.planner.util.Gen._
import org.specs2.runner._
import play.api.test.WithApplication

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._
import scala.concurrent.duration._

/**
 * .
 * main test class for DefaultAssetServiceComponent
 * it mocks AssetRepoComponent
 */
@RunWith(classOf[JUnitRunner])
class ProjectDALSpec extends BaseDALSpec {

  import TestDB._
  import TestDB.profile.api._

  def newDal = new SlickProjectDAL(new TestCaching)

  def insertRandomProject(uid: String): (Project, Group) = {
    running(testApp) {
      val p = randProject(uid)
      val g = randGroup(p)
      val action = (for {
        _ <- Projects += p
        _ <- Groups += g
      } yield ()).transactionally
      waitFor(db.run(action))
      (p, g)
    }
  }
  import org.planner.util.Time._

  "Project DAL" should {

    "insert project and the default group" in {
      test {
        val dal = newDal
        val p = Project(id = guid, userId = "1", name = guid, description = guido, parentId = None, created = now, updated = now)
        val g = Group(id = guid, projectId = p.id, name = p.name, created = now, updated = now, userId = "1", groupId = None)
        val res = waitFor(dal.insertProject(p, g))
        res must beAnInstanceOf[Project]
        val lstProjects = waitFor(db.run(Projects.filter(_.id === p.id).result))
        lstProjects.size === 1
        val lstGroups = waitFor(db.run(Groups.filter(_.id === g.id).result)).toList
        lstGroups.size === 1
        val lstGroupsUsers = waitFor(db.run(GroupsUsers.filter(_.groupId === g.id).result)).toList
        lstGroupsUsers.size === 1
      }
    }

    "get private projects by user" in  {
      test {
        val dal = newDal
        val resGet = Await.result(dal.getUserProjects("1", 0, 100), Duration.Inf)
        val ret = resGet.asInstanceOf[(List[(Group, Project)], Int)]
        val groups = ret._1.map(x => x._1).distinct
        val projects = ret._1.map(x => x._2).distinct
        projects.size === 2
        projects.head.id === "1"
      }
    }
    //
    //    "get public projects by user" in new WithApplication(testApp) {
    //      val dal = newDal
    //      val resGet = Await.result(dal.getUserPublicProjects("1", 0, 1), Duration.Inf)
    //      val ret = resGet.asInstanceOf[(List[(Group, Project)], Int)]
    //      ret._1.size === 0
    //    }

    "get project groups" in new WithApplication(testApp) {
      test {
        val dal = newDal
        val projects = Await.result(dal.getProjectGroups("1"), Duration.Inf)
        projects must beAnInstanceOf[List[Group]]
        projects.size === 2
      }
    }
  }

}

