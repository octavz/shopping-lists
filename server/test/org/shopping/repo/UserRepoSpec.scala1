package org.shopping.repo

import org.junit.runner._
import org.shopping.repo.impl.{SlickUserRepo, TestCaching}
import org.shopping.models.{User, UserSession}
import org.shopping.util.Gen._
import org.specs2.runner._

import scala.concurrent._
import scala.concurrent.duration._

@RunWith(classOf[JUnitRunner])
class UserRepoSpec extends BaseRepoSpec {

  "User Repo" should {

    "insertSession, findSessionById, deleteSessionByUser" in {
      test { env =>
        val dao = new SlickUserRepo(env.dbConfigProvider, new TestCaching)
        val us = UserSession(id = guid, userId = "1")
        Await.ready(dao.insertSession(us), Duration.Inf)
        val ret = Await.result(dao.findSessionById(us.id), Duration.Inf)
        ret must beSome
        ret.get.id === us.id
        ret.get.userId === us.userId
        val retDelete = Await.result(dao.deleteSessionByUser(us.userId), Duration.Inf)
        retDelete === 1
      }
    }

    "insertUser,getUserById, getUserByEmail" in {
      test { env =>
        val dao = new SlickUserRepo(env.dbConfigProvider, new TestCaching)
        val usr = newUser
        Await.ready(dao.insertUser(usr), Duration.Inf)
        val ret = Await.result(dao.getUserById(usr.id), Duration.Inf)
        ret.get === usr
        val ret1 = Await.result(dao.getUserByEmail(usr.login), Duration.Inf)
        ret1.get === usr
      }
    }

  }
}


