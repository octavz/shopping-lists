package org.shopping.dal.impl

import com.google.inject.Inject
import org.shopping.dal._
import org.shopping.db._
import org.shopping.util.Constants
import org.shopping.util.Time._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile

import scala.concurrent.ExecutionContext.Implicits.global

class SlickListDAL @Inject()(protected val dbConfigProvider: DatabaseConfigProvider, cache: Caching)
  extends ListDAL
    with DB
    with HasDatabaseConfigProvider[JdbcProfile] {
  val profile = dbConfig.driver

  import dbConfig.driver.api._

  override def insertList(model: List): DAL[List] = {
    val action = (for {
      _ <- Lists += model
      _ <- ListsUsers += ListUser(model.id, model.userId)
    } yield ()).transactionally

    db run action map (_ => model)
  }

  override def updateLists(model: List): DAL[List] = {
    val newModel = model.copy(updated = now)
    db.run(Lists.filter(_.id === model.id).update(newModel)) map (_ => model)
  }

  override def getUserLists(uid: String, offset: Int, count: Int): DAL[(Seq[List], Int)] = {
    val query = Lists.filter(_.userId === uid)

    val action = (for {
      l <- query.drop(offset).take(count).result
      c <- query.length.result
    } yield (l, c)).transactionally

    db.run(action).map {
      case (l, c) => (l.toList, c)
    }
  }

  override def getListById(id: String) = {
    val action = Lists.filter(p => p.id === id && p.status =!= Constants.STATUS_DELETE).take(1).result.headOption
    db run action
  }

  override def addListItem(model: ListItem): DAL[ListItem] = {
    val action = ListProducts += model
    db.run(action) map (_ => model)
  }

  override def addListItems(model: Seq[ListItem]): DAL[Seq[ListItem]] = {
    val action = ListProducts ++= model
    db.run(action) map (_ => model)
  }

  override def getListItemsByList(listId: String): DAL[Seq[ListItem]] = {
    val action = ListProducts.filter(_.listId === listId).result
    db run action
  }

  override def getListUsers(listId: String): DAL[Seq[String]] = {
    val action = ListsUsers.filter(_.listId === listId).map(_.userId).result
    db run action
  }
}
