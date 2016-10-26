package org.shopping.dto

case class ListMetadata(markedProducts: List[String])

case class ListItemsDTO(items: Seq[ListItemDTO], meta: ListMetadata)
