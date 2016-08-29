package org.planner.controllers

import javax.ws.rs.{PathParam, QueryParam}

import com.google.inject.Inject
import com.wordnik.swagger.annotations._
import org.planner.modules.core.ProjectModule
import org.planner.modules.dto._
import play.api.libs.json.JsResultException
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global

@Api(value = "/api/project", description = "Project operations")
class ProjectController @Inject()(projectModule: ProjectModule) extends BaseController(projectModule) {

  @ApiOperation(value = "Create project", notes = "Create project", response = classOf[ProjectDTO], httpMethod = "POST", nickname = "createProject")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(value = "The new project to be added", required = true, dataType = "ProjectDTO", paramType = "body"),
    new ApiImplicitParam(name = "Authorization", value = "authorization", defaultValue = "OAuth token", required = true, dataType = "string", paramType = "header")
  ))
  def insertProject() = Action.async {
    implicit request =>
      request.body.asJson.map {
        json => try {
          authorize {
            implicit authInfo =>
              try {
                val dto = json.as[ProjectDTO]
                projectModule.insertProject(dto) map (responseOk(_))
              } catch {
                case je: JsResultException => asyncBadRequest(je.errors.mkString(","))
              }
          }
        } catch {
          case e: Throwable => asyncBadRequest(e)
        }
      }.getOrElse(asyncBadRequest(new Exception("Bad Json")))
  }

  @ApiOperation(value = "Update project", notes = "update project", response = classOf[ProjectDTO], httpMethod = "PUT", nickname = "updateProject")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(value = "The project to be updated", required = true, dataType = "ProjectDTO", paramType = "body"),
    new ApiImplicitParam(name = "Authorization", value = "authorization", defaultValue = "OAuth token", required = true, dataType = "string", paramType = "header")
  ))
  def updateProject(
                     @ApiParam(value = "project id", required = true) @PathParam(value = "id") id: String
                     ) = Action.async {
    implicit request =>
      request.body.asJson.map {
        json => try {
          authorize {
            implicit authInfo =>
              try {
                val dto = json.as[ProjectDTO].copy(id = Some(id), userId = authInfo.user.userId)
                projectModule.updateProject(dto) map (responseOk(_))
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

  @ApiOperation(value = "Get user projects", notes = "Get user projects", response = classOf[ProjectListDTO], httpMethod = "GET", nickname = "getUserProjects")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "Authorization", value = "authorization", defaultValue = "OAuth token", required = true, dataType = "string", paramType = "header")
  ))
  def getUserProjects(
                       @ApiParam(value = "user id", required = true) @PathParam(value = "id") id: String,
                       @ApiParam(value = "offset", required = true, defaultValue = "0") @QueryParam(value = "offset") offset: Int,
                       @ApiParam(value = "count", required = true, defaultValue = "100") @QueryParam(value = "count") count: Int) =
    Action.async {
      implicit request =>
        try {
          authorize {
            implicit authInfo =>
              projectModule.getUserProjects(id, offset, count) map (responseOk(_))
          }
        } catch {
          case e: Throwable => asyncBadRequest(e)
        }

    }

  @ApiOperation(value = "Create task", notes = "Create task", response = classOf[TaskDTO], httpMethod = "POST", nickname = "createTask")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(value = "The new task to be added", required = true, dataType = "TaskDTO", paramType = "body"),
    new ApiImplicitParam(name = "Authorization", value = "authorization", defaultValue = "OAuth token", required = true, dataType = "string", paramType = "header")
  ))
  def insertTask(@ApiParam(value = "project id", required = true) @PathParam(value = "projectId") projectId: String
                  ) =
    Action.async {
      implicit request =>
        request.body.asJson.map {
          json => try {
            authorize {
              implicit authInfo =>
                try {
                  val dto = json.as[TaskDTO].copy(projectId = Some(projectId), userId = Some(authInfo.user.id))
                  projectModule.insertTask(dto) map (responseOk(_))
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

  @ApiOperation(value = "Get project top level tasks", notes = "Get project tasks", response = classOf[List[TaskDTO]], httpMethod = "GET", nickname = "getTasks")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "Authorization", value = "authorization", defaultValue = "OAuth token", required = true, dataType = "string", paramType = "header")
  ))
  def getTasks(@ApiParam(value = "project id", required = true) @PathParam(value = "projectId") projectId: String,
               @ApiParam(value = "offset", required = true, defaultValue = "0") @QueryParam(value = "offset") offset: Int,
               @ApiParam(value = "count", required = true, defaultValue = "100") @QueryParam(value = "count") count: Int) =
    Action.async {
      implicit request =>
        try {
          authorize {
            implicit authInfo =>
              projectModule.getTasks(projectId, offset, count) map (responseOk(_))
          }
        } catch {
          case e: Throwable => asyncBadRequest(e)
        }
    }

  @ApiOperation(value = "Delete project", notes = "Delete project", response = classOf[BooleanDTO], httpMethod = "DELETE", nickname = "deleteProject")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "Authorization", value = "authorization", defaultValue = "OAuth token", required = true, dataType = "string", paramType = "header")
  ))
  def deleteProject(
                     @ApiParam(value = "project id", required = true) @PathParam(value = "projectId") projectId: String
                     ) =
    Action.async {
      implicit request =>
        authorize {
          implicit authInfo =>
            projectModule.deleteProject(projectId) map (responseOk(_))
        }
    }


}
