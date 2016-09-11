package org.shopping.dto

import org.shopping.db._
import org.shopping.util.Time

case class ListItemDTO(productId: Option[String], description: Option[String], listId: Option[String], userId: Option[String]) {

  def this(model: ListItem) = this(
    productId = Some(model.productId),
    description = model.description,
    listId = Some(model.listId),
    userId = Some(model.userId))

  def toModel() = {
    val n = Time.now
    ListItem(
      listId = listId.getOrElse(throw new Exception("Task has no project id")),
      productId = productId.getOrElse(""),
      userId = userId.getOrElse(throw new Exception("User id not set for task.")),
      description = description,
      created = n,
      updated = n)
  }
}
