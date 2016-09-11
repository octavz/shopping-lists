package org.shopping.dto

import play.api.libs.functional.syntax._
import play.api.libs.json._

case class BooleanDTO(value: Boolean)

trait JsonDTOFormats extends BaseFormats with ConstraintReads {

  implicit val stringDTO = Json.format[StringDTO]

  implicit val booleanDTO = Json.format[BooleanDTO]

  implicit val userDTO = Json.format[UserDTO]

  implicit val taskDTO = Json.format[ListItemDTO]

  implicit val tasksDTO = Json.format[ListItemListDTO]

  implicit val registerDTO = (
    (__ \ 'login).format[String](maxLength[String](200) keepAnd email) ~
      (__ \ 'password).format[String](minLength[String](6) keepAnd maxLength[String](50))
    ) (RegisterDTO, unlift(RegisterDTO.unapply))

  implicit val projectDtoRead = (
    (__ \ 'id).readNullable[String](maxLength[String](50)) ~
      (__ \ 'name).read[String](minLength[String](5) keepAnd maxLength[String](200)) ~
      (__ \ 'desc).readNullable[String](maxLength[String](1500)) ~
      (__ \ 'userId).readNullable[String]
    ) (ListDTO)

  implicit val projectDtoWrite = Json.writes[ListDTO]

  implicit val projectListDto = Json.format[ProjectListDTO]

  implicit val loginResponse = Json.writes[LoginResponseDTO]
  implicit val loginRequest = Json.reads[LoginRequestDTO]

}
