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

  type Result[+T] = Future[Either[ErrorDTO, T]]

  type AuthData = AuthInfo[User]

  def result[T](v: T): Result[T] = Future.successful(Right(v))

  def resultSync[T](v: T): Either[ErrorDTO, T] = Right(v)

  def error(err: ErrorDTO): Result[Nothing] = Future.successful(Left(err))

  def errorSync(err: ErrorDTO): Either[ErrorDTO,Nothing] = Left(err)

  def error(errMessage: String, errCode: Int = 400): Result[Nothing] = {
    log.error(s"${currentMethod()}: $errMessage")
    Future.successful(Left(ErrorDTO(errCode, errMessage)))
  }

  def error(err: (Int, String)): Result[Nothing] = err match {
    case (errCode, errMessage) =>
      log.error(s"${currentMethod()}: $errMessage")
      Future.successful(Left(ErrorDTO(errCode, errMessage)))
  }

  def errorSync(errCode: Int, errMessage: String): Either[ErrorDTO, Nothing] = {
    log.error(s"${currentMethod()}: $errMessage")
    Left(ErrorDTO(errCode, errMessage))
  }

  def ex(ex: Throwable, errMessage: String = ErrorMessages.SERVER_ERROR, errCode: Int = 500): Result[Nothing] = {
    log.error(errMessage, ex)
    Future.successful(Left(ErrorDTO(errCode, errMessage)))
  }

  def exSync(ex: Throwable, errMessage: String = ErrorMessages.SERVER_ERROR, errCode: Int = 500) = {
    log.error(errMessage, ex)
    Left(ErrorDTO(errCode, errMessage))
  }

  def seqEither[S, T](data: Seq[Either[S, T]]): Either[S, Seq[T]] =
    data.partition(_.isRight) match {
      case (l, Nil) => Right(l.map(_.right.get))
      case (_, h :: _) => Left(h.left.get)
    }


  def seqEitherWithFold[S, T1, T2](data: Seq[Either[S, T1]])(acc: T2)(f: (T2, T1) => T2): Either[S, T2] =
    data.partition(_.isRight) match {
      case (l, Nil) => Right(l
        .map(_.right.get)
        .foldLeft(acc)(f))
      case (_, h :: _) => Left(h.left.get)
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
