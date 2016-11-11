package org.shopping.repo.impl

import com.google.inject.Inject
import org.shopping.repo._
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

  override def insertList(model: ListDef): Repo[ListDef] = model.touch2 { m =>
    val action = for {
      _ <- ListDefs += m
      _ <- ListsUsers += ListUser(m.id, m.userId)
    } yield ()

    db run action.transactionally map (_ => m)
  }

  override def updateList(model: ListDef): Repo[ListDef] = model.touch1 { m =>
    db.run(ListDefs.filter(_.id === m.id).update(m)) map (_ => m)
  }

  override def getUserLists(uid: String, offset: Int, count: Int): Repo[(Seq[ListDef], Int)] = {
    val query = for {
      (l,_) <- (ListDefs joinLeft ListsUsers on (_.userId === _.userId)).filter {
        case (d, u) => (d.status =!= Constants.STATUS_DELETE) && (d.userId === uid || u.map(_.userId === uid))
      }
    } yield l

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

  override def replaceListItems(listId: String, model: Seq[ListDefProduct]): Repo[Seq[ListDefProduct]] = {
    val n = now()
    if (model.nonEmpty) {
      val query = for {
        _ <- ListDefProducts.filter(_.listDefId === listId).delete
        _ <- ListDefProducts ++= model.map(_.copy(updated = n))
        ret <- ListDefProducts.filter(_.listDefId === listId).result
      } yield ret
      db.run(query.transactionally)
    } else {
      db.run(ListDefProducts.filter(_.listDefId === listId).result)
    }
  }

  override def getListProductsByList(listDefId: String): Repo[Seq[ListDefProduct]] = {
    db run ListDefProducts.filter(_.listDefId === listDefId).result
  }

  override def getListUsers(listId: String): Repo[Seq[String]] = {
    db run ListsUsers.filter(_.listDefId === listId).map(_.userId).result
  }

  override def updateListProduct(listProduct: ListDefProduct): Repo[ListDefProduct] =
    db.run {
      ListDefProducts
        .filter(p => p.productId === listProduct.productId && p.listDefId === listProduct.listDefId)
        .map(a => (a.bought, a.updated))
        .update(listProduct.bought, now())
    } map (_ => listProduct)

  override def updateBatchedBought(listId: String, ids: Map[String, Boolean]): Repo[Int] = {
    val bought = ids.filter { case (_, v) => v }.keySet
    val notBought = ids.filter { case (_, v) => !v }.keySet
    val n = now()
    val q = for {
      q1 <- ListDefProducts.filter(p => p.productId.inSetBind(bought) && p.listDefId === listId)
        .map(a => (a.bought, a.updated))
        .update(1, n)
      q2 <- ListDefProducts.filter(p => p.productId.inSetBind(notBought) && p.listDefId === listId)
        .map(a => (a.bought, a.updated))
        .update(0, n)
    } yield q1 + q2
    db run q.transactionally
  }
}
