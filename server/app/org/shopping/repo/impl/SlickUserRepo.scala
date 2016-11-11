package org.shopping.repo.impl

import com.google.inject.Inject
import org.shopping.repo.{Repo, _}
import org.shopping.db._
import org.shopping.models.{User, UserSession}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._
import org.shopping.models.JsonModelFormats._

class SlickUserRepo @Inject()(protected val dbConfigProvider: DatabaseConfigProvider, cache: Caching)
  extends UserRepo
    with HasDatabaseConfigProvider[JdbcProfile]
    with DB {
  val profile = dbConfig.driver

  import profile.api._

  override def insertSession(model: UserSession): Repo[UserSession] = model.touch2 { m =>
    db.run(UserSessions += m).map(_ => m)
  }

  override def findSessionById(id: String): Repo[Option[UserSession]] =
    cache.getOrElseOpt(CacheKeys.session(id)) {
      db.run(UserSessions.filter(_.id === id).result.headOption)
    }

  override def deleteSessionByUser(uid: String): Repo[Int] = db.run {
    UserSessions.filter(_.userId === uid).delete
  }

  override def getUserById(uid: String): Repo[Option[User]] =
    cache.getOrElseOpt(CacheKeys.user(uid)) {
      db.run(Users.filter(_.id === uid).result.headOption)
    }

  override def insertUser(user: User): Repo[User] = user.touch2 { m =>
    db.run(Users += m).map(_ => m)
  }

  override def getUserByEmail(email: String): Repo[Option[User]] =
    cache.getOrElseOpt(CacheKeys.byEmail(email)) {
      db.run(Users.filter(_.login === email).result.headOption)
    }

  override def searchUsers(email: Option[String], nick: Option[String]): Repo[Seq[User]] = {
    val r = if (email.isDefined && nick.isDefined) {
      db.run(Users.filter(u => u.nick === nick.get || u.login === email.get).result)
    }
    else if (email.isDefined && nick.isEmpty) db.run(Users.filter(u => u.login === email.get).result)
    else if (email.isEmpty && nick.isDefined) db.run(Users.filter(u => u.nick === nick.get).result)
    else Future.successful(Seq.empty)
    r map (_.toSeq)
  }

  override def updateUser(user: User): Repo[User] = user.touch1 { m =>
    db.run(Users.filter(_.id === m.id).update(m)).map(_ => m)
  }

}
