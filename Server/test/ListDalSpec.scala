import org.junit.runner._
import org.shopping.dal.impl.{SlickListDAL, TestCaching}
import org.shopping.util.Gen._
import org.specs2.runner._

import scala.concurrent._
import scala.concurrent.duration._

/**
  * .
  * main test class for DefaultAssetServiceComponent
  * it mocks AssetRepoComponent
  */
@RunWith(classOf[JUnitRunner])
class ListDalSpec extends BaseDALSpec {


  //  def insertRandomProject(uid: String): (Project, Group) = {
  //    test { env =>
  //      val schema = org.planner.db.DB(env.dbConfig.driver)
  //      import schema._
  //      import env.dbConfig.driver.api._
  //
  //      val p = randProject(uid)
  //      val g = randGroup(p)
  //      val action = (for {
  //        _ <- Projects += p
  //        _ <- Groups += g
  //      } yield ()).transactionally
  //      waitFor(env.dbConfig.db.run(action))
  //      (p, g)
  //    }
  //  }

  import org.shopping.util.Time._

  "Lists DAL" should {
    def newDal(te: TestEnv) = new SlickListDAL(te.dbConfigProvider, new TestCaching)

    "insert list" in {
      test { env =>
        val dal = newDal(env)
        val schema = org.shopping.db.DB(env.dbConfig.driver)
        import env.dbConfig.driver.api._
        import schema._
        val p = org.shopping.db.List(id = guid, userId = "1", name = guid, description = guido, created = now, updated = now)
        val res = waitFor(dal.insertList(p))
        res must beAnInstanceOf[org.shopping.db.List]
        val lstProjects = waitFor(env.db.run(Lists.filter(_.id === p.id).result))
        lstProjects.size === 1
      }
    }

    "get private lists by user" in {
      test { env =>
        val dal = newDal(env)
        val resGet = Await.result(dal.getUserLists("1", 0, 100), Duration.Inf)
        val ret = resGet.asInstanceOf[(List[(org.shopping.db.List)], Int)]
        val lists = ret._1.distinct
        lists.size === 2
        lists.head.id === "1"
        lists.tail.head.id === "2"
      }
    }

  }

}

