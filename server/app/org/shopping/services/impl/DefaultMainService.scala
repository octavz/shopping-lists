package org.shopping.services.impl

import javax.inject.Inject

import org.shopping.dto._
import org.shopping.services.{AuthData, ListService, MainService, ProductService, Result, UserService, _}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future._

class DefaultMainService @Inject()(
                                    userService: UserService,
                                    listService: ListService,
                                    productService: ProductService) extends MainService {

  private def userLists(implicit authData: AuthData) = listService.getUserLists(userId, 0, 1000)

  override def sync(data: SyncDTO)(implicit authData: AuthData): Result[SyncDTO] = try {
    for {
      userData <- data.userData.fold(userService.getUserById(userId))(userService.updateUser)
      meta <- data.listsMeta.fold(userLists) { l =>
        val (toUpdate, toInsert) = l.items.partition(_.id.isDefined)
        val fUpdate = listService.updateLists(ListsDTO(toUpdate))
        val fInsert = listService.insertLists(ListsDTO(toInsert))
        for {
          _ <- fUpdate
          _ <- fInsert
          r <- userLists
        } yield r
      }
      products <- sequence(data.products.getOrElse(Nil).map(productService.insertProduct)).map(seqEither)
      prices <- sequence(data.prices.getOrElse(Nil).map(productService.insertProductPrice)).map(seqEither)
    } yield for {
      rUserData <- userData.right
      rMeta <- meta.right
      rProducts <- products.right
      rPrices <- prices.right
    } yield SyncDTO(
      userData = Some(UpdateUserDTO(id = Some(rUserData.id), login = Some(rUserData.login), nick = Some(rUserData.nick), password = None)),
      listsMeta = Some(rMeta),
      products = Some(rProducts),
      prices = Some(rPrices)
    )
  } catch {
    case e: Throwable => ex(e)
  }

}
