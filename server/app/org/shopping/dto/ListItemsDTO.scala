package org.shopping.dto

case class ListMetadata(listId: String)

case class ListItemsDTO(items: Seq[ListItemDTO], meta: ListMetadata)
