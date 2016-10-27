package org.shopping

import org.shopping.dto.ErrorDTO
import play.api.Logger
import play.api.libs.json.{JsValue, Json, Writes}
import play.api.mvc.Results._

import scala.concurrent._

package object controllers {

  import org.shopping.dto.JsonDTOFormats._

  def err(code: Int, message: String)(implicit tjs: Writes[ErrorDTO]): JsValue = {
    Logger.error(message)
    Json.toJson(ErrorDTO(code, message))
  }

  def err(message: String)(implicit tjs: Writes[ErrorDTO]): JsValue = err(400, message)

  def asyncBadRequest(ex: Throwable)(implicit tjs: Writes[ErrorDTO]) = {
    Logger.error(ex.getMessage)
    Future.successful(BadRequest(err(500, ex.getMessage)))
  }

  def asyncBadRequest(msg: String)(implicit tjs: Writes[ErrorDTO]) = {
    Logger.error(msg)
    Future.successful(BadRequest(err(500, msg)))
  }


  def response[T <: AnyRef](a: Either[ErrorDTO, T])(implicit tjs: Writes[T]) = {
    a match {
      case Left(err) => InternalServerError(Json.toJson(err))
      case Right(o) => Ok(Json.toJson(o)(tjs))
    }
  }

}
