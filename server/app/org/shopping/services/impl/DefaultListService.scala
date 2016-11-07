package org.shopping.services.impl

import com.google.inject.Inject
import org.shopping.dal._
import org.shopping.dto._
import org.shopping.models.{ListDef, ListDefProduct, Product}
import org.shopping.services.{ListService, _}
import org.shopping.util.{Constants, ErrorMessages, Gen, Time}
import play.api.http.Status

import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent._
import scala.language.postfixOps

class DefaultListService @Inject()(userRepo: UserRepo, listRepo: ListRepo, productRepo: ProductRepo) extends ListService {

  override def insertList(dto: ListDTO): Result[ListDTO] = {
    val listDef = dto.toModel(Gen.guid)

    listRepo
      .insertList(listDef)
      .map(
        p => resultSync(new ListDTO(p))) recover { case e: Throwable => resultExSync(e, "insertList") }
  }

  var MAX_ALLOWED = 100

  def cloneIfNotOwned(listId: String): Future[ListDef] =
    listRepo.getListDefById(listId) flatMap {
      case Some(list) =>
        val uid = authData.user.id
        if (list.userId != userId) {
          val ts = Time.now()
          listRepo.insertList(list.copy(id = Gen.guid, userId = uid, created = ts, createdClient = ts, updated = ts))
        } else Future.successful(list)
      case _ => throw new Exception("I couldn't find the list you look for.")
    }

  def bought(lst: Seq[ListDefProduct]) = lst.filter(_.bought == 1).map(_.productId)

  override def addListItems(listId: String, listItems: ListItemsDTO): Result[ListItemsDTO] = {
    val now = Time.now()
    val notExisting = listItems.items.filter(_.productId.isEmpty)
    val models = notExisting
      .map {
        p =>
          val pid = Gen.guid
          pid -> Product(pid, userId, p.description.getOrElse("No description"), created = now, updated = now)
      }.toMap
    val
    val allMap = models ++=

    productRepo.insertProducts(models.values.toSeq).map { _ =>
      cloneIfNotOwned(listId).flatMap { lst =>
        listRepo.getListProductsByList(listId) flatMap { existing =>
          val f = if (existing.size > MAX_ALLOWED) {
            throw new RuntimeException(ErrorMessages.TOO_MANY_ITEMS)
          } else {
            listRepo.addListDefProducts(lst.id, model).map { listItems =>
              resultSync(ListItemsDTO(items = listItems.map(new ListItemDTO(_)),
                meta = Some(ListMetadata(listId, bought(existing)))))
            }
          }

          //add meta to db
          f flatMap { ret =>
            listItems.meta match {
              case Some(meta) =>
                listRepo
                  .updateBatchedBought(listId, meta.boughtItems.map(_ -> true).toMap)
                  .map(_ => ret)
              case _ => Future.successful(ret)
            }
          }
        }
      }
    } recover { case e: Throwable => resultExSync(e, "addListItems") }
  }

  override def getUserLists(userId: String, offset: Int, count: Int): Result[ListsDTO] = {
    val f = for {
      res <- if (userId == userId) listRepo.getUserLists(userId, offset, count) else throw new Exception("Unauthorized")
    } yield resultSync(ListsDTO(items = res._1.map(p => new ListDTO(p)).distinct, total = res._2))

    f recover {
      case e: Throwable => resultExSync(e, "getUserLists")
    }
  }

  override def updateList(dto: ListDTO): Result[ListDTO] = dto.id match {
    case None =>
      Future.failed(new Exception("List has empty id"))
    case Some(id) =>
      checkUser(valid(id)) {
        listRepo.getListDefById(id) flatMap {
          case None => resultError(Status.NOT_FOUND, "List not found")
          case Some(list) => listRepo.updateList(dto.toModel(dto.id.get)) map {
            p =>
              resultSync(new ListDTO(p))
          }
        }
      } recover {
        case e: Throwable => resultExSync(e, "updateList")
      }
  }

  private def valid(listId: String): Future[Boolean] =
    listRepo.getListUsers(listId) map (_.contains(userId))

  override def getListItems(listId: String): Result[ListItemsDTO] =
    checkUser(valid(listId)) {
      listRepo.getListProductsByList(listId) map {
        items =>
          resultSync(ListItemsDTO(
            items = items.map(new ListItemDTO(_)),
            meta = Some(ListMetadata(listId, bought(items)))))
      }
    } recover {
      case e: Throwable => resultExSync(e, "getListItems")
    }

  override def deleteList(listId: String): Result[BooleanDTO] =
    checkUser(valid(listId)) {
      listRepo.getListDefById(listId) flatMap {
        case Some(list) =>
          val newList = list.copy(status = Constants.STATUS_DELETE)
          listRepo.updateList(newList) map {
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
