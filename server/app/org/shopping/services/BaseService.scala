package org.shopping.services

import scala.concurrent.{ExecutionContext, Future}

trait BaseService {

  def userId(implicit authData: AuthData) = authData.user.id

  private[services] def checkUser[T](v: => Future[Boolean])(c: => Result[T])(implicit ec: ExecutionContext): Result[T] =
    v flatMap {
      valid => if (!valid) error(401 -> "Permission denied.") else c
    }

  private[services] def checkUser[T](v: => Boolean)(c: => Result[T]): Result[T] =
    if (!v) error(401 -> "Permission denied.") else c

}
