package org.shopping.repo

import org.shopping.models.{User, UserSession}

trait UserRepo {

  def insertSession(us: UserSession): Repo[UserSession]

  def insertUser(user: User): Repo[User]

  def updateUser(user: User): Repo[User]

  def findSessionById(id: String): Repo[Option[UserSession]]

  def deleteSessionByUser(uid: String): Repo[Int]

  def getUserById(uid: String): Repo[Option[User]]

  def getUserByEmail(email: String): Repo[Option[User]]

  def searchUsers(email: Option[String], nick: Option[String]): Repo[Seq[User]]


}
