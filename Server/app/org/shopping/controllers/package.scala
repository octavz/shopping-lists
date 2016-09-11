package org.shopping

import play.api.Logger
import play.api.libs.json.{Json, Writes}
import play.api.mvc.Results._

import scala.concurrent._

package object controllers {

  def asyncBadRequest(ex: Throwable) = {
    Logger.error(ex.getMessage)
    Future.successful(BadRequest( s"""{"errCode":0, "errMessage":${ex.getMessage}}"""))
  }

  def asyncBadRequest(msg: String) = {
    Logger.error(msg)
    Future.successful(BadRequest( s"""{"errCode":0, "errMessage":${msg}}"""))
  }

  def responseOk[T <: AnyRef](a: T)(implicit m: Writes[T]) = Ok(Json.toJson(a))

}
