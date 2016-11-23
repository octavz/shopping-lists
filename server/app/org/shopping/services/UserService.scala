package org.shopping.services

import org.shopping.dto.{RegisterRequestDTO, UpdateUserDTO, UserDTO, UsersDTO}

import scala.concurrent.Future
import scalaoauth2.provider.{AuthorizationRequest, GrantHandlerResult, OAuthError}

trait UserService extends BaseService {

  def getUserById(uid: String)(implicit authData: AuthData): Result[UserDTO]

  def getUserByToken(token: String): Result[UserDTO]

  def createSession(accessToken: String): Result[String]

  def login(request: AuthorizationRequest): Future[Either[OAuthError, GrantHandlerResult]]

  def registerUser(u: RegisterRequestDTO): Result[RegisterRequestDTO]

  def searchUsers(email: Option[String], nick: Option[String])(implicit authData: AuthData): Result[UsersDTO]

  def updateUser(u: UpdateUserDTO)(implicit authData: AuthData): Result[UserDTO]

}
