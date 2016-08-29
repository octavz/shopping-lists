package org.planner.dal

import org.planner.db._

trait UserDAL {

  def create: DAL[Unit]

  def insertSession(us: UserSession): DAL[UserSession]

  def insertUser(user: User): DAL[User]

  def findSessionById(id: String): DAL[Option[UserSession]]

  def deleteSessionByUser(uid: String): DAL[Int]

  def getUserById(uid: String): DAL[User]

  def getUserByEmail(email: String): DAL[Option[User]]

  def insertGroup(model: Group): DAL[Group]

  def insertGroupsUser(model: GroupsUser): DAL[GroupsUser]

  def insertGroupWithUser(model: Group, userId: String): DAL[Group]

  def getUserGroupsIds(userId: String): DAL[List[String]]

  def getUserGroups(userId: String): DAL[List[Group]]

  def searchUsers(email: Option[String], nick: Option[String]): DAL[List[User]]


}
