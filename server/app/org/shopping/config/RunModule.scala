package org.shopping.config

import com.google.inject.AbstractModule
import org.shopping.repo.impl._
import org.shopping.repo._
import org.shopping.services.impl._
import org.shopping.services._

class RunModule extends AbstractModule {
  def configure() = {
    bind(classOf[Oauth2Repo]).to(classOf[SlickOauth2Repo])
    bind(classOf[ListRepo]).to(classOf[SlickListRepo])
    bind(classOf[UserRepo]).to(classOf[SlickUserRepo])
    bind(classOf[ProductRepo]).to(classOf[SlickProductRepo])
    bind(classOf[ListService]).to(classOf[DefaultListService])
    bind(classOf[UserService]).to(classOf[DefaultUserService])
    bind(classOf[ProductService]).to(classOf[DefaultProductService])
    bind(classOf[MainService]).to(classOf[DefaultMainService])
    bind(classOf[Caching]).to(classOf[TestCaching])
  }

}
