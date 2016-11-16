package org.shopping

package object dto {

  case class BooleanDTO(value: Boolean)

  case class ErrorDTO(errCode: Int, message: String)

  case class StringDTO(value: String)

  case class SyncDTO(userData: Option[UserDTO],
    listsMeta: Option[ListsDTO],
    lists: Option[Seq[ListItemsDTO]],
    products: Option[Seq[ProductDTO]],
    prices: Option[Seq[ProductPriceDTO]])
}
