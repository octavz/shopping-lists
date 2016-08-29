import org.planner.db._
import org.planner.util.Gen._
import org.planner.util.Time._
import org.specs2.execute.{Result, AsResult}
import org.specs2.mock._
import org.specs2.mutable._
import play.api.db.slick.DatabaseConfigProvider
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.test.PlaySpecification
import slick.dbio.{DBIOAction, NoStream}
import slick.driver.JdbcProfile

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, _}
import scala.concurrent.duration._
import play.api.Application
import play.api.db.Databases
import play.api.db.evolutions._

object TestDB extends {
  val profile = slick.driver.H2Driver
} with DB

trait BaseDALSpec extends PlaySpecification {

  import TestDB._

  val duration = Duration.Inf

  def database = Databases.inMemory(
    name = "test",
    urlOptions = Map(
      "MODE" -> "PostgreSQL",
      "DATABASE_TO_UPPER" -> "false",
      "DB_CLOSE_DELAY" -> "-1"
    ),
    config = Map(
      "logStatements" -> true,
      "username" -> "sa",
      "password" -> "test"
    )
  )

  def test[T: AsResult](t: => T) = {
    try {
      println("around around around around around around around around around around ")
      running(testApp) {
        Evolutions.cleanupEvolutions(database)
        Evolutions.applyEvolutions(database)
        val result = AsResult(t)
        result

      }
    } catch {
      case e: Exception => {
        failure(e.getMessage())
      }
    }
  }

  def waitFor[T](f: Future[T]): T = Await.result(f, duration)

  lazy val guiceApp =
    new GuiceApplicationBuilder()
      .configure(Map(
      "slick.dbs.default.driver" -> "slick.driver.H2Driver$",
      "slick.dbs.default.db.driver" -> "org.h2.Driver",
      "slick.dbs.default.db.url" -> s"jdbc:h2:mem:test;DATABASE_TO_UPPER=false;MODE=PostgreSQL;DB_CLOSE_DELAY=-1",
      "slick.dbs.default.db.user" -> "sa",
      "slick.dbs.default.db.password" -> "test"))

  def dbSync[R](a: DBIOAction[R, NoStream, Nothing]): R = waitFor(db.run(a))

  def testApp: Application = {
    guiceApp.build()
  }

  val config = DatabaseConfigProvider.get[JdbcProfile](testApp)
  val profile = config.driver
  val db = config.db

  def newUser = User(id = guid, login = guid, providerToken = None, created = now, userId = None, groupId = None,
    updated = now, lastLogin = None, password = guid, nick = guid)

  def randProject(uid: String) = Project(id = guid, userId = uid, name = guid, description = guido, parentId = None, created = now, updated = now)

  def randGroup(p: Project) = Group(id = guid, projectId = p.id, name = p.name, created = now, updated = now, userId = "1", groupId = None)

  def genString(size: Int): String = (for (i <- 1 to size) yield "a").mkString
}
