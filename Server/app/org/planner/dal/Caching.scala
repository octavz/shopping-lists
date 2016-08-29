package org.planner.dal

import play.api.libs.json.Format

import scala.concurrent.Future

trait Caching {
  def set[A](key: String, value: A, expiration: Int = 0)(implicit m: Format[A]): Future[Boolean]

  def get[A](key: String)(implicit m: Format[A]): Future[Option[A]]

  def getOrElse[A](key: String, expiration: Int = 0)(orElse: => Future[A])(implicit m: Format[A]): Future[A]

  def getOrElseOpt[A](key: String, expiration: Int = 0)(orElse: => Future[Option[A]])(implicit m: Format[A]): Future[Option[A]]

  def getOrElseSync[A](key: String, expiration: Int = 0)(orElse: => A)(implicit m: Format[A]): Future[A]
}

