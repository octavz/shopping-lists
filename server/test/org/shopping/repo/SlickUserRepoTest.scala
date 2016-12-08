package org.shopping.repo

import org.shopping.models._
import org.shopping.repo.impl.{SlickUserRepo, TestCaching}
import org.shopping.util.Gen._

import scala.concurrent._
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

class SlickUserRepoTest extends BaseRepoSpec {

  "User Repo" should {

    "insertSession, findSessionById, deleteSessionByUser" in {
      test { env =>
        val dao = new SlickUserRepo(env.dbConfigProvider, new TestCaching)
        val us = UserSession(id = guid, userId = "1")
        Await.ready(dao.insertSession(us), Duration.Inf)
        val ret = Await.result(dao.findSessionById(us.id), Duration.Inf)
        assert(ret.isDefined)
        ret.get.id === us.id
        ret.get.userId === us.userId
        val retDelete = Await.result(dao.deleteSessionByUser(us.userId), Duration.Inf)
        retDelete === 1
      }
    }

    "insertUser,getUserById, getUserByEmail" in {
      test { env =>
        val dao = new SlickUserRepo(env.dbConfigProvider, new TestCaching)
        val usr = anUser
        Await.ready(dao.insertUser(usr), Duration.Inf)
        val ret = Await.result(dao.getUserById(usr.id), Duration.Inf)
        ret.get === usr
        val ret1 = Await.result(dao.getUserByEmail(usr.login), Duration.Inf)
        ret1.get === usr
      }
    }

  }
}


