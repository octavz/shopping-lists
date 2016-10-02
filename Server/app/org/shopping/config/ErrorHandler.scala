package org.shopping.config

import play.api.http.HttpErrorHandler
import play.api.mvc._
import play.api.mvc.Results._

import scala.concurrent._
import javax.inject.Singleton

import org.shopping.dto.{ErrorDTO, JsonDTOFormats}
import play.api.libs.json.Json;

@Singleton
class ErrorHandler extends HttpErrorHandler with JsonDTOFormats {

  def onClientError(request: RequestHeader, statusCode: Int, message: String) = {
    Future.successful(
      Status(statusCode)(Json.toJson(ErrorDTO(statusCode, message)))
    )
  }

  def onServerError(request: RequestHeader, exception: Throwable) = {
    Future.successful(
      InternalServerError((Json.toJson(ErrorDTO(500, exception.getMessage))))
    )
  }
}
