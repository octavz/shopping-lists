package org.shopping.controllers

import controllers.Assets
import play.api.mvc.{Action, Controller}
import scalaoauth2.provider.OAuth2Provider

class MainController extends Controller {

  def build = Action { _ => Ok("build")}

  def indexUser(user: String) = Action.async {
    implicit request =>
      Assets.at("/public/client", "index.html")(request)
  }

  def indexProject(user: String, project: String) = Action.async {
    implicit request =>
      Assets.at("/public/client", "index.html")(request)
    //Ok(views.html.users.index.render())
  }

  def options(path: String) = Action {
    Ok("")
  }
}
