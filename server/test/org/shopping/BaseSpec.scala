package org.shopping

import org.scalamock.scalatest.MockFactory
import org.scalatestplus.play.PlaySpec
import org.shopping.models.User
import org.shopping.services._
import org.shopping.util.Gen._
import org.shopping.util.Time._
import play.api._

import scala.concurrent._
import scala.concurrent.duration._
import scala.language.postfixOps

class BaseSpec extends PlaySpec with MockFactory{
  val testConf = Map(
      "evolutions" -> "disabled",
      "slick.dbs.default.driver" -> "slick.driver.PostgresDriver$",
      "slick.dbs.default.db.driver" -> "org.postgresql.Driver",
      "slick.dbs.default.db.url" -> s"jdbc:postgresql://localhost:5432/mytest",
      "slick.dbs.default.db.user" -> "postgres",
      "slick.dbs.default.db.password" -> "root"
    )

  def waitFor[T](f: Future[T], duration: FiniteDuration = 1000.milli)
                (implicit ec: ExecutionContext): T = Await.result(f, duration)

  def anUser = User(id = guid, login = guid, providerToken = None, created = now(), updated = now(), lastLogin = None, password = guid, nick = guid)

  implicit val authInfo = new AuthData(anUser, Some("1"), None, None)

  def running(a: Application)(call: => Unit): Unit = {
    Play.start(a)
    try {
      call
    } catch {
      case t: Throwable =>
        Play.stop(a)
        throw t
    }
  }

}
