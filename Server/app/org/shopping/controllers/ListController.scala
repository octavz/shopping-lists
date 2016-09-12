package org.shopping.controllers


import com.google.inject.Inject
import org.shopping.modules.core.ListModule
import org.shopping.dto._
import play.api.libs.json.JsResultException
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global

class ListController @Inject()(listModule: ListModule) extends BaseController(listModule) {

  def insertList() = Action.async {
    implicit request =>
      request.body.asJson.map {
        json => try {
          authorize {
            implicit authInfo =>
              try {
                val dto = json.as[ListDTO]
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

  def getUserLists(id: String, offset: Int, count: Int) =
    Action.async {
      implicit request =>
        try {
          authorize {
            implicit authInfo =>
              listModule.getUserLists(id, offset, count) map (responseOk(_))
          }
        } catch {
          case e: Throwable => asyncBadRequest(e)
        }

    }

  def insertTask(listId: String) =
    Action.async {
      implicit request =>
        request.body.asJson.map {
          json => try {
            authorize {
              implicit authInfo =>
                try {
                  val dto = json.as[ListItemDTO].copy(listId = Some(listId), userId = Some(authInfo.user.id))
                  listModule.insertListItem(dto) map (responseOk(_))
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

  def getTasks(listId: String,
               offset: Int,
               count: Int) =
    Action.async {
      implicit request =>
        try {
          authorize {
            implicit authInfo =>
              listModule.getListItems(listId, offset, count) map (responseOk(_))
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
