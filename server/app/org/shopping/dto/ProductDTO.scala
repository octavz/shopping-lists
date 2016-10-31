package org.shopping.dto

import org.shopping.models.Product
import org.shopping.util.Time
import org.shopping.services._

case class ProductDTO(id: Option[String], name: String, description: Option[String] = None) {

  def this(model: Product) =
    this(id = Some(model.id), name = model.name, description = model.description)

  def toModel(userId: String): Product = {
    val n = Time.now()
    Product(
      id = id.getOrGuid,
      userId = userId,
      name = name,
      description = description,
      created = n,
      updated = n)
  }
}
