package org.shopping.services.impl

import javax.inject.Inject

import org.shopping.dto.SyncDTO
import org.shopping.services.{AuthData, ListService, MainService, ProductService, Result, UserService}

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
    userService.updateUser(data.userData)
    ???
  }
}
