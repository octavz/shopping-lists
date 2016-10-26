package org.shopping.modules.core.impl

import com.google.inject.Inject
import org.shopping.dal._
import org.shopping.dto._
import org.shopping.modules._
import org.shopping.modules.core.ListService
import org.shopping.util.{Constants, ErrorMessage}
import play.api.http.Status

import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent._
import scala.language.postfixOps

class DefaultListService @Inject()(dalUser: UserDAL, dalList: ListDAL) extends ListService {

  override def insertList(dto: ListDTO): Result[ListDTO] = {
    val list = dto.toModel
    val f = dalList.insertList(list) map (p => resultSync(new ListDTO(p)))
    f recover { case e: Throwable => resultExSync(e, "insertList") }
  }

  var MAX_ALLOWED = 100

  override def addListItems(listId: String, listItems: ListItemsDTO): Result[ListItemsDTO] = {
    val model = listItems.items.map(_.toModel(authData.user.id, listId))
    val f = dalList.getListItemsByList(listId) flatMap { existing =>
      if (existing.size > MAX_ALLOWED) resultError(ErrorMessage.TOO_MANY_ITEMS)
      else dalList.addListItems(model).map { r =>
        resultSync(ListItemsDTO(items = r.map(s => new ListItemDTO(s)),meta = ListMetadata(List())))
      }
    }

    f recover { case e: Throwable => resultExSync(e, "addListItems") }
  }

  override def getUserLists(id: String, offset: Int, count: Int): Result[ListsDTO] = {
    println(s"Wanted for: $id - current user: $userId")
    val f = for {
      res <- if (id == userId) dalList.getUserLists(userId, offset, count) else throw new Exception("Unauthorized")
    } yield resultSync(ListsDTO(items = res._1.map(p => new ListDTO(p)).distinct, total = res._2))

    f recover { case e: Throwable => resultExSync(e, "getUserLists") }
  }

  override def updateList(dto: ListDTO): Result[ListDTO] = {
    val f = if (dto.id.isEmpty) Future.failed(new Exception("List has empty id"))
    else {
      checkUser(dto.userId.getOrElse(throw new Exception("User not found!")), dto.id.get) flatMap {
        res =>
          if (!res) throw new Exception("Not valid")
          dalList.getListById(dto.id.get) flatMap {
            case None => resultError(Status.NOT_FOUND, "List not found")
            case Some(list) => dalList.updateLists(dto.toModel) map { p =>
              resultSync(new ListDTO(p))
            }
          }
      }
    }

    f recover { case e: Throwable => resultExSync(e, "updateList") }
  }

  private def checkUser(userId: String, listId: String): Future[Boolean] =
    dalList.getListUsers(listId) map (_.contains(userId))

  override def getListItems(listId: String): Result[ListItemsDTO] = {
    val f = checkUser(userId, listId) flatMap {
      isValid =>
        if (!isValid) throw new Exception("User is not valid in context")
        dalList.getListItemsByList(listId) map {
          res =>
            resultSync(ListItemsDTO(items = res.map(t => new ListItemDTO(t)), meta = ListMetadata(List())))
        }
    }

    f recover {
      case e: Throwable => resultExSync(e, "getListItems")
    }
  }

  override def deleteList(listId: String): Result[BooleanDTO] = {
    val f = checkUser(userId, listId) flatMap {
      isValid =>
        if (!isValid) throw new Exception("User is not valid in context")
        dalList.getListById(listId) flatMap {
          case Some(list) =>
            val newList = list.copy(status = Constants.STATUS_DELETE)
            dalList.updateLists(newList) map {
              x =>
                resultSync(BooleanDTO(true))
            }
          case None =>
            resultError(Status.NOT_FOUND, "List not found")
        }
    }

    f recover {
      case e: Throwable => resultExSync(e, "deleteList")
    }
  }

}
