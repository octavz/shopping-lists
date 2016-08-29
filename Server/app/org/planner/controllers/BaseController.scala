package org.planner.controllers

import com.google.inject.Inject
import org.planner.dal.Oauth2DAL
import org.planner.db.User
import org.planner.modules.core.BaseModule
import org.planner.modules.dto.JsonDTOFormats
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._
import scalaoauth2.provider.{AuthInfo, OAuth2Provider, ProtectedResource}

class BaseController(module: BaseModule)
  extends Controller
  with JsonDTOFormats
  with OAuth2Provider {
  @Inject var dalAuth: Oauth2DAL = _

  //val authHandler = inject[Oauth2DAL]

  def authorize[A](callback: AuthInfo[User] => Future[Result])(implicit request: play.api.mvc.Request[A]): Future[Result] = {
    val f = ProtectedResource.handleRequest(request, dalAuth) flatMap {
      case Left(e) if e.statusCode == 400 => Future.successful(BadRequest.withHeaders(responseOAuthErrorHeader(e)))
      case Left(e) if e.statusCode == 401 => Future.successful(Unauthorized.withHeaders(responseOAuthErrorHeader(e)))
      case Right(authInfo) =>
        module.setAuth(authInfo)
        callback(authInfo)
    }

    f recover {
      case e: Throwable =>
        BadRequest.withHeaders(("500", e.getMessage))
    }

  }

}
