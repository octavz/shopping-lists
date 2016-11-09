package org.shopping.dto

case class SyncDTO(userData: UserDTO, listsMeta: ListsDTO, lists: Seq[ListItemsDTO], products: Seq[ProductDTO], prices: Seq[ProductPriceDTO] ) {

}
