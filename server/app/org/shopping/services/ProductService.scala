package org.shopping.services

import org.shopping.dto._

trait ProductService extends BaseService {

  def insertProduct(dto: ProductDTO): Result[ProductDTO]

  def updateProduct(dto: ProductDTO): Result[ProductDTO]

  def deleteProduct(id: String): Result[BooleanDTO]

  def insertSupplier(dto: SupplierDTO): Result[SupplierDTO]

  def getAllSuppliers: Result[SuppliersDTO]

  def insertProductPrice(dto: ProductPriceDTO): Result[ProductPriceDTO]

  def updateProductPrice(dto: ProductPriceDTO): Result[ProductPriceDTO]

}
