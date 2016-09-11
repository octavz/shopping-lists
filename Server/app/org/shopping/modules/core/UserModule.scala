package org.shopping.modules.core

import org.shopping.dto.{RegisterDTO, UserDTO}
import org.shopping.modules.Result

import scala.concurrent._
import scalaoauth2.provider.{AuthorizationRequest, GrantHandlerResult, OAuthError}

  trait UserModule extends BaseModule {

    def getUserById(uid: String): Result[UserDTO]

    def getUserByToken(token: String): Result[UserDTO]

    def createSession(accessToken: String): Result[String]

    def login(request: AuthorizationRequest): Future[Either[OAuthError, GrantHandlerResult]]

    def registerUser(u: RegisterDTO): Result[RegisterDTO]

    def searchUsers(email: Option[String], nick: Option[String]): Result[Seq[UserDTO]]

}
