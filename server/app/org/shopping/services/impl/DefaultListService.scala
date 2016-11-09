package org.shopping.services.impl

import com.google.inject.Inject
import org.shopping.repo._
import org.shopping.dto._
import org.shopping.models.{ListDef, ListDefProduct, Product}
import org.shopping.services.{ListService, _}
import org.shopping.util.ErrorMessages._
import org.shopping.util.{Constants, ErrorMessages, Gen}
import play.api.http.Status

import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent._
import scala.language.postfixOps

class DefaultListService @Inject()(userRepo: UserRepo, listRepo: ListRepo, productRepo: ProductRepo) extends ListService {

  override def insertList(dto: ListDTO): Result[ListDTO] = {
    val listDef = dto.toModel(Gen.guid)

    listRepo
      .insertList(listDef)
      .map(p => resultSync(new ListDTO(p)))
      .recover {
        case e: Throwable => exSync(e, "insertList")
      }
  }

  private[impl] val MAX_ALLOWED = 100

  private[impl] def cloneIfNotOwned(listId: String): Result[ListDef] =
    listRepo.getListDefById(listId) flatMap {
      case Some(list) =>
        val ret =
          if (list.userId != userId) {
            listRepo.insertList(list.copy(id = Gen.guid, userId = userId))
          } else {
            Future.successful(list)
          }
        ret.map(resultSync)
      case _ => error(NOT_FOUND)
    }

  private[impl] def bought(lst: Seq[ListDefProduct]) = lst.filter(_.bought == 1).map(_.productId)

  private[impl] def combineListProducts(l1: Seq[ListDefProduct], l2: Seq[ListDefProduct]): Seq[ListDefProduct] =
    (l1 ++ l2).groupBy(_.productId).map {
      case (_, l) => l.head.copy(quantity = l.size)
    }.toSeq

  override def addListItems(listId: String, listItems: ListItemsDTO): Result[ListItemsDTO] = {
    val models = listItems.items
      .filter(_.productId.isEmpty)
      .map(p => Product(id = Gen.guid, userId = userId, name = p.description.getOrElse("")))
    val f1 = for {
      _ <- productRepo.insertProducts(models)
      l <- cloneIfNotOwned(listId)
    } yield l

    val f: Result[ListItemsDTO] = f1 flatMap {
      case Right(l) =>
        val lId = l.id
        for {
          existing <- listRepo.getListProductsByList(lId)
          items <- {
            if (existing.size > MAX_ALLOWED) {
              error(ErrorMessages.TOO_MANY_ITEMS)
            } else {
              val all = models.map(p => ListDefProduct(listDefId = lId, productId = p.id, description = p.description)) ++
                listItems.items.filter(_.productId.isDefined).map(p => p.toModel(listId, p.productId.get))
              listRepo.replaceListItems(lId, combineListProducts(all, existing)).map(_.map(new ListItemDTO(_))).map(resultSync)
            }
          }
          _ <- listItems.meta match {
            case Some(meta) =>
              listRepo.updateBatchedBought(lId, meta.boughtItems.map(_ -> true).toMap)
            case _ => Future.successful(())
          }
        } yield {
          items match {
            case Left(err) => errorSync(400, ErrorMessages.SERVER_ERROR)
            case Right(its) => resultSync(ListItemsDTO(items = its, meta = Some(ListMetadata(listId, bought(existing)))))
          }
        }
      case (Left(err)) => error(ErrorMessages.SERVER_ERROR)
    }

    f recover {
      case e: Throwable => exSync(e)
    }
  }

  override def getUserLists(uid: String, offset: Int, count: Int): Result[ListsDTO] = {
    listRepo.getUserLists(userId, offset, count) map {
      case (seq, total) => resultSync(ListsDTO(items = seq.map(new ListDTO(_)), total = total))
    } recover {
      case e: Throwable => exSync(e, "getUserLists")
    }
  }

  override def updateList(dto: ListDTO): Result[ListDTO] = dto.id match {
    case None => error(ErrorMessages.EMPTY_ID)
    case Some(id) =>
      checkUser(valid(id)) {
        listRepo.getListDefById(id) flatMap {
          case None => error(errCode = Status.NOT_FOUND, errMessage = ErrorMessages.NOT_FOUND)
          case Some(list) => listRepo.updateList(dto.toModel(dto.id.get)) map {
            p =>
              resultSync(new ListDTO(p))
          }
        }
      } recover {
        case e: Throwable => exSync(e, "updateList")
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
      case e: Throwable => exSync(e, "getListItems")
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
          error(errCode = Status.NOT_FOUND, errMessage = ErrorMessages.NOT_FOUND)
      }
    } recover {
      case e: Throwable => exSync(e, "deleteList")
    }

}
