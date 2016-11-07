package org.shopping.dal.impl

import com.google.inject.Inject
import org.shopping.dal._
import org.shopping.db._
import org.shopping.models.{ListDef, ListDefProduct, ListUser}
import org.shopping.util.Constants
import org.shopping.util.Time._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile

import scala.concurrent.ExecutionContext.Implicits.global

class SlickListRepo @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
  extends ListRepo
    with DB
    with HasDatabaseConfigProvider[JdbcProfile] {
  val profile = dbConfig.driver

  import dbConfig.driver.api._

  override def insertList(model: ListDef): DAL[ListDef] = {
    val action = for {
      _ <- ListDefs += model
      _ <- ListsUsers += ListUser(model.id, model.userId)
    } yield ()

    db run action.transactionally map (_ => model)
  }

  override def updateList(model: ListDef): DAL[ListDef] = {
    val newModel = model.copy(updated = now())
    db.run(ListDefs.filter(_.id === model.id).update(newModel)) map (_ => model)
  }

  override def getUserLists(uid: String, offset: Int, count: Int): DAL[(Seq[ListDef], Int)] = {
    val query = ListDefs.filter(d => d.userId === uid && d.status =!= Constants.STATUS_DELETE)

    val action = for {
      l <- query.drop(offset).take(count).result
      c <- query.length.result
    } yield (l, c)

    db.run(action.transactionally).map {
      case (l, c) => (l, c)
    }
  }

  override def getListDefById(id: String) = {
    db run ListDefs.filter(p => p.id === id && p.status =!= Constants.STATUS_DELETE)
      .take(1)
      .result
      .headOption
  }

  override def addListDefProducts(listId: String, model: Seq[ListDefProduct]): DAL[Seq[ListDefProduct]] = {
    if (model.nonEmpty) {
      val query = for {
        _ <- ListDefProducts ++= model
        ret <- ListDefProducts.filter(_.listDefId === listId).result
      } yield ret
      db.run(query.transactionally)
    } else {
      db.run(ListDefProducts.filter(_.listDefId === listId).result)
    }
  }

  override def getListProductsByList(listDefId: String): DAL[Seq[ListDefProduct]] = {
    db run ListDefProducts.filter(_.listDefId === listDefId).result
  }

  override def getListUsers(listId: String): DAL[Seq[String]] = {
    db run ListsUsers.filter(_.listDefId === listId).map(_.userId).result
  }

  override def updateListProduct(listProduct: ListDefProduct): DAL[ListDefProduct] =
    db.run {
      ListDefProducts
        .filter(p => p.productId === listProduct.productId && p.listDefId === listProduct.listDefId)
        .map(_.bought)
        .update(listProduct.bought)
    } map (_ => listProduct)

  override def updateBatchedBought(listId: String, ids: Map[String, Boolean]): DAL[Int] = {
    val bought = ids.filter { case (_, v) => v }.keySet
    val notbought = ids.filter { case (_, v) => !v }.keySet
    val q = for {
      q1 <- ListDefProducts.filter(p => p.productId.inSetBind(bought) && p.listDefId === listId)
        .map(_.bought)
        .update(1)
      q2 <- ListDefProducts.filter(p => p.productId.inSetBind(notbought) && p.listDefId === listId)
        .map(_.bought)
        .update(0)
    } yield q1 + q2
    db run q.transactionally
  }
}
