package org.shopping.controllers

import com.google.inject.Inject
import org.shopping.repo.Oauth2Repo
import org.shopping.dto.{ErrorDTO, JsonDTOFormats}
import org.shopping.models.User
import org.shopping.services.BaseService
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._
import scalaoauth2.provider.{AuthInfo, OAuth2Provider, ProtectedResource}

class BaseController(service: BaseService)
  extends Controller
    with JsonDTOFormats
    with OAuth2Provider {
  @Inject var authRepo: Oauth2Repo = _

  def authorize[A](callback: AuthInfo[User] => Future[Result])(implicit request: play.api.mvc.Request[A]): Future[Result] = {
    val f = ProtectedResource.handleRequest(request, authRepo) flatMap {
      case Left(e) if e.statusCode == 400 => Future.successful(BadRequest(err(400, e.description)).withHeaders(responseOAuthErrorHeader(e)))
      case Left(e) if e.statusCode == 401 => Future.successful(Unauthorized(err(401, "UNAUTHORIZED")).withHeaders(responseOAuthErrorHeader(e)))
      case Right(authInfo) =>
        callback(authInfo)
    }

    f recover {
      case e: Throwable => BadRequest(err(500, e.getMessage))
    }

  }

}
