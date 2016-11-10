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
  implicit val usersDTO = Json.format[UsersDTO]
  implicit val taskDTO = Json.format[ListItemDTO]
  implicit val errorDTO = Json.writes[ErrorDTO]
  implicit val metaResponse = Json.format[ListMetadata]
  implicit val tasksDTO = Json.format[ListItemsDTO]
  implicit val registerRequestDTO = (
    (__ \ 'login).format[String](maxLength[String](200) keepAnd email) ~
      (__ \ 'password).format[String](minLength[String](6) keepAnd maxLength[String](50))
    ) (RegisterRequestDTO, unlift(RegisterRequestDTO.unapply))

  implicit val registerResponseDTO = Json.writes[RegisterResponseDTO]

  implicit val listRead = (
    (__ \ 'id).readNullable[String](maxLength[String](50)) ~
      (__ \ 'name).read[String](minLength[String](1) keepAnd maxLength[String](200)) ~
      (__ \ 'description).readNullable[String](maxLength[String](1500)) ~
      (__ \ 'userId).readNullable[String] ~
      (__ \ 'created).read[Long] ~
      (__ \ 'status).readNullable[Int]
    ) (ListDTO)

  implicit val listWrite = Json.writes[ListDTO]
  implicit val lists = Json.format[ListsDTO]
  implicit val loginResponse = Json.writes[LoginResponseDTO]
  implicit val loginRequest = Json.reads[LoginRequestDTO]
  implicit val productDTO = Json.format[ProductDTO]
  implicit val productPriceDTO = Json.format[ProductPriceDTO]
  implicit val supplierDTO = Json.format[SupplierDTO]
  implicit val suppliersDTO = Json.writes[SuppliersDTO]
  implicit val syncDTO = Json.format[SyncDTO]
}

object JsonDTOFormats extends JsonDTOFormats
