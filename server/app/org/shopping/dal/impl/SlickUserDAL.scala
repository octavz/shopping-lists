package org.shopping.dal.impl

import com.google.inject.Inject
import org.shopping.dal.JsonFormats._
import org.shopping.dal.{DAL, _}
import org.shopping.db._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._

class SlickUserDAL @Inject()(protected val dbConfigProvider: DatabaseConfigProvider, cache: Caching)
  extends UserDAL
    with HasDatabaseConfigProvider[JdbcProfile]
    with DB {
  val profile = dbConfig.driver

  import profile.api._

  override def insertSession(us: UserSession): DAL[UserSession] = {
    db.run(UserSessions += us).map(_ => us)
  }

  override def findSessionById(id: String): DAL[Option[UserSession]] =
    cache.getOrElseOpt(CacheKeys.session(id)) {
      db.run(UserSessions.filter(_.id === id).result.headOption)
    }

  override def deleteSessionByUser(uid: String): DAL[Int] = {
    val action = sqlu"delete from user_sessions where user_id = $uid"
    println(action.statements)
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

  override def searchUsers(email: Option[String], nick: Option[String]): DAL[Seq[User]] = {
    val r = if (email.isDefined && nick.isDefined) db.run(Users.filter(u => u.nick === nick.get || u.login === email.get).result)
    else if (email.isDefined && nick.isEmpty) db.run(Users.filter(u => u.login === email.get).result)
    else if (email.isEmpty && nick.isDefined) db.run(Users.filter(u => u.nick === nick.get).result)
    else Future.successful(Seq.empty)
    r map (_.toSeq)
  }
}
