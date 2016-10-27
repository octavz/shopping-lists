package org.shopping.dal.impl

import com.google.inject.Inject
import org.shopping.dal._
import org.shopping.db._
import org.shopping.util.Constants
import org.shopping.util.Time._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile

import scala.concurrent.ExecutionContext.Implicits.global

class SlickListDAL @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
  extends ListDAL
    with DB
    with HasDatabaseConfigProvider[JdbcProfile] {
  val profile = dbConfig.driver

  import dbConfig.driver.api._

  override def insertList(model: FullList): DAL[FullList] = {
    val action = (for {
      _ <- ListDefs += model.listDef
      _ <- Lists += model.inst
      _ <- ListsUsers += ListUser(model.listDef.id, model.listDef.userId)
    } yield ()).transactionally

    db run action map (_ => model)
  }

  override def updateList(model: ListDef): DAL[ListDef] = {
    val newModel = model.copy(updated = now)
    db.run(ListDefs.filter(_.id === model.id).update(newModel)) map (_ => model)
  }

  override def getUserLists(uid: String, offset: Int, count: Int): DAL[(Seq[FullList], Int)] = {
    val query = for {
      (defs, insts) <- ListDefs join Lists on ((d, i) => d.id === i.listDefId && d.userId === uid && d.status =!= Constants.STATUS_DELETE && i.status =!= Constants.STATUS_DELETE)
    } yield (defs, insts)


    val action = (for {
      l <- query.drop(offset).take(count).result
      c <- query.length.result
    } yield (l, c)).transactionally

    db.run(action).map {
      case (l, c) => (l map FullList.tupled, c)
    }
  }

  override def getListDefById(id: String) = {
    val action = ListDefs.filter(p => p.id === id && p.status =!= Constants.STATUS_DELETE).take(1).result.headOption
    db run action
  }

  override def getListById(id: String) = {
    val action = Lists.filter(p => p.id === id && p.status =!= Constants.STATUS_DELETE).take(1).result.headOption
    db run action
  }

  override def addListProduct(model: ListProduct): DAL[ListProduct] = {
    val action = ListProducts += model
    db.run(action) map (_ => model)
  }

  override def addListProducts(model: Seq[ListProduct]): DAL[Seq[ListProduct]] = {
    val action = ListProducts ++= model
    db.run(action) map (_ => model)
  }

  override def getListProductsByListId(listId: String): DAL[Seq[ListProduct]] = {
    val action = ListProducts.filter(_.listId === listId).result
    db run action
  }

  override def addListDefProducts(model: Seq[ListDefProduct]): DAL[Seq[ListDefProduct]] = {
    val action = ListDefProducts ++= model
    db.run(action) map (_ => model)
  }

  override def getListDefProductsByListId(listDefId: String): DAL[Seq[ListDefProduct]] = {
    val action = ListDefProducts.filter(_.listDefId === listDefId).result
    db run action
  }

  override def getListUsers(listId: String): DAL[Seq[String]] = {
    val action = ListsUsers.filter(_.listDefId === listId).map(_.userId).result
    db run action
  }
}
