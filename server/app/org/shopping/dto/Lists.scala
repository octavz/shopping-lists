package org.shopping.dto

import org.shopping.models.{ListDef, ListDefProduct}

case class ListMetadata(listId: String,  boughtItems: Seq[String])

case class ListItemsDTO(items: Seq[ListItemDTO], meta: Option[ListMetadata])

case class ListDTO(id: Option[String], name: String, description: Option[String], userId: Option[String], created: Long, status: Option[Int] = Some(0)) {

  def this(model: ListDef) = this(
    id = Some(model.id),
    name = model.name,
    description = model.description,
    userId = Some(model.userId),
    created = model.createdClient)

  def toModel(id: String, userId: String): ListDef = ListDef(id = id,
    userId = userId,
    name = name, description = description, createdClient = created)
}
case class ListItemDTO(productId: Option[String], quantity: Int, description: Option[String], status: Int = 0) {

  def this(model: ListDefProduct) =
    this(productId = Some(model.productId), quantity = model.quantity, description = model.description)

  def toModel(listId: String, productId: String) =
    ListDefProduct(listDefId = listId, productId = productId, description = description, quantity = quantity)

}

case class ListsDTO(items: Seq[ListDTO], total: Int = 1000)
