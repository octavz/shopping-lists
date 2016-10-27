package org.shopping.dto

import org.shopping.db._
import org.shopping.util.Time

case class ListItemDTO(productId: Option[String], quantity: Int, description: Option[String]) {

  def this(model: ListDefProduct) = this(
    productId = Some(model.productId),
    quantity = model.quantity,
    description = model.description
  )

  def toModel(listId: String, userId: String) = {
    val n = Time.now()
    ListDefProduct(
      listDefId = listId,
      productId = productId.getOrElse(""),
      userId = userId,
      description = description,
      quantity = quantity,
      created = n,
      updated = n)
  }
}
