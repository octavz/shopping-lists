package org.shopping.dto

import play.api.libs.json._
import org.shopping.modules.{ResultError}

trait BaseFormats {
  implicit def genericReqWrite[T](implicit fmt: Writes[T]): Writes[Either[ResultError, T]] =
    new Writes[Either[ResultError, T]] {
      def writes(d: Either[ResultError, T]): JsValue = d match {
        case Right(t) => Json.toJson(t.asInstanceOf[T])
        case Left(err) => Json.obj(
          "errCode" -> err.errorCode,
          "errMessage" -> err.message
        )
      }
    }
}
