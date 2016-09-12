package org.shopping.modules.core.impl

import com.google.inject.Inject
import org.shopping.dal._
import org.shopping.dto._
import org.shopping.modules._
import org.shopping.modules.core.ListModule
import org.shopping.util.Constants
import play.api.http.Status

import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent._
import scala.language.postfixOps

class DefaultListModule @Inject()(dalUser: UserDAL, dalProject: ListDAL) extends ListModule {

  override def insertList(dto: ListDTO): Result[ListDTO] = {
    val project = dto.toModel()
    val f = dalProject.insertList(project) map (p => resultSync(new ListDTO(p)))
    f recover { case e: Throwable => resultExSync(e, "addGroup") }
  }

  override def getUserLists(id: String, offset: Int, count: Int): Result[ListsDTO] = {
    println(s"Wanted for: $id - current user: $userId")
    val f = for {
      res <- if (id == userId) dalProject.getUserLists(userId, offset, count) else throw new Exception("Unauthorized")
    } yield resultSync(ListsDTO(items = res._1.map(p => new ListDTO(p)).distinct, total = res._2))

    f recover { case e: Throwable => resultExSync(e, "getUserProjects") }
  }

  override def updateList(dto: ListDTO): Result[ListDTO] = {
    val f = if (dto.id.isEmpty) Future.failed(new Exception("Project has empty id"))
    else {
      checkUser(dto.userId.getOrElse(throw new Exception("User not found!")), dto.id.get) flatMap {
        res =>
          if (!res) throw new Exception("Not valid")
          dalProject.getListById(dto.id.get) flatMap {
            case None => resultError(Status.NOT_FOUND, "Project not found")
            case Some(project) => dalProject.updateLists(dto.toModel()) map { p =>
              resultSync(new ListDTO(p))
            }
          }
      }
    }

    f recover { case e: Throwable => resultExSync(e, "updateProject") }
  }

  private def checkUser(userId: String, projectId: String): Future[Boolean] = Future.successful {
    true
  }

  override def insertListItem(task: ListItemDTO): Result[ListItemDTO] = {
    val f = {
      val model = task.toModel()
      dalProject.addListItem(model) map { _ => resultSync(task) }
    }

    f recover {
      case e: Throwable => resultExSync(e, "insertTask")
    }
  }

  override def getListItems(projectId: String, offset: Int, count: Int): Result[ListItemListDTO] = {
    val f = checkUser(userId, projectId) flatMap {
      isValid =>
        if (!isValid) throw new Exception("User is not valid in context")
        dalProject.getListItemsByListAndUser(projectId, userId, offset, count) map {
          res =>
            resultSync(ListItemListDTO(items = res._1.map(t => new ListItemDTO(t)), total = res._2))
        }
    }

    f recover {
      case e: Throwable => resultExSync(e, "getTasks")
    }
  }

  override def deleteList(projectId: String): Result[BooleanDTO] = {
    val f = checkUser(userId, projectId) flatMap {
      isValid =>
        if (!isValid) throw new Exception("User is not valid in context")
        dalProject.getListById(projectId) flatMap {
          case Some(project) =>
            val newProject = project.copy(status = Constants.STATUS_DELETE)
            dalProject.updateLists(newProject) map {
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
