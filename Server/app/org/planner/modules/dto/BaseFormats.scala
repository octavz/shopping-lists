package org.planner.modules.dto

import play.api.libs.json._
import org.planner.modules.{ResultError}

trait BaseFormats {
  implicit def genericReqWrite[T](implicit fmt: Writes[T]): Writes[Either[ResultError, T]] =
    new Writes[Either[ResultError, T]] {
      def writes(d: Either[ResultError, T]): JsValue = d match {
        case Right(t) => Json.toJson(t.asInstanceOf[T])
        case Left(err) => Json.obj(
          "errCode" -> err._1,
          "errMessage" -> err._2
        )
      }
    }
}
