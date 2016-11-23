package org.shopping.controllers

import com.google.inject.Inject
import org.shopping.dto._
import org.shopping.services.ListService
import org.shopping.util.ErrorMessages
import play.api.libs.json.JsResultException
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global

class ListController @Inject()(listService: ListService) extends BaseController(listService) {

  def insertList() = Action.async {
    implicit request =>
      request.body.asJson.map {
        json => try {
          authorize {
            implicit authInfo =>
              try {
                val dto = json.as[ListDTO].copy(userId = Some(authInfo.user.id))
                listService.insertList(dto) map (response(_))
              } catch {
                case je: JsResultException => asyncBadRequest(je.errors.mkString(","))
              }
          }
        } catch {
          case e: Throwable => asyncBadRequest(e)
        }
      }.getOrElse(asyncBadRequest(new Exception(ErrorMessages.BAD_JSON)))
  }

  def updateList(id: String) = Action.async {
    implicit request =>
      request.body.asJson.map {
        json => try {
          authorize {
            implicit authInfo =>
              try {
                val dto = json.as[ListDTO].copy(id = Some(id), userId = Some(authInfo.user.id))
                listService.updateList(dto) map (response(_))
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

  def getUserLists(userId: String, offset: Int, count: Int) = Action.async {
    implicit request =>
      try {
        authorize {
          implicit authInfo =>
            listService.getUserLists(userId, offset, count) map (response(_))
        }
      } catch {
        case e: Throwable => asyncBadRequest(e)
      }

  }

  def deleteList(listId: String) = Action.async {
    implicit request =>
      authorize {
        implicit authInfo =>
          listService.deleteList(listId) map (response(_))
      }
  }

}
