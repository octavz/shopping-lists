package org.shopping

import java.sql.Timestamp
import java.text.SimpleDateFormat

import org.shopping.db.{User, UserSession}
import play.api.libs.json._

import scala.concurrent.Future

package object dal {

  type DAL[T] = Future[T]

  def dal[T](v: T): DAL[T] = Future.successful(v)

  def dalErr[T](error: String): DAL[T] = Future.failed(new Exception(error))

  object JsonFormats extends DefaultReads with DefaultWrites {

    implicit object timestampFormat extends Format[Timestamp] {
      val format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SS'Z'")

      def reads(json: JsValue) = {
        val str = json.as[String]
        JsSuccess(new Timestamp(format.parse(str).getTime))
      }

      def writes(ts: Timestamp) = JsString(format.format(ts))
    }

    implicit val fmtUser = Json.format[User]
    implicit val fmtSession = Json.format[UserSession]
  }


  object CacheKeys {
    def session(id: String): String = s"session:$id"

    def user(id: String): String = s"user:$id"

    def userGroupsIds(id: String): String = s"userGroupIds:$id"

    def userGroups(id: String): String = s"userGroup:$id"

    def byEmail(email: String): String = s"user:email:$email"

  }

}

