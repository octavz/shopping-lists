package org.shopping.dal

import org.shopping.models._

trait ProductRepo {

  def insertProduct(model: Product): DAL[Product]

  def insertProducts(model: Seq[Product]): DAL[Seq[Product]]

  def insertSupplier(model: Supplier): DAL[Supplier]

  def insertProductPrice(model: ProductPrice): DAL[ProductPrice]

  def updateProduct(model: Product): DAL[Product]

  def getProductById(id: String): DAL[Option[Product]]

  def searchProduct(name: String): DAL[Seq[Product]]

  def getProductPrice(productId: String, supplierId: String): DAL[Option[ProductPrice]]

  def updateProductPrice(productPrice: ProductPrice): DAL[ProductPrice]

  def getAllSuppliers: DAL[Seq[Supplier]]
}
