import com.google.inject.AbstractModule
import org.shopping.config.RunModule
import org.shopping.dal.Oauth2DAL
import org.shopping.db._
import org.shopping.modules.core.{ListService, UserService}
import org.shopping.util.Gen
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

class BaseDALSpec extends PlaySpecification with Mockito {

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
      val qualifier = Some(QualifierInstance(new play.db.NamedDatabaseImpl("test")))
      val bindingKey = BindingKey[DatabaseConfigProvider](classOf[DatabaseConfigProvider], qualifier)
      val dbConfigProvider = inj.instanceOf[DatabaseConfigProvider](bindingKey)
      val dbConfig = dbConfigProvider.get[JdbcProfile]
      val db = dbConfig.db
      val e = TestEnv(app, dbConfigProvider, dbConfig, db)

      val database = inj.instanceOf[DBApi].database("test")
      Evolutions.applyEvolutions(database)
      try {
        AsResult(t(e))
      } catch {
        case e: Throwable =>
          e.printStackTrace()
          failure(e.getMessage)
      } finally {
        Evolutions.cleanupEvolutions(database)
      }
    }
  } catch {
    case e: Exception =>
      e.printStackTrace()
      failure(e.getMessage)
  }

  def waitFor[T](f: Future[T]): T = Await.result(f, duration)

  class TestModule extends AbstractModule {
    override def configure() = {
      bind(classOf[ListService]).toInstance(mock[ListService])
      bind(classOf[UserService]).toInstance(mock[UserService])
      bind(classOf[Oauth2DAL]).toInstance(mock[Oauth2DAL])
    }
  }

  def guiceApp(dbName: String, module: AbstractModule = new TestModule) =
    new GuiceApplicationBuilder()
      .disable[RunModule]
      .bindings(module)
      .configure(
        Map(
          "evolutions" -> "disabled",
          "slick.dbs.test.driver" -> "slick.driver.PostgresDriver$",
          "slick.dbs.test.db.driver" -> "org.postgresql.Driver",
          "slick.dbs.test.db.url" -> s"jdbc:postgresql://localhost:5432/mytest",
          "slick.dbs.test.db.user" -> "postgres",
          "slick.dbs.test.db.password" -> ""
        ))
      .in(Mode.Test)

  def dbSync[R](a: DBIOAction[R, NoStream, Nothing])(implicit dbConfig: DatabaseConfig[JdbcProfile]): R = {
    waitFor(dbConfig.db.run(a))
  }

  def newUser = User(id = guid, login = guid, providerToken = None, created = now, updated = now, lastLogin = None, password = guid, nick = guid)

  def randProject(uid: String) = List(id = guid, userId = uid, name = guid, description = guido, created = now, updated = now)

  def genString(size: Int): String = (for (i <- 1 to size) yield "a").mkString

}
