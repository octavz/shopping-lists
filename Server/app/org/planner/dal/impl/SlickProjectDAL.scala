package org.planner.dal.impl

import com.google.inject.Inject
import org.planner.dal._
import org.planner.db._
import org.planner.util.Constants
import org.planner.util.Time._
import play.api.Play
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile

import scala.concurrent.ExecutionContext.Implicits.global

class SlickProjectDAL @Inject()(cache: Caching) extends ProjectDAL with DB {
  val config = DatabaseConfigProvider.get[JdbcProfile](Play.current)
  val profile = config.driver
  val db = config.db

  import config.driver.api._

  override def insertProject(model: Project, group: Group): DAL[Project] = {
    val action = (for {
      _ <- Projects += model
      _ <- Groups += group
      _ <- GroupsUsers += GroupsUser(userId = model.userId, groupId = group.id)
    } yield ()).transactionally

    db run action map (_ => model)
  }

  override def updateProject(model: Project): DAL[Project] = {
    val newModel = model.copy(updated = now)
    db.run(Projects.filter(_.id === model.id).update(newModel)) map (_ => model)
  }

  override def getUserProjects(uid: String, offset: Int, count: Int): DAL[(List[(Group, Project)], Int)] = {
    val query = for {
      ((groupUser, group), projects) <- GroupsUsers join Groups on (_.groupId === _.id) join Projects on (_._2.projectId === _.id) if groupUser.userId === uid
    } yield (group, projects)

    val action = (for {
      l <- query.drop(offset).take(count).result
      c <- query.length.result
    } yield (l, c)).transactionally

    db.run(action).map {
      case (l, c) => (l.toList, c)
    }
  }

  override def getProjectGroups(projectId: String): DAL[List[Group]] = {
    val action = Groups.filter(_.projectId === projectId).result
    db.run(action) map {
      _.toList
    }
  }

  override def getUserPublicProjects(
                                      uid: String, offset: Int, count: Int): DAL[(List[(Group, Project)], Int)] = {
    val projectsByUser = sql"""
            SELECT g.*, p.* FROM projects p
            INNER JOIN groups g ON g.project_id = p.id
            INNER JOIN groups_users gu ON gu.group_id = g.id
            WHERE
            gu.user_id = $uid AND
            (p.perm & 64 <> 0 OR p.perm & 128 <> 0) and p.status <> ${Constants.STATUS_DELETE}
            offset $offset limit $count
            """.as[(Group, Project)]

    val total = sql"""
            SELECT count(g.*) FROM projects p
            INNER JOIN groups g ON g.project_id = p.id
            INNER JOIN groups_users gu ON gu.group_id = g.id
            WHERE gu.user_id = $uid AND
            (p.perm & 64 <> 0 OR p.perm & 128 <> 0) and p.status <> ${Constants.STATUS_DELETE}
            """.as[Int]
    val action = (for {
      lst <- projectsByUser
      count <- total
    } yield (lst, count)).transactionally
    db.run(action).map {
      case (l, c) => (l.toList, c.head)
    }
  }

  override def getProjectById(id: String) = {
    val action = Projects.filter(p => p.id === id && p.status =!= Constants.STATUS_DELETE).take(1).result.headOption
    db run action
  }

  override def insertTask(model: Task): DAL[Task] = {
    val action = Tasks += model
    db.run(action) map (_ => model)
  }

  override def getTasksByProjectAndUser(projectId: String, userId: String, offset: Int, count: Int): DAL[(List[Task], Int)] = {
    val q = Tasks.filter(t => t.projectId === projectId)
    val action = (for {
      l <- q.take(count).drop(offset).result
      c <- q.length.result} yield (l, c)).transactionally

    db.run(action) map {
      case (l, c) => (l.toList, c)
    }
  }

}
