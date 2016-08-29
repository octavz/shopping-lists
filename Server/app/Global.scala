import org.planner.config._
import org.planner.controllers.{ProjectController, UserController}

import play.api._
import play.api.mvc._

object Global extends WithFilters(CORSFilter()) {

  override def onStart(app: Application) {
    Logger.info("Application is launched...")
  }

  override def onStop(app: Application) {
    Logger.info("Application is shutting down!")
  }

//  override def getControllerInstance[A](clazz: Class[A]) = clazz match {
//    case c if c.isAssignableFrom(classOf[UserController]) => Runtime.userController.asInstanceOf[A]
//    case c if c.isAssignableFrom(classOf[ProjectController]) => Runtime.projectController.asInstanceOf[A]
//    case _ => super.getControllerInstance(clazz)
//  }

}
