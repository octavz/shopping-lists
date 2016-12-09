package org.shopping.repo

import org.shopping.models.{ListDef, ListDefProduct}
import org.shopping.repo.impl.SlickListRepo
import org.shopping.util.Gen._

import scala.concurrent.ExecutionContext.Implicits.global

class SlickListRepoTest extends BaseRepoSpec {

  import org.shopping.util.Time._

  "Lists Repo" must {
    def newRepo(te: TestEnv) = new SlickListRepo(te.dbConfigProvider /*, new TestCaching*/)

    "insert list" in {
      test { env =>
        val repo = newRepo(env)
        val schema = org.shopping.db.DB(env.dbConfig.driver)
        import env.dbConfig.driver.api._
        import schema._
        val model = ListDef(id = guid, userId = "1", name = guid, description = guido, createdClient = now(), created = now(), updated = now())
        waitFor(repo.insertList(model))
        val defs = waitFor(env.db.run(ListDefs.filter(_.id === model.id).result))
        defs.size === 1
        defs.head.id === model.id
        defs.head.name === model.name
      }
    }

    "implement getUserLists and return all user lists" in {
      test { env =>
        val repo = newRepo(env)
        val ret = waitFor(repo.getUserLists("1", 0, 100))
        val lists = ret._1.distinct
        lists.size === 2
        lists.sortBy(_.list.id).head.list.id === "1"
      }
    }

    "handle updating a list correctly" in {
      test { env =>
        val repo = newRepo(env)
        val schema = org.shopping.db.DB(env.dbConfig.driver)
        import env.dbConfig.driver.api._
        import schema._
        val ldef = ListDef("listId", "1", "test_list")
        waitFor(env.db.run(ListDefs += ldef))
        waitFor(repo.updateList(ldef.copy(name = "updated")))
        val l = waitFor(env.db.run(ListDefs.filter(_.id === "listId").result))
        l.size === 1
        l.head.name === "updated"
      }
    }

    "implement getListDefById" in {
      test { env =>
        val repo = newRepo(env)
        val schema = org.shopping.db.DB(env.dbConfig.driver)
        import env.dbConfig.driver.api._
        import schema._
        val ldef = ListDef("listId", "1", "test_list")
        waitFor(env.db.run(ListDefs += ldef))
        val l = waitFor(repo.getListDefById(ldef.id))
        assert(l.isDefined)
        l.get.list.name === "test_list"
      }
    }

    "implement getListDefById and return attached items" in {
      test { env =>
        val repo = newRepo(env)
        val l = waitFor(repo.getListDefById("1"))
        assert(l.isDefined)
        l.get.list.name === "test_list"
        l.get.items.size === 2
      }
    }

    "implement replaceListItems and correctly returns the list" in {
      test { env =>
        val repo = newRepo(env)
        val schema = org.shopping.db.DB(env.dbConfig.driver)
        import env.dbConfig.driver.api._
        import schema._
        val listId = guid
        val ldef = ListDef(listId, "1", "test_list")
        val oldItems = Seq(ListDefProduct(listId, "1", None))
        waitFor(env.db.run(ListDefs += ldef))
        oldItems.foreach(i => waitFor(env.db.run(ListDefProducts += i)))
        val all = waitFor(env.db.run(ListDefProducts.filter(_.listDefId === listId).result))
        all.size === 1
        all.head.productId === "1"
        val newItems = Seq(ListDefProduct(listId, "2", None), ListDefProduct(listId, "3", None), ListDefProduct(listId, "4", None))
        val l = waitFor(repo.replaceListItems(listId, newItems))
        l.size === 3
        l.map(_.productId) must contain allOf("2", "3", "4")
      }
    }

    "implement getListProductsByList and correctly update the list" in {
      test { env =>
        val repo = newRepo(env)
        val l = waitFor(repo.getListProductsByList("1"))
        l.size === 2
      }
    }

    "implement getListUsers and correctly update the list" in {
      test { env =>
        val repo = newRepo(env)
        val l = waitFor(repo.getListUsers("1"))
        l.size === 1
        l.head === "1"
      }
    }

    "correctly update bought flag in updateListProduct" in {
      test { env =>
        val repo = newRepo(env)
        val schema = org.shopping.db.DB(env.dbConfig.driver)
        import env.dbConfig.driver.api._
        import schema._
        val model = ListDefProduct("1", "1", None, 1)
        waitFor(repo.updateListProduct(model))
        val res = waitFor(env.db.run(ListDefProducts.filter(a => a.productId === "1" && a.listDefId === "1").result))
        res.size === 1
        res.head.bought === 1
      }
    }
  }

}

