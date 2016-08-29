package org.planner.modules.core

import org.planner.modules.Result
import org.planner.modules.dto.{BooleanDTO, UserDTO, GroupDTO, RegisterDTO}
import scala.concurrent._

import scalaoauth2.provider.{AuthorizationRequest, GrantHandlerResult, OAuthError}

  trait UserModule extends BaseModule {

    def getUserById(uid: String): Result[UserDTO]

    def getUserByToken(token: String): Result[UserDTO]

    def createSession(accessToken: String): Result[String]

    def login(request: AuthorizationRequest): Future[Either[OAuthError, GrantHandlerResult]]

    def registerUser(u: RegisterDTO): Result[RegisterDTO]

    def addGroup(dto: GroupDTO): Result[GroupDTO]

    def searchUsers(email: Option[String], nick: Option[String]): Result[List[UserDTO]]

    def addUsersToGroup(groupId: String, userIds: List[String]): Result[BooleanDTO]

}
