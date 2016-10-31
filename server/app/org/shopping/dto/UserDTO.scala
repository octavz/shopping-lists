package org.shopping.dto

import org.shopping.models.User
import org.shopping.util.Time

case class UserDTO(login: String, password: String, id: String, nick: String) {

  def this(model: User) = this(model.login, model.password, model.id, model.nick)

  def toModel = {
    val n = Time.now
    User(id = id, login = login, providerToken = Some(login), created = n, updated = n, lastLogin = None, password = password, nick = nick)
  }

}
