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

  override def setAuth(value: AuthData) = {
    super.setAuth(value)
    userService.setAuth(value)
    listService.setAuth(value)
    productService.setAuth(value)
  }

  override def sync(data: SyncDTO): Result[SyncDTO] = {
    for {
      userData <- data.userData.fold(userService.getUserById(userId))(userService.updateUser)
      meta <- data.listsMeta.fold(listService.getUserLists(userId, 0, 1000))(listService.updateLists)
      lists <- sequence(data.lists.getOrElse(Nil).map(listService.addListItems)).map(seqEither)
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
