package org.shopping.dal

import org.shopping.db._
import org.shopping.models.{User, UserSession}

trait UserRepo {

  def insertSession(us: UserSession): DAL[UserSession]

  def insertUser(user: User): DAL[User]

  def findSessionById(id: String): DAL[Option[UserSession]]

  def deleteSessionByUser(uid: String): DAL[Int]

  def getUserById(uid: String): DAL[User]

  def getUserByEmail(email: String): DAL[Option[User]]

  def searchUsers(email: Option[String], nick: Option[String]): DAL[Seq[User]]


}
