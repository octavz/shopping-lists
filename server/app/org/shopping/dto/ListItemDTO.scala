package org.shopping.dto

import org.shopping.models.ListDefProduct
import org.shopping.util.Time

case class ListItemDTO(productId: Option[String], quantity: Int, description: Option[String]) {

  def this(model: ListDefProduct) = this(
    productId = Some(model.productId),
    quantity = model.quantity,
    description = model.description
  )

  def toModel(listId: String, productId: String) = {
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
