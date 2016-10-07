package org.shopping.controllers


import com.google.inject.Inject
import org.shopping.modules.core.ListService
import org.shopping.dto._
import play.api.libs.json.JsResultException
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global

class ListController @Inject()(listModule: ListService) extends BaseController(listModule) {

  def insertList() = Action.async {
    implicit request =>
      request.body.asJson.map {
        json => try {
          authorize {
            implicit authInfo =>
              try {
                val dto = json.as[ListDTO].copy(userId = Some(authInfo.user.id))
                listModule.insertList(dto) map (responseOk(_))
              } catch {
                case je: JsResultException => asyncBadRequest(je.errors.mkString(","))
              }
          }
        } catch {
          case e: Throwable => asyncBadRequest(e)
        }
      }.getOrElse(asyncBadRequest(new Exception("Bad Json")))
  }

  def updateList(id: String) = Action.async {
    implicit request =>
      request.body.asJson.map {
        json => try {
          authorize {
            implicit authInfo =>
              try {
                val dto = json.as[ListDTO].copy(id = Some(id), userId = Some(authInfo.user.id))
                listModule.updateList(dto) map (responseOk(_))
              } catch {
                case je: JsResultException => asyncBadRequest(je.errors.mkString(","))
                case e: Throwable => asyncBadRequest(e)
              }
          }
        } catch {
          case e: Throwable => asyncBadRequest(e)
        }
      }.getOrElse(asyncBadRequest(new Exception("Bad Json")))
  }

  def getUserLists(userId: String, offset: Int, count: Int) =
    Action.async {
      implicit request =>
        try {
          authorize {
            implicit authInfo =>
              listModule.getUserLists(userId, offset, count) map (responseOk(_))
          }
        } catch {
          case e: Throwable => asyncBadRequest(e)
        }

    }

  def addListItems(listId: String) = Action.async {
    implicit request =>
      request.body.asJson.map {
        json => try {
          authorize {
            implicit authInfo =>
              try {
                val dto = json.as[ListItemsDTO]
                listModule.addListItems(listId, dto) map (responseOk(_))
              } catch {
                case je: JsResultException => asyncBadRequest(je.errors.mkString(","))
                case e: Throwable => asyncBadRequest(e)
              }
          }
        } catch {
          case e: Throwable => asyncBadRequest(e)
        }
      }.getOrElse(asyncBadRequest(new Exception("Bad Json")))
  }

  def getListItems(listId: String) = Action.async {
    implicit request =>
      try {
        authorize {
          implicit authInfo =>
            listModule.getListItems(listId) map (responseOk(_))
        }
      } catch {
        case e: Throwable => asyncBadRequest(e)
      }
  }

  def deleteList(listId: String) =
    Action.async {
      implicit request =>
        authorize {
          implicit authInfo =>
            listModule.deleteList(listId) map (responseOk(_))
        }
    }


}
