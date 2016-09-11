package org.shopping

import org.shopping.db.User
import org.shopping.util.Gen

import scala.concurrent._

import scalaoauth2.provider.AuthInfo
import ExecutionContext.Implicits.global

package object modules {

  implicit class CustomStringOption(str: Option[String]) {

    def getOrGuid = str match {
      case Some(s) => if (s.isEmpty) Gen.guid else s
      case _ => Gen.guid
    }

  }

  type ResultError = (Int, String)

  type Result[T] = Future[Either[ResultError, T]]
  type AuthData = AuthInfo[User]

  def result[T](v: T) = Future.successful(Right(v))

  def resultSync[T](v: T) = Right(v)

  def resultError(errCode: Int, errMessage: String, data: String = "") = Future.successful(Left(errCode, errMessage))

  def resultErrorSync(errCode: Int, errMessage: String, data: String = "") = Left(errCode, errMessage)

  def resultEx(ex: Throwable, data: String = "", errCode: Int = 500) = Future.successful(Left(errCode, ex.getMessage))

  def resultExSync(ex: Throwable, data: String = "", errCode: Int = 500) = Left(errCode, ex.getMessage)

  object PermProject {
    val OwnerRead = 1
    val OwnerReadWrite = 2
    val OwnerReadWriteDelete = 4
    val GroupRead = 8
    val GroupReadWrite = 16
    val GroupReadWriteDelete = 32
    val PublicRead = 64
    val PublicReadWrite = 128
    val PublicReadWriteDelete = 256
  }

  implicit class CustomFuture[S, T](val f: Future[Either[String, S]]) {

    def >>=(tf: S => Future[Either[String, Any]]): Future[Either[String, Any]] = {
      val fRet: Future[Either[String, Any]] = f flatMap {
        case Left(err) => Future.successful(Left(err))
        case Right(r1) =>
          tf(r1) map {
            case Left(err) => Left(err)
            case Right(r2) => Right(r2)
          }
      }
      fRet recover {
        case e: Throwable => Left(e.getMessage)
      }
    }

  }

  implicit class ErrorExtractor[T](val ret: Either[ResultError, T]) {

    def errCode = ret match {
      case Left(er) => er._1
      case _ => 0
    }

    def errMessage = ret match {
      case Left(er) => er._2
      case _ => ""
    }

    def value = ret match {
      case Left(er) => None
      case Right(v) => Some(v)
    }
  }

}
