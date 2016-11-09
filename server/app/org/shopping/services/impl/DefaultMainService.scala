package org.shopping.services.impl

import javax.inject.Inject

import org.shopping.dto.SyncDTO
import org.shopping.services.{ListService, MainService, ProductService, Result, UserService}

class DefaultMainService @Inject()(
  userService: UserService,
  listService: ListService,
  productService: ProductService) extends MainService {

  override def sync(data: SyncDTO): Result[SyncDTO] = {
    ???
  }
}
