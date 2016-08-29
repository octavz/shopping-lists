package org.planner.dal.impl

import com.google.inject.Inject
import org.planner.dal.JsonFormats._
import org.planner.dal.{DAL, _}
import org.planner.db._
import play.api.Play
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._

class SlickUserDAL @Inject()(cache: Caching) extends UserDAL with DB {
  val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)
  val profile = dbConfig.driver
  val db = dbConfig.db

  import dbConfig.driver.api._

  override def create = {
    //(Clients.ddl ++ Users.ddl ++ GrantTypes.ddl ++ ClientGrantTypes.ddl ++ AccessTokens.ddl ++ AuthCodes.ddl).create
    Future.successful(())
  }

  override def insertSession(us: UserSession): DAL[UserSession] = {
    db.run(UserSessions += us).map(_ => us)
  }

  override def findSessionById(id: String): DAL[Option[UserSession]] =
    cache.getOrElseOpt(CacheKeys.session(id)) {
      db.run(UserSessions.filter(_.id === id).result.headOption)
    }

  override def deleteSessionByUser(uid: String): DAL[Int] = {
    val action = sqlu"delete from user_sessions where user_id = $uid"
    db.run(action)
  }

  override def getUserById(uid: String): DAL[User] = {
    cache.getOrElse(CacheKeys.user(uid)) {
      db.run(Users.filter(_.id === uid).result.head)
    }
  }

  override def insertUser(user: User): DAL[User] = {
    db.run(Users += user).map(_ => user)
  }

  override def getUserByEmail(email: String): DAL[Option[User]] = {
    cache.getOrElseOpt(CacheKeys.byEmail(email)) {
      db.run(Users.filter(_.login === email).result.headOption)
    }
  }

  override def insertGroup(model: Group): DAL[Group] = db.run(Groups.+=(model)) map (_ => model)

  override def insertGroupsUser(model: GroupsUser): DAL[GroupsUser] = db.run(GroupsUsers.+=(model)) map (_ => model)

  override def insertGroupWithUser(model: Group, userId: String): DAL[Group] = {
    val action = (for {
      _ <- Groups += model
      _ <- GroupsUsers += GroupsUser(model.id, userId)
    } yield ()
      ).transactionally
    db run action map (_ => model)
  }

  override def getUserGroupsIds(userId: String): DAL[List[String]] =
    cache.getOrElse(CacheKeys.userGroupsIds(userId)) {
      db.run(GroupsUsers.filter(_.userId === userId).map(_.groupId).result).map(_.toList)
    }

  override def getUserGroups(userId: String): DAL[List[Group]] = {
    cache.getOrElse(CacheKeys.userGroups(userId)) {
      val q = for {
        (groupUser, group) <- GroupsUsers join Groups on (_.groupId === _.id)
      } yield group
      db.run(q.result).map(_.toList)
    }
  }

  override def searchUsers(email: Option[String], nick: Option[String]): DAL[List[User]] = {
    val r = if (email.isDefined && nick.isDefined) db.run(Users.filter(u => u.nick === nick.get || u.login === email.get).result)
    else if (email.isDefined && nick.isEmpty) db.run(Users.filter(u => u.login === email.get).result)
    else if (email.isEmpty && nick.isDefined) db.run(Users.filter(u => u.nick === nick.get).result)
    else Future.successful(Seq.empty)
    r map (_.toList)
  }
}
