package org.shopping.repo.impl

import org.shopping.repo.Caching
import play.api.Logger
import scredis._
import scredis.serialization._
import play.api.libs.json._

import scala.concurrent._
import scala.concurrent.duration._

class RedisCaching extends Caching {

  class UTF8Reader extends StringReader("UTF-8")

  lazy val client = Redis()

  import client.dispatcher

  override def set[A](key: String, value: A, expiration: Int = 0)(implicit m: Format[A]): Future[Boolean] = {
    try {
      val json = Json.toJson(value).toString()
      val f = client.set(key, json, if (expiration == 0) None else Some(expiration.seconds))
      f recover { case _ => false }
    } catch {
      case ex: Throwable =>
        Logger.error(ex.getMessage)
        Future.successful(false)
    }
  }

  override def get[A](key: String)(implicit m: Format[A]): Future[Option[A]] = {
    try {
      val f = client.get(key) map {
        case Some(value) =>
          try {
            val res = Json.parse(value).as[A] //  Json().readValue(value, m.runtimeClass).asInstanceOf[A]
            Some(res)
          } catch {
            case ex: Exception => None
          }
        case _ => None
      }
      f recover { case _ => Option.empty[A] }
    } catch {
      case ex: Throwable =>
        Logger.error(ex.getMessage)
        Future.successful(None)
    }
  }

  override def getOrElseSync[A](key: String, expiration: Int = 0)(orElse: => A)(implicit m: Format[A]): Future[A] = getOrElse[A](key, expiration)(Future.successful(orElse))

  override def getOrElse[A](key: String, expiration: Int = 0)(orElse: => Future[A])(implicit m: Format[A]): Future[A] =
    try {
      val f: Future[A] = client.get(key) flatMap {
        case Some(v) => Future.successful {
          //          Json().readValue(v, m.runtimeClass).asInstanceOf[A]
          Json.parse(v).as[A]
        }
        case _ =>
          orElse flatMap {
            case None => Future.successful(None.asInstanceOf[A])
            case v if v != null =>
              set(key, v, expiration) map {
                case true => v
                case _ => throw new RuntimeException("Saving value failed")
              }
            case v => Future.successful(v)
          }
      }
      f recoverWith { case _ => orElse }
    } catch {
      case ex: Throwable =>
        Logger.error(ex.getMessage)
        orElse
    }

  override def getOrElseOpt[A](key: String, expiration: Int)(orElse: => Future[Option[A]])(implicit m: Format[A]): Future[Option[A]] = {
    try {
      val f: Future[Option[A]] = client.get(key) flatMap {
        case Some(v) => Future.successful(Some(Json.parse(v).as[A]))
        case _ => orElse
      }
      f recoverWith { case _ => orElse }
    } catch {
      case ex: Throwable =>
        Logger.error(ex.getMessage)
        orElse
    }
  }
}




