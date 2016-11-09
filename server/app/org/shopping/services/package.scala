package org.shopping

import org.shopping.dto.ErrorDTO
import org.shopping.models.User
import org.shopping.util.{ErrorMessages, Gen, _}
import play.api.Logger

import scala.concurrent._
import scalaoauth2.provider.AuthInfo

package object services {
  val log = Logger.logger

  implicit class CustomStringOption(str: Option[String]) {

    def getOrGuid = str match {
      case Some(s) => if (s.isEmpty) Gen.guid else s
      case _ => Gen.guid
    }

  }

  type Result[T] = Future[Either[ErrorDTO, T]]

  type AuthData = AuthInfo[User]

  def result[T](v: T): Result[T] = Future.successful(Right(v))

  def resultSync[T](v: T): Either[ErrorDTO, T] = Right(v)

  def error[T](errMessage: String, errCode: Int = 400): Result[T] = {
    log.error(s"${currentMethod()}: $errMessage")
    Future.successful(Left(ErrorDTO(errCode, errMessage)))
  }

  def error[T](err: (Int, String)): Result[T] = err match {
    case (errCode, errMessage) =>
      log.error(s"${currentMethod()}: $errMessage")
      Future.successful(Left(ErrorDTO(errCode, errMessage)))
  }

  def errorSync[T](errCode: Int, errMessage: String): Either[ErrorDTO, T] = {
    log.error(s"${currentMethod()}: $errMessage")
    Left(ErrorDTO(errCode, errMessage))
  }

  def ex[T](ex: Throwable, errMessage: String = ErrorMessages.SERVER_ERROR, errCode: Int = 500): Result[T] = {
    log.error(errMessage, ex)
    Future.successful(Left(ErrorDTO(errCode, errMessage)))
  }

  def exSync(ex: Throwable, errMessage: String = ErrorMessages.SERVER_ERROR, errCode: Int = 500) = {
    log.error(errMessage, ex)
    Left(ErrorDTO(errCode, errMessage))
  }

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

  implicit class ErrorExtractor[T](val ret: Either[ErrorDTO, T]) {

    def errCode = ret match {
      case Left(er) => er.errCode
      case _ => 0
    }

    def errMessage = ret match {
      case Left(er) => er.message
      case _ => ""
    }

    def value = ret match {
      case Left(er) => None
      case Right(v) => Some(v)
    }
  }

}
