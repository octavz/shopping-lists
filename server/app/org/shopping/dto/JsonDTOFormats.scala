package org.shopping.dto

import play.api.libs.functional.syntax._
import play.api.libs.json._

trait BaseFormats {
  implicit def genericReqWrite[T](implicit fmt: Writes[T]): Writes[Either[ErrorDTO, T]] =
    new Writes[Either[ErrorDTO, T]] {
      def writes(d: Either[ErrorDTO, T]): JsValue = d match {
        case Right(t) => Json.toJson(t.asInstanceOf[T])
        case Left(err) => Json.obj(
          "errCode" -> err.errCode,
          "errMessage" -> err.message
        )
      }
    }
}

trait JsonDTOFormats extends BaseFormats with ConstraintReads {
  implicit val stringDTO = Json.format[StringDTO]
  implicit val booleanDTO = Json.format[BooleanDTO]
  implicit val userDTO = Json.format[UserDTO]
  implicit val updateUserDTO = Json.format[UpdateUserDTO]
  implicit val usersDTO = Json.format[UsersDTO]
  implicit val taskDTO = Json.format[ListItemDTO]
  implicit val errorDTO = Json.writes[ErrorDTO]
  implicit val metaResponse = Json.format[ListMeta]
  implicit val registerRequestDTO = (
    (__ \ 'login).format[String](maxLength[String](200) keepAnd email) ~
      (__ \ 'password).format[String](minLength[String](6) keepAnd maxLength[String](50))
    ) (RegisterRequestDTO, unlift(RegisterRequestDTO.unapply))

  implicit val registerResponseDTO = Json.writes[RegisterResponseDTO]
  implicit val list = Json.format[ListDTO]
  implicit val lists = Json.format[ListsDTO]
  implicit val loginResponse = Json.writes[LoginResponseDTO]
  implicit val loginRequest = Json.reads[LoginRequestDTO]
  implicit val productDTO = Json.format[ProductDTO]
  implicit val productPriceDTO = Json.format[ProductPriceDTO]
  implicit val supplierDTO = Json.format[SupplierDTO]
  implicit val suppliersDTO = Json.writes[SuppliersDTO]
  implicit val syncDTO = Json.format[SyncDTO]
  implicit val productsDTO = Json.format[ProductsDTO]
  implicit val syncProductsDTO = Json.format[SyncProductsDTO]
}

object JsonDTOFormats extends JsonDTOFormats
