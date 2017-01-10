package org.shopping.controllers

import javax.inject.Inject

import org.shopping.dto._
import org.shopping.services.MainService
import org.shopping.util.ErrorMessages
import play.api.libs.json.JsResultException
import play.api.mvc.{Action, AnyContent}

import scala.concurrent.ExecutionContext.Implicits.global

class MainController @Inject()(mainService: MainService) extends BaseController(mainService) {
//  val version = "0.1.16"

  def build = Action { _ => Ok("build")}

  def index = Action {
    try {
      val s = getClass.getResourceAsStream("version")
      println(s)
      val version = scala.io.Source.fromInputStream(s).getLines().toList.head
      Ok(version)
    }catch {
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
        json => try {
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
