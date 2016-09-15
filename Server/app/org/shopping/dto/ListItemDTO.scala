package org.shopping.dto

import org.shopping.db._
import org.shopping.util.Time

case class ListItemDTO(productId: Option[String], description: Option[String]) {

  def this(model: ListItem) = this(
    productId = Some(model.productId),
    description = model.description
    )

  def toModel(listId: String, userId: String) = {
    val n = Time.now
    ListItem(
      listId = listId,
      productId = productId.getOrElse(""),
      userId = userId,
      description = description,
      created = n,
      updated = n)
  }
}
