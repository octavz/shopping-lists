package org.shopping.dto

import org.shopping.db._
import org.shopping.models.ListDefProduct
import org.shopping.util.Time

case class ListItemDTO(productId: String, quantity: Int, description: Option[String]) {

  def this(model: ListDefProduct) = this(
    productId = model.productId,
    quantity = model.quantity,
    description = model.description
  )

  def toModel(listId: String) = {
    val n = Time.now()
    ListDefProduct(
      listDefId = listId,
      productId = productId,
      description = description,
      quantity = quantity,
      created = n,
      updated = n)
  }
}
