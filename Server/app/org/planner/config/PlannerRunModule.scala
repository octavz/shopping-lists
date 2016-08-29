package org.planner.config

import com.google.inject.AbstractModule
import org.planner.dal.{Caching, UserDAL, ProjectDAL, Oauth2DAL}

import org.planner.modules.core._
import org.planner.modules.core.impl._
import org.planner.dal.impl._

class PlannerRunModule extends AbstractModule {
  def configure() = {
    bind(classOf[Oauth2DAL]).to(classOf[SlickOauth2DAL])
    bind(classOf[ProjectDAL]).to(classOf[SlickProjectDAL])
    bind(classOf[UserDAL]).to(classOf[SlickUserDAL])
    bind(classOf[Caching]).to(classOf[RedisCaching])
    bind(classOf[ProjectModule]).to(classOf[DefaultProjectModule])
    bind(classOf[UserModule]).to(classOf[DefaultUserModule])
  }

}
