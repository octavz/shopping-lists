package org.shopping.dto

import org.shopping.models.User
import org.shopping.util.{Gen, Time}

case class RegisterRequestDTO(login: String, password: String) {

  def this(model: User) = this(model.login, model.password)

  def toModel(id: String) = {
    val n = Time.now
    User(id = id, login = login, providerToken = Some(login), created = n, updated = n, lastLogin = None, password = password, nick = login.takeWhile(_ != '@'))
  }
}
