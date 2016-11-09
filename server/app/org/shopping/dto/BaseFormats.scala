package org.shopping.dto

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
