package org.shopping.services.impl

import javax.inject.Inject

import org.shopping.dto._
import org.shopping.services.{AuthData, ListService, MainService, ProductService, Result, UserService, _}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.Future._

class DefaultMainService @Inject()(
  userService: UserService,
  listService: ListService,
  productService: ProductService) extends MainService {

  override def setAuth(value: AuthData) = {
    super.setAuth(value)
    userService.setAuth(value)
    listService.setAuth(value)
    productService.setAuth(value)
  }

  private def getItems(lists: Either[ErrorDTO, ListsDTO]): Result[Seq[ListItemsDTO]] =
    lists.fold(error, { l =>
      sequence(l.items.map(_.id.get).map(listService.getListItems)) map {
        lst => resultSync(
          lst.flatMap {
            case Left(err) => None
            case Right(li) => if(li.items.isEmpty) None else Some(li)
          })
      }
    })

  override def sync(data: SyncDTO): Result[SyncDTO] = {
    for {
      userData <- data.userData.fold(userService.getUserById(userId))(userService.updateUser)
      meta <- data.listsMeta.fold(listService.getUserLists(userId, 0, 1000))(listService.updateLists)
      lists <- getItems(meta)
      products <- sequence(data.products.getOrElse(Nil).map(productService.insertProduct)).map(seqEither)
      prices <- sequence(data.prices.getOrElse(Nil).map(productService.insertProductPrice)).map(seqEither)
    } yield for {
      rUserData <- userData.right
      rMeta <- meta.right
      rLists <- lists.right
      rProducts <- products.right
      rPrices <- prices.right
    } yield SyncDTO(
      userData = Some(rUserData),
      listsMeta = Some(rMeta),
      lists = Some(rLists),
      products = Some(rProducts),
      prices = Some(rPrices)
    )
  }

}
