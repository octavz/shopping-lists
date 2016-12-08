package org.shopping.repo

import com.google.inject.AbstractModule
import org.shopping.BaseSpec
import org.shopping.config.RunModule
import org.shopping.services.{ListService, MainService, ProductService, UserService}
import play.api.db.DBApi
import play.api.db.evolutions._
import play.api.db.slick._
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.inject.{BindingKey, QualifierInstance}
import play.api.{Application, Mode}
import slick.backend.DatabaseConfig
import slick.dbio.{DBIOAction, NoStream}
import slick.driver.JdbcProfile
import slick.jdbc.JdbcBackend
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.duration._

class BaseRepoSpec extends BaseSpec {

  case class TestEnv(app: Application
                     , dbConfigProvider: DatabaseConfigProvider
                     , dbConfig: DatabaseConfig[JdbcProfile]
                     , db: JdbcBackend#DatabaseDef)

  val duration = Duration.Inf

  def test(t: TestEnv => Unit) = try {
//    println("Building environment")
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
        t(e)
      } catch {
        case e: Throwable => throw e
      } finally {
        Evolutions.cleanupEvolutions(database)
      }
    }
  } catch {
    case e: Exception =>
      throw e
  }

  class TestModule extends AbstractModule {
    override def configure(): Unit = {
      bind(classOf[ProductService]).toInstance(mock[ProductService])
      bind(classOf[ListService]).toInstance(mock[ListService])
      bind(classOf[UserService]).toInstance(mock[UserService])
      bind(classOf[MainService]).toInstance(mock[MainService])
      bind(classOf[Oauth2Repo]).toInstance(mock[Oauth2Repo])
    }
  }

  def guiceApp(dbName: String, module: AbstractModule = new TestModule): GuiceApplicationBuilder =
    new GuiceApplicationBuilder()
      .disable[RunModule]
      .bindings(module)
      .configure(testConf)
      .in(Mode.Test)

  def dbSync[R](a: DBIOAction[R, NoStream, Nothing])(implicit dbConfig: DatabaseConfig[JdbcProfile]): R = {
    waitFor(dbConfig.db.run(a))
  }

  def genString(size: Int): String = "a" * size

}
