package org.shopping.services.impl

import javax.inject.Inject

import org.shopping.dto.{ListsDTO, SyncDTO}
import org.shopping.services.{AuthData, ListService, MainService, ProductService, Result, UserService}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

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

  def seqEither[S, T](data: Seq[Either[S, T]]): Either[S, Seq[T]] =
    data.partition(_.isRight) match {
      case (l, Nil) => Right(l.map(_.right.toOption.get))
      case (_, h :: _) => Left(h.left.toOption.get)
    }

  override def sync(data: SyncDTO): Result[SyncDTO] = {
    for {
      userData <- data.userData.fold(userService.getUserById(userId))(userService.updateUser)
      meta <- Future.sequence(data.listsMeta.items.map(listService.updateList)).map(seqEither)
      lists <- Future.sequence(data.lists.map(listService.addListItems)).map(seqEither)
      products <- Future.sequence(data.products.map(productService.insertProduct)).map(seqEither)
      prices <- Future.sequence(data.prices.map(productService.insertProductPrice)).map(seqEither)
    } yield for {
      rUserData <- userData.right
      rMeta <- meta.right
      rLists <- lists.right
      rProducts <- products.right
      rPrices <- prices.right
    } yield SyncDTO(
      userData = Some(rUserData),
      listsMeta = ListsDTO(rMeta, 0),
      lists = rLists,
      products = rProducts,
      prices = rPrices
    )
  }

}
