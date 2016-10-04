package org.shopping.config

import javax.inject.Inject

import akka.stream.Materializer
import controllers.Default
import play.api.{Application, Logger}
import play.api.mvc._

import scala.concurrent._
import scala.concurrent.ExecutionContext

class CORSFilter @Inject()(implicit val mat: Materializer, ec: ExecutionContext, app: Application) extends Filter {
  lazy val allowedDomain = app.configuration.getString("cors.allowed.domain")

  def isPreFlight(r: RequestHeader) = r.method.toLowerCase.equals("options") && r.headers.get("Access-Control-Request-Method").nonEmpty

  def apply(f: (RequestHeader) => Future[Result])(request: RequestHeader): Future[Result] = {
    Logger.trace("[cors] filtering request to add cors")
    if (isPreFlight(request)) {
      Logger.trace("[cors] request is preflight")
      Logger.trace(s"[cors] default allowed domain is $allowedDomain")
      Future.successful(Default.Ok.withHeaders(
        "Access-Control-Allow-Origin" -> allowedDomain.orElse(request.headers.get("Origin")).getOrElse(""),
        "Access-Control-Allow-Methods" -> request.headers.get("Access-Control-Request-Method").getOrElse("*"),
        "Access-Control-Allow-Headers" -> request.headers.get("Access-Control-Request-Headers").getOrElse(""),
        "Access-Control-Allow-Credentials" -> "true"))
    } else {
      Logger.trace("[cors] request is normal")
      Logger.trace(s"[cors] default allowed domain is $allowedDomain")
      f(request).map {
        _.withHeaders(
          "Access-Control-Allow-Origin" -> allowedDomain.orElse(request.headers.get("Origin")).getOrElse(""),
          "Access-Control-Allow-Methods" -> request.headers.get("Access-Control-Request-Method").getOrElse("*"),
          "Access-Control-Allow-Headers" -> request.headers.get("Access-Control-Request-Headers").getOrElse(""),
          "Access-Control-Allow-Credentials" -> "true")
      }
    }
  }

}
