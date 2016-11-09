package org.shopping

import scala.concurrent.Future

package object repo {

  type Repo[T] = Future[T]

  def repo[T](v: T): Repo[T] = Future.successful(v)

  def repoErr[T](error: String): Repo[T] = Future.failed(new Exception(error))



  object CacheKeys {
    def session(id: String): String = s"session:$id"

    def user(id: String): String = s"user:$id"

    def userGroupsIds(id: String): String = s"userGroupIds:$id"

    def userGroups(id: String): String = s"userGroup:$id"

    def byEmail(email: String): String = s"user:email:$email"

  }

}

