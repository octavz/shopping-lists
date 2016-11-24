package org.shopping.dto

import org.shopping.models.{ListDef, ListDefProduct}

case class ListMeta(listId: String, boughtItems: Seq[String])

//case class ListItemsDTO(items: Seq[ListItemDTO], meta: Option[ListMeta])

case class ListDTO(id: Option[String],
  name: String,
  description: Option[String],
  userId: Option[String],
  created: Long,
  items: Option[Seq[ListItemDTO]],
  meta: Option[ListMeta] = None,
  status: Option[Int] = Some(0),
  clientTag: Option[String] = None) {

  def this(model: ListDef, items: Seq[ListDefProduct]) = this(
    id = Some(model.id),
    name = model.name,
    description = model.description,
    userId = Some(model.userId),
    created = model.createdClient,
    clientTag = Option(model.clientTag),
    items = Some(items.map(new ListItemDTO(_))))

  def this(model: ListDef, items: List[ListItemDTO]) = this(
    id = Some(model.id),
    name = model.name,
    description = model.description,
    userId = Some(model.userId),
    created = model.createdClient,
    clientTag = Option(model.clientTag),
    items = Some(items))

  def toModel(id: String, userId: String): ListDef = ListDef(id = id, userId = userId, name = name,
    description = description, createdClient = created, status = status.map(_.toShort).getOrElse(0), clientTag = clientTag.getOrElse(""))
}

case class ListItemDTO(productId: Option[String], quantity: Int, description: Option[String], status: Int = 0,
  clientTag: Option[String] = None) {

  def this(model: ListDefProduct) =
    this(productId = Some(model.productId), quantity = model.quantity, description = model.description, clientTag = Option(model.clientTag))

  def toModel(listId: String, productId: String) =
    ListDefProduct(listDefId = listId, productId = productId, description = description, quantity = quantity, clientTag = clientTag.getOrElse(""))

}

case class ListsDTO(items: Seq[ListDTO], total: Int = 1000)
