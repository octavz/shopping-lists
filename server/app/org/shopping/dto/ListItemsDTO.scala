package org.shopping.dto

case class ListMetadata(listId: String,  boughtItems: Seq[String])

case class ListItemsDTO(items: Seq[ListItemDTO], meta: Option[ListMetadata])
