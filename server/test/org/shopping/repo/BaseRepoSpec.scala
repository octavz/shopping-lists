package org.shopping.repo

import com.google.inject.AbstractModule
import org.shopping.config.RunModule
import org.shopping.models.User
import org.shopping.services.{ListService, MainService, ProductService, UserService}
import org.shopping.util.Gen._
import org.shopping.util.Time._
import org.specs2.execute.AsResult
import org.specs2.mock.Mockito
import play.api.db.DBApi
import play.api.db.evolutions._
import play.api.db.slick._
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.inject.{BindingKey, QualifierInstance}
import play.api.test.PlaySpecification
import play.api.{Application, Mode}
import slick.backend.DatabaseConfig
import slick.dbio.{DBIOAction, NoStream}
import slick.driver.JdbcProfile
import slick.jdbc.JdbcBackend

import scala.concurrent.duration._
import scala.concurrent.{Future, _}

class BaseRepoSpec extends PlaySpecification with Mockito {

  case class TestEnv(app: Application
                     , dbConfigProvider: DatabaseConfigProvider
                     , dbConfig: DatabaseConfig[JdbcProfile]
                     , db: JdbcBackend#DatabaseDef)

  val duration = Duration.Inf

  def test[T: AsResult](t: TestEnv => T) = try {
    println("Building environment")
    //    val dbRandom = Gen.guid
    val dbRandom = ""
    val app = guiceApp(dbRandom).build()
    running(app) {
      val inj = app.injector
      val qualifier = Some(QualifierInstance(new play.db.NamedDatabaseImpl("default")))
      val bindingKey = BindingKey[DatabaseConfigProvider](classOf[DatabaseConfigProvider], qualifier)
      val dbConfigProvider = inj.instanceOf[DatabaseConfigProvider](bindingKey)
      val dbConfig = dbConfigProvider.get[JdbcProfile]
      val db = dbConfig.db
      val e = TestEnv(app, dbConfigProvider, dbConfig, db)

      val database = inj.instanceOf[DBApi].database("default")
      try {
        Evolutions.applyEvolutions(database)
        AsResult(t(e))
      } catch {
        case e: Throwable =>
          failure(e.getMessage)
      } finally {
        Evolutions.cleanupEvolutions(database)
      }
    }
  } catch {
    case e: Exception =>
      failure(e.getMessage)
  }

  def waitFor[T](f: Future[T]): T = Await.result(f, duration)

  class TestModule extends AbstractModule {
    override def configure() = {
      bind(classOf[ProductService]).toInstance(mock[ProductService])
      bind(classOf[ListService]).toInstance(mock[ListService])
      bind(classOf[UserService]).toInstance(mock[UserService])
      bind(classOf[MainService]).toInstance(mock[MainService])
      bind(classOf[Oauth2Repo]).toInstance(mock[Oauth2Repo])
    }
  }

  def guiceApp(dbName: String, module: AbstractModule = new TestModule) =
    new GuiceApplicationBuilder()
      .disable[RunModule]
      .bindings(module)
      .configure(
        Map(
          "evolutions" -> "disabled",
          "slick.dbs.default.driver" -> "slick.driver.PostgresDriver$",
          "slick.dbs.default.db.driver" -> "org.postgresql.Driver",
          "slick.dbs.default.db.url" -> s"jdbc:postgresql://localhost:5432/mytest",
          "slick.dbs.default.db.user" -> "postgres",
          "slick.dbs.default.db.password" -> "root"
        ))
      .in(Mode.Test)

  def dbSync[R](a: DBIOAction[R, NoStream, Nothing])(implicit dbConfig: DatabaseConfig[JdbcProfile]): R = {
    waitFor(dbConfig.db.run(a))
  }

  def newUser = User(id = guid, login = guid, providerToken = None, created = now, updated = now, lastLogin = None, password = guid, nick = guid)

  def genString(size: Int): String = (for (i <- 1 to size) yield "a").mkString

}
