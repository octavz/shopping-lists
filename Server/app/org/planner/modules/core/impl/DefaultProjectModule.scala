package org.planner.modules.core.impl

import com.google.inject.Inject

import org.planner.dal._
import org.planner.modules.core.ProjectModule
import org.planner.modules.dto._
import org.planner.util.Constants
import play.api.http.Status
import org.planner.modules._
import org.planner.db._
import org.planner.util.Gen._
import org.planner.util.Time._

import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits._
import scala.language.postfixOps

class DefaultProjectModule @Inject()(dalUser: UserDAL, dalProject: ProjectDAL) extends ProjectModule {

  override def insertProject(dto: ProjectDTO): Result[ProjectDTO] = {
    val project = dto.toModel()
    val group = Group(id = guid, projectId = project.id, name = project.name, created = now, updated = now, userId = authData.user.id, groupId = None, perm = PermProject.OwnerReadWriteDelete)
    val f = dalProject.insertProject(project, group) map (p => resultSync(new ProjectDTO(p, group)))
    f recover { case e: Throwable => resultExSync(e, "addGroup") }
  }

  override def getUserProjects(id: String, offset: Int, count: Int): Result[ProjectListDTO] = {
    println(s"Wanted for: $id - current user: $userId")
    val f = for {
      res <- if (id == userId) dalProject.getUserProjects(userId, offset, count) else dalProject.getUserPublicProjects(id, offset, count)
    } yield resultSync(ProjectListDTO(items = res._1.map(p => new ProjectDTO(p._2, p._1)).distinct, total = res._2))

    f recover { case e: Throwable => resultExSync(e, "getUserProject") }
  }

  override def updateProject(dto: ProjectDTO): Result[ProjectDTO] = {
    val f = if (dto.id.isEmpty) Future.failed(new Exception("Project has empty id"))
    else {
      checkUser(dto.userId.getOrElse(throw new Exception("User not found!")), dto.id.get) flatMap {
        res =>
          if (!res) throw new Exception("Not valid")
          dalProject.getProjectById(dto.id.get) flatMap {
            case None => resultError(Status.NOT_FOUND, "Project not found")
            case Some(project) => dalProject.updateProject(dto.toModel()) map { p =>
              resultSync(new ProjectDTO(p, Group(id = "", projectId = p.id, name = "", userId = "user", groupId = None, created = p.created, updated = p.updated)))
            }
          }
      }
    }

    f recover { case e: Throwable => resultExSync(e, "updateProject") }
  }

  private def checkUser(userId: String, projectId: String): Future[Boolean] = {
    for {
      projectGroups <- dalProject.getProjectGroups(projectId)
      userGroups <- dalUser.getUserGroupsIds(userId)
    } yield {
      projectGroups map (_.id) intersect (userGroups) nonEmpty
    }
  }

  override def insertTask(task: TaskDTO): Result[TaskDTO] = {
    val f = dalProject.getProjectGroups(task.projectId.getOrElse(throw new Exception("Task has no project id"))) flatMap {
      projectGroups =>
        dalUser.getUserGroupsIds(task.userId.getOrElse(throw new Exception("User id not found"))) flatMap {
          userGroups =>
            task.groupId match {
              case Some(gid) =>
                projectGroups.find(_.id == gid) match {
                  case None =>
                    Future.failed(new Exception("Attached group doesn't belong to the project"))
                  case _ =>
                    val userBelongsToProject = projectGroups.map(_.id).intersect(userGroups).nonEmpty
                    if (!userBelongsToProject) Future.failed(new Exception(s"User ${task.userId.get} doesn't belong to project ${task.projectId.get}"))
                    else dalProject.insertTask(task.toModel()) map { _ => resultSync(task) }
                }
              case None =>
                projectGroups.find(_.`type` == Constants.DEFAULT_GROUP_TYPE) match {
                  case Some(mainGroup) =>
                    val model = task.toModel().copy(groupId = mainGroup.projectId)
                    dalProject.insertTask(model) map { _ => resultSync(task) }
                  case None => throw new Exception(s"Project ${task.projectId.get} has no main group")
                }
            }
        }
    }

    f recover {
      case e: Throwable => resultExSync(e, "insertTask")
    }
  }

  override def getTasks(projectId: String, offset: Int, count: Int): Result[TaskListDTO] = {
    val f = checkUser(userId, projectId) flatMap {
      isValid =>
        if (!isValid) throw new Exception("User is not valid in context")
        dalProject.getTasksByProjectAndUser(projectId, userId, offset, count) map {
          res =>
            resultSync(TaskListDTO(items = res._1.map(t => new TaskDTO(t)), total = res._2))
        }
    }

    f recover {
      case e: Throwable => resultExSync(e, "getTasks")
    }
  }

  override def deleteProject(projectId: String): Result[BooleanDTO] = {
    val f = checkUser(userId, projectId) flatMap {
      isValid =>
        if (!isValid) throw new Exception("User is not valid in context")
        dalProject.getProjectById(projectId) flatMap {
          case Some(project) =>
            val newProject = project.copy(status = Constants.STATUS_DELETE)
            dalProject.updateProject(newProject) map {
              x =>
                resultSync(BooleanDTO(true))
            }
          case None =>
            resultError(Status.NOT_FOUND, "Project not found")
        }
    }

    f recover {
      case e: Throwable => resultExSync(e, "deleteProject")
    }
  }

}
