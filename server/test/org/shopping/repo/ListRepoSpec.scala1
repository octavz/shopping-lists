package org.shopping.repo

import org.junit.runner._
import org.shopping.repo.impl.SlickListRepo
import org.shopping.models.ListDef
import org.shopping.util.Gen._
import org.specs2.runner._

import scala.concurrent._
import scala.concurrent.duration._

@RunWith(classOf[JUnitRunner])
class ListRepoSpec extends BaseRepoSpec {


  import org.shopping.util.Time._

  "Lists Repo" should {
    def newRepo(te: TestEnv) = new SlickListRepo(te.dbConfigProvider /*, new TestCaching*/)

    "insert list" in {
      test { env =>
        val repo = newRepo(env)
        val schema = org.shopping.db.DB(env.dbConfig.driver)
        import env.dbConfig.driver.api._
        import schema._
        val model = ListDef(id = guid, userId = "1", name = guid, description = guido, createdClient = now(), created = now(), updated = now())
        val res = waitFor(repo.insertList(model))
        res must beAnInstanceOf[ListDef]
        val defs = waitFor(env.db.run(ListDefs.filter(_.id === model.id).result))
        defs.size === 1
      }
    }

    "get private lists by user" in {
      test { env =>
        val repo = newRepo(env)
        val ret = Await.result(repo.getUserLists("1", 0, 100), Duration.Inf)
        val lists = ret._1.distinct
        lists.size === 2
        lists.sortBy(_.list.id).head.list.id === "1"
      }
    }

  }

}

