package org.shopping.modules.core.impl

import com.google.inject.Inject
import org.shopping.dal._
import org.shopping.db.{ListDef, ListDefProduct}
import org.shopping.dto._
import org.shopping.modules._
import org.shopping.modules.core.ListService
import org.shopping.util.{Constants, ErrorMessage, Gen, Time}
import play.api.http.Status

import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent._
import scala.language.postfixOps

class DefaultListService @Inject()(dalUser: UserDAL, dalList: ListDAL) extends ListService {

  override def insertList(dto: ListDTO): Result[ListDTO] = {
    val listDef = dto.toModel

    dalList
      .insertList(listDef)
      .map(
        p => resultSync(new ListDTO(p))) recover { case e: Throwable => resultExSync(e, "insertList") }
  }

  var MAX_ALLOWED = 100

  def cloneIfNotOwned(listId: String): Future[ListDef] =
    dalList.getListDefById(listId) flatMap {
      case Some(list) =>
        val uid = authData.user.id
        if (list.userId != userId) {
          val ts = Time.now()
          dalList.insertList(list.copy(id = Gen.guid, userId = uid, created = ts, createdClient = ts, updated = ts))
        } else Future.successful(list)
      case _ => throw new Exception("I couldn't find the list you look for.")
    }

  def bought(lst: Seq[ListDefProduct]) = lst.filter(_.bought == 1).map(_.productId)

  override def addListItems(listId: String, listItems: ListItemsDTO): Result[ListItemsDTO] = {
    val model = listItems.items.map(_.toModel(listId))
    cloneIfNotOwned(listId).flatMap { lst =>
      dalList.getListProductsByList(listId) flatMap { existing =>
        val f = if (existing.size > MAX_ALLOWED) {
          throw new RuntimeException(ErrorMessage.TOO_MANY_ITEMS)
        } else {
          dalList.addListDefProducts(lst.id, model).map { listItems =>
            resultSync(ListItemsDTO(items = listItems.map(new ListItemDTO(_)),
              meta = Some(ListMetadata(listId, bought(existing)))))
          }
        }

        //add meta to db
        f flatMap { ret =>
          listItems.meta match {
            case Some(meta) =>
              dalList
                .updateBatchedBought(listId, meta.boughtItems.map(_ -> true).toMap)
                .map(_ => ret)
            case _ => Future.successful(ret)
          }
        }
      }
    } recover { case e: Throwable => resultExSync(e, "addListItems") }
  }

  override def getUserLists(userId: String, offset: Int, count: Int): Result[ListsDTO] = {
    val f = for {
      res <- if (userId == userId) dalList.getUserLists(userId, offset, count) else throw new Exception("Unauthorized")
    } yield resultSync(ListsDTO(items = res._1.map(p => new ListDTO(p)).distinct, total = res._2))

    f recover { case e: Throwable => resultExSync(e, "getUserLists") }
  }

  override def updateList(dto: ListDTO): Result[ListDTO] = {
    val f = if (dto.id.isEmpty) Future.failed(new Exception("List has empty id"))
    else {
      checkUser(dto.userId.getOrElse(throw new Exception("User not found!")), dto.id.get) flatMap {
        res =>
          if (!res) throw new Exception("Not valid")
          dalList.getListDefById(dto.id.get) flatMap {
            case None => resultError(Status.NOT_FOUND, "List not found")
            case Some(list) => dalList.updateList(dto.toModel) map { p =>
              resultSync(new ListDTO(p))
            }
          }
      }
    }

    f recover { case e: Throwable => resultExSync(e, "updateList") }
  }

  private def checkUser(userId: String, listId: String): Future[Boolean] =
    dalList.getListUsers(listId) map (_.contains(userId))

  override def getListItems(listId: String): Result[ListItemsDTO] =
    checkUser(userId, listId).flatMap {
      isValid =>
        if (!isValid) throw new Exception("User is not valid in context")
        dalList.getListProductsByList(listId) map {
          items =>
            resultSync(ListItemsDTO(
              items = items.map(new ListItemDTO(_)),
              meta = Some(ListMetadata(listId, bought(items)))
            ))
        }
    } recover {
      case e: Throwable => resultExSync(e, "getListItems")
    }

  override def deleteList(listId: String): Result[BooleanDTO] =
    checkUser(userId, listId).flatMap {
      isValid =>
        if (!isValid) throw new Exception("User is not valid in context")
        dalList.getListDefById(listId) flatMap {
          case Some(list) =>
            val newList = list.copy(status = Constants.STATUS_DELETE)
            dalList.updateList(newList) map {
              x =>
                resultSync(BooleanDTO(true))
            }
          case None =>
            resultError(Status.NOT_FOUND, "List not found")
        }
    } recover {
      case e: Throwable => resultExSync(e, "deleteList")
    }

}
