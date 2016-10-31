package org.shopping.services

import org.shopping.dto.{BooleanDTO, ProductDTO}

trait ProductService extends BaseService {

  def insertProduct(dto: ProductDTO): Result[ProductDTO]

  def updateProduct(dto: ProductDTO): Result[ProductDTO]

  def deleteProduct(dto: String): Result[BooleanDTO]

}
