package org.shopping.config

import com.google.inject.AbstractModule
import org.shopping.dal.{Caching, UserDAL, ListDAL, Oauth2DAL}

import org.shopping.modules.core._
import org.shopping.modules.core.impl._
import org.shopping.dal.impl._

class RunModule extends AbstractModule {
  def configure() = {
    bind(classOf[Oauth2DAL]).to(classOf[SlickOauth2DAL])
    bind(classOf[ListDAL]).to(classOf[SlickListDAL])
    bind(classOf[UserDAL]).to(classOf[SlickUserDAL])
    bind(classOf[Caching]).to(classOf[RedisCaching])
    bind(classOf[ListModule]).to(classOf[DefaultProjectModule])
    bind(classOf[UserModule]).to(classOf[DefaultUserModule])
  }

}
