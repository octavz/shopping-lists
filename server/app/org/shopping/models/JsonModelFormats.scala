package org.shopping.models

import play.api.libs.json._

trait JsonModelFormats extends ConstraintReads {
  implicit val fmtProduct = Json.format[Product]
  implicit val fmtUser = Json.format[User]
  implicit val fmtSession = Json.format[UserSession]
}

object JsonModelFormats extends JsonModelFormats

