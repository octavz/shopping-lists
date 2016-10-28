import org.junit.runner._
import org.shopping.dal.impl.SlickListDAL
import org.shopping.db.ListDef
import org.shopping.util.Gen._
import org.specs2.runner._

import scala.concurrent._
import scala.concurrent.duration._

@RunWith(classOf[JUnitRunner])
class ListDALSpec extends BaseDALSpec {


  import org.shopping.util.Time._

  "Lists DAL" should {
    def newDal(te: TestEnv) = new SlickListDAL(te.dbConfigProvider /*, new TestCaching*/)

    "insert list" in {
      test { env =>
        val dal = newDal(env)
        val schema = org.shopping.db.DB(env.dbConfig.driver)
        import env.dbConfig.driver.api._
        import schema._
        val model = ListDef(id = guid, userId = "1", name = guid, description = guido, createdClient = now(), created = now(), updated = now())
        val res = waitFor(dal.insertList(model))
        res must beAnInstanceOf[ListDef]
        val defs = waitFor(env.db.run(ListDefs.filter(_.id === model.id).result))
        defs.size === 1
      }
    }

    "get private lists by user" in {
      test { env =>
        val dal = newDal(env)
        val resGet = Await.result(dal.getUserLists("1", 0, 100), Duration.Inf)
        val ret = resGet.asInstanceOf[(Seq[ListDef], Int)]
        val lists = ret._1.distinct
        lists.size === 2
        lists.head.id === "1"
      }
    }

  }

}

