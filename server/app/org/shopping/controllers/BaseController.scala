package org.shopping.controllers

import com.google.inject.Inject
import org.shopping.dal.Oauth2DAL
import org.shopping.db.User
import org.shopping.dto.{ErrorDTO, JsonDTOFormats}
import org.shopping.modules.core.BaseService
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._
import scalaoauth2.provider.{AuthInfo, OAuth2Provider, ProtectedResource}

class BaseController(module: BaseService)
  extends Controller
    with JsonDTOFormats
    with OAuth2Provider {
  @Inject var dalAuth: Oauth2DAL = _

  //val authHandler = inject[Oauth2DAL]

  def authorize[A](callback: AuthInfo[User] => Future[Result])(implicit request: play.api.mvc.Request[A]): Future[Result] = {
    val f = ProtectedResource.handleRequest(request, dalAuth) flatMap {
      case Left(e) if e.statusCode == 400 => Future.successful(BadRequest(err(401, e.description)).withHeaders(responseOAuthErrorHeader(e)))
      case Left(e) if e.statusCode == 401 => Future.successful(Unauthorized(err(401, "UNAUTHORIZED")).withHeaders(responseOAuthErrorHeader(e)))
      case Right(authInfo) =>
        module.setAuth(authInfo)
        callback(authInfo)
    }

    f recover {
      case e: Throwable => BadRequest(err(500, e.getMessage))
    }

  }

}
