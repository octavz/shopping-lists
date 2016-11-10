package org.shopping.dto

import org.shopping.models.User

case class UsersDTO(items: Seq[UserDTO])

case class UserDTO(login: String, password: String, id: String, nick: String) {

  def this(model: User) = this(model.login, model.password, model.id, model.nick)

  def toModel = {
    User(id = id, login = login, providerToken = Some(login), lastLogin = None, password = password, nick = nick)
  }

}

case class RegisterResponseDTO(login: String, password: String) {

  def this(model: User) = this(model.login, model.password)

  def toModel(userId: String) = {
    User(id = userId, login = login, providerToken = Some(login), lastLogin = None, password = password, nick = login)
  }
}

case class RegisterRequestDTO(login: String, password: String) {

  def this(model: User) = this(model.login, model.password)

  def toModel(id: String) = {
    User(id = id, login = login, providerToken = Some(login), lastLogin = None, password = password, nick = login.takeWhile(_ != '@'))
  }
}

//new ApiImplicitParam(name = "username", required = true, dataType = "String", paramType = "form", defaultValue = "aaa@aaa.com")
//, new ApiImplicitParam(name = "password", required = true, dataType = "String", paramType = "form", defaultValue = "123456")
//, new ApiImplicitParam(name = "client_id", required = true, dataType = "String", paramType = "form", defaultValue = "1")
//, new ApiImplicitParam(name = "grant_type", required = true, dataType = "String", paramType = "form", defaultValue = "password")
//, new ApiImplicitParam(name = "client_secret", required = true, dataType = "String", paramType = "form", defaultValue = "secret")

case class LoginRequestDTO(login: String, password: String, clientId: Option[String], grantType: Option[String],
  clientSecret: Option[String])

case class LoginResponseDTO(accessToken: String, user: UserDTO)
