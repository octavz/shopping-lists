package org.shopping.dal

import org.shopping.models._

trait ProductRepo {

  def insertProduct(model: Product): Repo[Product]

  def insertProducts(model: Seq[Product]): Repo[Seq[Product]]

  def insertSupplier(model: Supplier): Repo[Supplier]

  def insertProductPrice(model: ProductPrice): Repo[ProductPrice]

  def updateProduct(model: Product): Repo[Product]

  def getProductById(id: String): Repo[Option[Product]]

  def searchProduct(name: String): Repo[Seq[Product]]

  def getProductPrice(productId: String, supplierId: String): Repo[Option[ProductPrice]]

  def updateProductPrice(productPrice: ProductPrice): Repo[ProductPrice]

  def getAllSuppliers: Repo[Seq[Supplier]]
}
