package org.shopping.services

import org.shopping.dto._

trait ProductService extends BaseService {

  def insertProduct(dto: ProductDTO)(implicit authData: AuthData): Result[ProductDTO]

  def updateProduct(dto: ProductDTO)(implicit authData: AuthData): Result[ProductDTO]

  def deleteProduct(id: String)(implicit authData: AuthData): Result[BooleanDTO]

  def insertSupplier(dto: SupplierDTO)(implicit authData: AuthData): Result[SupplierDTO]

  def getAllSuppliers(implicit authData: AuthData): Result[SuppliersDTO]

  def insertProductPrice(dto: ProductPriceDTO)(implicit authData: AuthData): Result[ProductPriceDTO]

  def updateProductPrice(dto: ProductPriceDTO)(implicit authData: AuthData): Result[ProductPriceDTO]

  def searchProduct(query: String, offset: Int, count: Int)(implicit authData: AuthData): Result[ProductsDTO]

}
