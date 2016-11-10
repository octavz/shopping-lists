package org.shopping

package object dto {

  case class BooleanDTO(value: Boolean)

  case class ErrorDTO(errCode: Int, message: String)
  case class StringDTO(value: String)
  case class SyncDTO(userData: UserDTO,
    listsMeta: ListsDTO,
    lists: Seq[ListItemsDTO],
    products: Seq[ProductDTO],
    prices: Seq[ProductPriceDTO])

}
