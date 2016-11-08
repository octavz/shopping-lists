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

  def combineListProducts(l1: Seq[ListDefProduct], l2: Seq[ListDefProduct]): Seq[ListDefProduct] =
    (l1 ++ l2).groupBy(_.productId).map {
      case (_, l) => l.head.copy(quantity = l.size)
    }.toSeq

  override def addListItems(listId: String, listItems: ListItemsDTO): Result[ListItemsDTO] = {
    val now = Time.now()
    val notExisting = listItems.items.filter(_.productId.isEmpty)
    val models = notExisting
      .map { p => Product(
        id = Gen.guid,
        userId = userId,
        name = p.description.getOrElse("No description"),
        created = now,
        updated = now)
      }

    val f = for {
      _ <- productRepo.insertProducts(models)
      lId <- cloneIfNotOwned(listId) map (_.id)
      existing <- listRepo.getListProductsByList(lId)
      items <- if (existing.size > MAX_ALLOWED) {
        throw new RuntimeException(ErrorMessages.TOO_MANY_ITEMS)
      } else {
        val all = models.map(p => ListDefProduct(
          listDefId = lId,
          productId = p.id,
          description = p.description,
          created = now,
          updated = now)) ++ listItems.items.filter(_.productId.isDefined).map(p => p.toModel(listId, p.productId.get))
        listRepo.addListDefProducts(lId, combineListProducts(all, existing))
      }
      _ <- listItems.meta match {
        case Some(meta) =>
          listRepo.updateBatchedBought(lId, meta.boughtItems.map(_ -> true).toMap)
        case _ => Future.successful(())
      }
    } yield {
      resultSync(ListItemsDTO(items = items.map(new ListItemDTO(_)),
        meta = Some(ListMetadata(listId, bought(existing)))))
    }

    f recover {
      case e: Throwable => resultExSync(e, "addListItems")
    }
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
