package org.shopping.dal.impl

import com.google.inject.Inject
import org.shopping.dal.{Repo, _}
import org.shopping.db._
import org.shopping.models.{User, UserSession}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._

class SlickUserRepo @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
  extends UserRepo
    with HasDatabaseConfigProvider[JdbcProfile]
    with DB {
  val profile = dbConfig.driver

  import profile.api._

  override def insertSession(model: UserSession): Repo[UserSession] = model.insert { m =>
    db.run(UserSessions += m).map(_ => m)
  }

  override def findSessionById(id: String): Repo[Option[UserSession]] =
  //    cache.getOrElseOpt(CacheKeys.session(id)) {
    db.run(UserSessions.filter(_.id === id).result.headOption)

  //    }

  override def deleteSessionByUser(uid: String): Repo[Int] = {
    val action = sqlu"delete from user_sessions where user_id = $uid"
    println(action.statements)
    db.run(action)
  }

  override def getUserById(uid: String): Repo[User] = {
    //    cache.getOrElse(CacheKeys.user(uid)) {
    db.run(Users.filter(_.id === uid).result.head)
    //    }
  }

  override def insertUser(user: User): Repo[User] = insert(user) { m =>
    db.run(Users += m).map(_ => m)
  }

  override def getUserByEmail(email: String): Repo[Option[User]] = {
    //    cache.getOrElseOpt(CacheKeys.byEmail(email)) {
    db.run(Users.filter(_.login === email).result.headOption)
    //    }
  }

  override def searchUsers(email: Option[String], nick: Option[String]): Repo[Seq[User]] = {
    val r = if (email.isDefined && nick.isDefined) db.run(Users.filter(u => u.nick === nick.get || u.login === email.get).result)
    else if (email.isDefined && nick.isEmpty) db.run(Users.filter(u => u.login === email.get).result)
    else if (email.isEmpty && nick.isDefined) db.run(Users.filter(u => u.nick === nick.get).result)
    else Future.successful(Seq.empty)
    r map (_.toSeq)
  }
}
