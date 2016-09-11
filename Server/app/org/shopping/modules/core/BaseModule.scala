package org.shopping.modules.core

import org.shopping.modules.AuthData

trait BaseModule {
  protected var _authData: AuthData = null

  def setAuth(value: AuthData) = {
    _authData = value
  }

  def authData: AuthData = _authData


  def userId = {
    authData.user.id
  }
}
