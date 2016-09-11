package org.shopping.controllers


import com.google.inject.Inject
import org.shopping.modules.core.ListModule
import org.shopping.dto._
import play.api.libs.json.JsResultException
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global

class ProjectController @Inject()(projectModule: ListModule) extends BaseController(projectModule) {

  def insertProject() = Action.async {
    implicit request =>
      request.body.asJson.map {
        json => try {
          authorize {
            implicit authInfo =>
              try {
                val dto = json.as[ListDTO]
                projectModule.insertList(dto) map (responseOk(_))
              } catch {
                case je: JsResultException => asyncBadRequest(je.errors.mkString(","))
              }
          }
        } catch {
          case e: Throwable => asyncBadRequest(e)
        }
      }.getOrElse(asyncBadRequest(new Exception("Bad Json")))
  }

  def updateProject(id: String) = Action.async {
    implicit request =>
      request.body.asJson.map {
        json => try {
          authorize {
            implicit authInfo =>
              try {
                val dto = json.as[ListDTO].copy(id = Some(id), userId = Some(authInfo.user.id))
                projectModule.updateList(dto) map (responseOk(_))
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

  def getUserProjects(id: String, offset: Int, count: Int) =
    Action.async {
      implicit request =>
        try {
          authorize {
            implicit authInfo =>
              projectModule.getUserLists(id, offset, count) map (responseOk(_))
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
                  projectModule.insertListItem(dto) map (responseOk(_))
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

  def getTasks(projectId: String,
               offset: Int,
               count: Int) =
    Action.async {
      implicit request =>
        try {
          authorize {
            implicit authInfo =>
              projectModule.getListItems(projectId, offset, count) map (responseOk(_))
          }
        } catch {
          case e: Throwable => asyncBadRequest(e)
        }
    }

  def deleteProject(projectId: String) =
    Action.async {
      implicit request =>
        authorize {
          implicit authInfo =>
            projectModule.deleteList(projectId) map (responseOk(_))
        }
    }


}
