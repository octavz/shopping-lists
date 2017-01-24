package org.shopping.controllers

import javax.inject.Inject

import org.shopping.dto._
import org.shopping.services.MainService
import org.shopping.util.ErrorMessages
import play.api.Environment
import play.api.libs.json.JsResultException
import play.api.mvc.{Action, AnyContent}

import scala.concurrent.ExecutionContext.Implicits.global

class MainController @Inject()(mainService: MainService, env: Environment) extends BaseController(mainService) {

  def build = Action { _ => Ok("build") }

  def index = Action {
    try {
      val version = Option(env.classLoader.getResourceAsStream("version")) match {
        case Some(s) =>
          scala.io.Source.fromInputStream(s).getLines().toList.head
        case _ =>
          "No version file found, please add a version file to conf folder."
      }
      Ok(version)
    } catch {
      case t: Throwable =>
        t.printStackTrace()
        InternalServerError
    }
  }

  def options(path: String) = Action {
    Ok("")
  }

  def syncData(): Action[AnyContent] = Action.async {
    implicit request =>
      request.body.asJson.map {
        json =>
          try {
            authorize {
              implicit authInfo =>
                try {
                  mainService.sync(json.as[SyncDTO]) map response[SyncDTO]
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
