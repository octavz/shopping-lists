package org.shopping.dal

import org.shopping.models._

trait ProductRepo {

  def insertProduct(model: Product): DAL[Product]

  def insertSupplier(model: Supplier): DAL[Supplier]

  def insertProductPrice(model: ProductPrice): DAL[ProductPrice]

  def updateProduct(model: Product): DAL[Product]

  def getProductById(id: String): DAL[Option[Product]]
}
