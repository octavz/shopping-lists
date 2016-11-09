package org.shopping.repo.impl

import org.shopping.repo.Caching
import play.api.libs.json.Format
import scala.concurrent._

class TestCaching extends Caching {
  override def set[A](key: String, value: A, expiration: Int)(implicit m: Format[A]): Future[Boolean] = Future.successful(false)

  override def get[A](key: String)(implicit m: Format[A]): Future[Option[A]] = Future.successful(None)

  override def getOrElse[A](key: String, expiration: Int)(orElse: => Future[A])(implicit m: Format[A]): Future[A] = orElse

  override def getOrElseSync[A](key: String, expiration: Int)(orElse: => A)(implicit m: Format[A]): Future[A] = Future.successful(orElse)

  override def getOrElseOpt[A](key: String, expiration: Int)(orElse: => Future[Option[A]])(implicit m: Format[A]): Future[Option[A]] = orElse
}
