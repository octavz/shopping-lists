package org.shopping

package object dto {

  case class BooleanDTO(value: Boolean)

  case class ErrorDTO(errCode: Int, message: String)

  case class StringDTO(value: String)

  case class SyncDTO(userData: Option[UpdateUserDTO],
    listsMeta: Option[ListsDTO],
    products: Option[Seq[ProductDTO]],
    prices: Option[Seq[ProductPriceDTO]])
}
