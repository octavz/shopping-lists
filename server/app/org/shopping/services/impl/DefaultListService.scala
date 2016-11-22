package org.shopping.services.impl

import com.google.inject.Inject
import org.shopping.repo._
import org.shopping.dto._
import org.shopping.models.{ListDefProduct, ListWithItems, Product}
import org.shopping.services.{ListService, _}
import org.shopping.util.ErrorMessages._
import org.shopping.util.{Constants, ErrorMessages, Gen}
import play.api.http.Status

import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent._
import scala.language.postfixOps

class DefaultListService @Inject()(userRepo: UserRepo, listRepo: ListRepo, productRepo: ProductRepo) extends ListService {
  private[impl] val MAX_ALLOWED = 100

  override def insertList(dto: ListDTO)(implicit authData: AuthData): Result[ListDTO] = {
    val listDef = dto.toModel(Gen.guid, userId)

    val f: Result[ListDTO] = for {
      list <- listRepo.insertList(listDef)
      items <- addItems(listDef.id, dto.items.getOrElse(Nil).take(MAX_ALLOWED))
    } yield {
      items match {
        case Right(its) => resultSync(new ListDTO(list, its.toList))
        case Left(err) => errorSync(err)
      }
    }
    f recover {
      case e: Throwable => exSync(e, "insertList")
    }
  }

  private[impl] def cloneIfNotOwned(listId: String)(implicit authData: AuthData): Result[ListWithItems] =
    listRepo.getListDefById(listId) flatMap {
      case Some(ListWithItems(list, items)) =>
        val f: Result[ListWithItems] = if (list.userId != userId) {
          val newList = list.copy(id = Gen.guid, userId = userId)
          listRepo.insertList(newList) flatMap { nl =>
            val newItems = items.map(_.copy(listDefId = nl.id))
            listRepo.replaceListItems(newList.id, newItems) map (_ => Right(ListWithItems(nl, newItems)))
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

  private[services] def addItems(listId: String, items: Seq[ListItemDTO])(implicit a: AuthData): Result[Seq[ListItemDTO]] =
    if (items.isEmpty) {
      result(Nil)
    } else {
      val models = items
        .filter(_.productId.isEmpty)
        .map(p =>
          Product(id = Gen.guid, userId = userId, name = p.description.getOrElse(""), tags = p.description.getOrElse("").toLowerCase))

      val f: Result[Seq[ListItemDTO]] = for {
        _ <- productRepo.insertProducts(models)
        existing <- listRepo.getListProductsByList(listId)
        items <- if (existing.size > MAX_ALLOWED) {
          error(ErrorMessages.TOO_MANY_ITEMS)
        } else {
          val all = models.map(p => ListDefProduct(listDefId = listId, productId = p.id, description = p.description)) ++
            items.filter(_.productId.isDefined).map(p => p.toModel(listId, p.productId.get))
          listRepo.replaceListItems(listId, combineListProducts(all, existing)).map(_.map(new ListItemDTO(_))).map(resultSync)
        }
      } yield {
        items match {
          case Left(err) => errorSync(400, ErrorMessages.SERVER_ERROR)
          case Right(its) => resultSync(its)
        }
      }

      f recover {
        case e: Throwable =>
          exSync(e)
      }
    }

  override def getUserLists(uid: String, offset: Int, count: Int)(implicit authData: AuthData): Result[ListsDTO] = {
    listRepo.getUserLists(userId, offset, count) map {
      case (seq, total) => resultSync(ListsDTO(items = seq.map(
        l => new ListDTO(l.list, l.items)), total = total))
    } recover {
      case e: Throwable => exSync(e, "getUserLists")
    }
  }

  override def updateLists(lists: ListsDTO)
    (implicit authData: AuthData): Result[ListsDTO] = Future.sequence(lists.items map updateList) map {
    seqEitherWithFold(_)(Seq.empty[ListDTO])(_ :+ _).right map (ListsDTO(_))
  }

  override def insertLists(lists: ListsDTO)
    (implicit authData: AuthData): Result[ListsDTO] = Future.sequence(lists.items map insertList) map {
    seqEitherWithFold(_)(Seq.empty[ListDTO])(_ :+ _).right map (ListsDTO(_))
  }

  override def updateList(dto: ListDTO)(implicit authData: AuthData): Result[ListDTO] = dto.id match {
    case None => error(ErrorMessages.EMPTY_ID)
    case Some(id) =>
      checkUser(valid(id)) {
        cloneIfNotOwned(id).flatMap {
          case Right(listDef) =>
            addItems(listDef.list.id, dto.items.getOrElse(Nil)).flatMap {
              case Right(its) =>
                listRepo.updateList(dto.toModel(listDef.list.id, userId)).map { p =>
                  resultSync(new ListDTO(p, its.toList))
                }
              case Left(err) => error(err)
            }
          case Left(err) => error(err)
        }
      } recover {
        case e: Throwable => exSync(e, "updateList")
      }
  }

  private def valid(listId: String)(implicit authData: AuthData): Future[Boolean] = {
    listRepo.getListUsers(listId) map {
      lst => lst.contains(userId)
    }
  }

  override def deleteList(listId: String)(implicit authData: AuthData): Result[BooleanDTO] =
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
