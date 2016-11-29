package org.shopping.services.impl

import com.google.inject.Inject
import org.shopping.dto._
import org.shopping.models.{ListDefProduct, ListWithItems, Product}
import org.shopping.repo._
import org.shopping.services.{ListService, _}
import org.shopping.util.ErrorMessages._
import org.shopping.util.{Constants, ErrorMessages, Gen}
import play.api.http.Status

import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent._
import scala.language.postfixOps

class DefaultListService @Inject()(userRepo: UserRepo, listRepo: ListRepo, productRepo: ProductRepo) extends ListService {
  private[impl] val MAX_ALLOWED = 100

  override def insertList(dto: ListDTO)(implicit a: AuthData): Result[ListDTO] = {
    val listDef = dto.toModel(Gen.guid, userId)
    listRepo.insertList(listDef) flatMap { list =>
      setItems(ListWithItems(listDef, Nil), dto.items.getOrElse(Nil).take(MAX_ALLOWED)) map {
        case Right(its) => resultSync(new ListDTO(list, its.toList))
        case Left(err) => errorSync(err)
      }
    } recover {
      case e: Throwable => exSync(e, "insertList")
    }
  }

  private[impl] def cloneIfNotOwned(listId: String)(implicit a: AuthData): Result[ListWithItems] =
    listRepo.getListDefById(listId) flatMap {
      case Some(ListWithItems(list, items)) =>
        val f: Result[ListWithItems] = if (list.userId != userId) {
          val newList = list.copy(id = Gen.guid, userId = userId)
          listRepo.insertList(newList) flatMap { nl =>
            val newItems = items.map(_.copy(listDefId = nl.id))
            if (newItems.nonEmpty)
              listRepo.replaceListItems(newList.id, newItems) map (_ => Right(ListWithItems(nl, newItems)))
            else
              result(ListWithItems(nl, Nil))
          }
        } else {
          Future.successful(Right(ListWithItems(list, items)))
        }

        f recover {
          case e: Throwable => exSync(e, "clone failed")
        }
      case _ => error(NOT_FOUND)
    }

  private[impl] def bought(lst: Seq[ListDefProduct]) = lst.filter(_.bought == 1).map(_.productId)

  private[impl] def combineListProducts(l1: Seq[ListDefProduct], l2: Seq[ListDefProduct]): Seq[ListDefProduct] =
    (l1 ++ l2).groupBy(_.productId).map {
      case (_, l) => l.head.copy(quantity = l.size)
    }.toSeq


  private[services] def setItems(fullList: ListWithItems, items: Seq[ListItemDTO])
    (implicit a: AuthData): Result[Seq[ListItemDTO]] =
    if (items.size > MAX_ALLOWED) {
      error(ErrorMessages.TOO_MANY_ITEMS)
    } else if (items.isEmpty) {
      result(Nil)
    } else {
      val listId = fullList.list.id
      def prod2item(p: Product): ListDefProduct =
        ListDefProduct(listDefId = listId, productId = p.id, description = p.description)
      val (woId, withId) = items.partition(_.productId.isEmpty)
      val notExistingProducts = woId.map(p =>
        Product(id = Gen.guid, userId = userId, name = p.description.getOrElse(""), tags = p.description.getOrElse("").toLowerCase))
      productRepo.insertProducts(notExistingProducts) flatMap { _ =>
        val all = notExistingProducts.map(prod2item) ++ withId.map(p => p.toModel(listId, p.productId.get))
        if (all.nonEmpty)
          listRepo.replaceListItems(listId, all).map(_.map(new ListItemDTO(_))) map resultSync
        else
          result(Nil)
      } recover {
        case e: Throwable => exSync(e)
      }
    }

  override def getUserLists(uid: String, offset: Int, count: Int)(implicit a: AuthData): Result[ListsDTO] = {
    listRepo.getUserLists(userId, offset, count) map {
      case (seq, total) => resultSync(ListsDTO(items = seq.map(l => new ListDTO(l.list, l.items)), total = total))
    } recover {
      case e: Throwable => exSync(e, "getUserLists")
    }
  }

  override def updateLists(lists: ListsDTO)(implicit a: AuthData): Result[ListsDTO] =
    Future.sequence(lists.items map updateList) map {
      seqEitherWithFold(_)(Seq.empty[ListDTO])(_ :+ _).right map (ListsDTO(_))
    }

  override def insertLists(lists: ListsDTO)(implicit a: AuthData): Result[ListsDTO] =
    Future.sequence(lists.items map insertList) map {
      seqEitherWithFold(_)(Seq.empty[ListDTO])(_ :+ _).right map (ListsDTO(_))
    }

  override def updateList(dto: ListDTO)(implicit a: AuthData): Result[ListDTO] = dto.id match {
    case None =>
      error(ErrorMessages.EMPTY_ID)
    case Some(id) =>
      checkUser(valid(id)) {
        cloneIfNotOwned(id).flatMap {
          case Right(lst) =>
            setItems(lst, dto.items.getOrElse(Nil)).flatMap {
              case Right(its) =>
                listRepo.updateList(dto.toModel(lst.list.id, userId)).map { p =>
                  resultSync(new ListDTO(p, its.toList))
                }
              case Left(err) => error(err)
            }
          case Left(err) => error(err)
        }
      } recover {
        case e: Throwable =>
          exSync(e, "updateList")
      }
  }

  private def valid(listId: String)(implicit a: AuthData): Future[Boolean] =
    listRepo.getListUsers(listId) map (_.contains(userId))

  override def deleteList(listId: String)(implicit a: AuthData): Result[BooleanDTO] =
    checkUser(valid(listId)) {
      listRepo.getListDefById(listId) flatMap {
        case Some(ListWithItems(list, items)) =>
          listRepo.updateList(list.copy(status = Constants.STATUS_DELETE)) map { _ =>
            resultSync(BooleanDTO(true))
          }
        case None =>
          error(Status.NOT_FOUND -> ErrorMessages.NOT_FOUND)
      }
    } recover {
      case e: Throwable => exSync(e, "deleteList")
    }

}
