package org.shopping.controllers

import javax.inject.Inject

import controllers.Assets
import org.shopping.dto._
import org.shopping.services.MainService
import org.shopping.util.ErrorMessages
import play.api.libs.json.JsResultException
import play.api.mvc.Action

import scala.concurrent.ExecutionContext.Implicits.global

class MainController @Inject()(mainService: MainService) extends BaseController(mainService) {

  def build = Action { _ => Ok("build")}

  def indexUser(user: String) = Action.async {
    implicit request =>
      Assets.at("/public/client", "index.html")(request)
  }

  def indexProject(user: String, project: String) = Action.async {
    implicit request =>
      Assets.at("/public/client", "index.html")(request)
    //Ok(views.html.users.index.render())
  }

  def options(path: String) = Action {
    Ok("")
  }

  def syncData() = Action.async {
    implicit request =>
      request.body.asJson.map {
        json => try {
          authorize {
            implicit authInfo =>
              try {
                mainService.sync(json.as[SyncDTO]) map (response(_))
              } catch {
                case je: JsResultException => asyncBadRequest(je.errors.mkString(","))
                case e: Throwable => asyncBadRequest(e)
              }
          }
        } catch {
          case e: Throwable => asyncBadRequest(e)
        }
      }.getOrElse(asyncBadRequest(ErrorMessages.BAD_JSON))
  }

}
