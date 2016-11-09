package org.shopping.services

import scala.concurrent.{ExecutionContext, Future}

trait BaseService {
  private[services] var _authData: AuthData = _

  def setAuth(value: AuthData) = {
    _authData = value
  }

  def authData: AuthData = _authData

  def userId = {
    authData.user.id
  }

  private[services] def checkUser[T](v: => Future[Boolean])(c: => Result[T])
    (implicit ec: ExecutionContext): Result[T] =
    v flatMap {
      valid => if (!valid) error(401 -> "Permission denied.") else c
    }

}
