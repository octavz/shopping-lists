package org.shopping.dto

import org.shopping.models._
import org.shopping.util.Time

case class ProductDTO(id: Option[String], name: String, description: Option[String] = None) {

  def this(model: Product) =
    this(id = Some(model.id), name = model.name, description = model.description)

  def toModel(userId: String, id: String): Product = {
    val n = Time.now()
    Product(id = id, userId = userId, name = name, description = description, created = n, updated = n)
  }
}

case class SuppliersDTO(items: Seq[SupplierDTO])

case class SupplierDTO(id: Option[String], name: String, description: Option[String]) {
  def this(model: Supplier) = this(id = Some(model.id), name = model.name, description = model.description)

  def toModel(id: String): Supplier =
    Supplier(id = id, name = name, description = description)
}

case class ProductPriceDTO(productId: String, supplierId: String, price: BigDecimal) {
  def this(model: ProductPrice) = this(model.productId, model.supplierId, model.price)

  def toModel(userId: String) = ProductPrice(userId = userId, productId = productId, supplierId = supplierId, price = price)
}
