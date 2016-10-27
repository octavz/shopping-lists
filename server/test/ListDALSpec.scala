import org.junit.runner._
import org.shopping.dal.impl.{SlickListDAL, TestCaching}
import org.shopping.db.{FullList, ListDef, ListInst}
import org.shopping.util.Gen._
import org.specs2.runner._

import scala.concurrent._
import scala.concurrent.duration._

@RunWith(classOf[JUnitRunner])
class ListDALSpec extends BaseDALSpec {


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
    def newDal(te: TestEnv) = new SlickListDAL(te.dbConfigProvider /*, new TestCaching*/)

    "insert list" in {
      test { env =>
        val dal = newDal(env)
        val schema = org.shopping.db.DB(env.dbConfig.driver)
        import env.dbConfig.driver.api._
        import schema._
        val listDef = ListDef(id = guid, userId = "1", name = guid, description = guido, created = now(), updated = now())
        val listInst = ListInst(id = guid, userId = listDef.userId, listDefId = listDef.id, createdClient = 1000, created = now(), updated = now())
        val model = FullList(listDef, listInst)
        val res = waitFor(dal.insertList(model))
        res must beAnInstanceOf[FullList]
        val instance = waitFor(env.db.run(Lists.filter(_.id === listInst.id).result))
        val defs = waitFor(env.db.run(ListDefs.filter(_.id === listDef.id).result))
        instance.size === 1
        defs.size === 1
      }
    }

    "get private lists by user" in {
      test { env =>
        val dal = newDal(env)
        val resGet = Await.result(dal.getUserLists("1", 0, 100), Duration.Inf)
        val ret = resGet.asInstanceOf[(Seq[FullList], Int)]
        val lists = ret._1.distinct
        lists.size === 2
        lists.head.inst.id === "1"
        lists.tail.head.inst.id === "2"
      }
    }

  }

}

