package org.shopping.repo

import org.shopping.models._

trait ProductRepo {

  def insertProduct(model: Product): Repo[Product]

  def insertProducts(model: Seq[Product]): Repo[Seq[Product]]

  def insertSupplier(model: Supplier): Repo[Supplier]

  def insertProductPrice(model: ProductPrice): Repo[ProductPrice]

  def updateProduct(model: Product): Repo[Product]

  def getProductById(id: String): Repo[Option[Product]]

  def searchProduct(query: String, offset: Int, count: Int): Repo[(Seq[Product], Int)]

  def getModifiedProductsSince(since: Long, userId: Option[String]): Repo[(Seq[Product], Int)]

  def getProductPrice(productId: String, supplierId: String): Repo[Option[ProductPrice]]

  def updateProductPrice(productPrice: ProductPrice): Repo[ProductPrice]

  def getAllSuppliers: Repo[Seq[Supplier]]
}
