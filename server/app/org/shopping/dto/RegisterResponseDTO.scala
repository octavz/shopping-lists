package org.shopping.dto

import org.shopping.db._
import org.shopping.util.{Gen, Time}

case class RegisterResponseDTO(login: String, password: String) {

  def this(model: User) = this(model.login, model.password)

  def toModel = {
    val n = Time.now
    User(id = Gen.guid, login = login, providerToken = Some(login), created = n, updated = n, lastLogin = None, password = password, nick = login)
  }
}
