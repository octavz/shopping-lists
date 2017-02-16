package org.shopping.dto

import org.shopping.models._

case class ProductDTO(id: Option[String], name: String, tags: String, description: Option[String] = None,
                      clientTag: Option[String] = None, status: Short) {

  def this(model: Product) =
    this(id = Some(model.id), name = model.name, tags = model.tags, description = model.description, clientTag = Option(model.clientTag), status = model.status)

  def toModel(userId: String, id: String): Product =
    Product(id = id, userId = userId, name = name, tags = tags, description = description, clientTag = clientTag.getOrElse(""), status = status)

}

case class ProductsDTO(items: Seq[ProductDTO], offset: Int, count: Int, total: Int)

case class SyncProductsDTO(items: Seq[ProductDTO], time: Long, total: Int)

case class SuppliersDTO(items: Seq[SupplierDTO])

case class SupplierDTO(id: Option[String], name: String, description: Option[String], clientTag: Option[String] = None) {
  def this(model: Supplier) =
    this(id = Some(model.id), name = model.name, description = model.description, clientTag = Option(model.clientTag))

  def toModel(id: String): Supplier =
    Supplier(id = id, name = name, description = description, clientTag = clientTag.getOrElse(""))
}

case class ProductPriceDTO(productId: String, supplierId: String, price: BigDecimal) {
  def this(model: ProductPrice) = this(model.productId, model.supplierId, model.price)

  def toModel(userId: String) =
    ProductPrice(userId = userId, productId = productId, supplierId = supplierId, price = price)
}
