package org.shopping.controllers

import com.google.inject.Inject
import org.shopping.dto._
import org.shopping.services.ProductService
import play.api.libs.json.JsResultException
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global

class ProductController @Inject()(listModule: ProductService) extends BaseController(listModule) {

  def insertProduct() = Action.async {
    implicit request =>
      request.body.asJson.map {
        json => try {
          authorize {
            implicit authInfo =>
              try {
                val dto = json.as[ProductDTO]
                listModule.insertProduct(dto) map (response(_))
              } catch {
                case je: JsResultException => asyncBadRequest(je.errors.mkString(","))
              }
          }
        } catch {
          case e: Throwable => asyncBadRequest(e)
        }
      }.getOrElse(asyncBadRequest(new Exception("Bad Json")))
  }

  def updateProduct(id: String) = Action.async {
    implicit request =>
      request.body.asJson.map {
        json => try {
          authorize {
            implicit authInfo =>
              try {
                val dto = json.as[ProductDTO].copy(id = Some(id))
                listModule.updateProduct(dto) map (response(_))
              } catch {
                case je: JsResultException => asyncBadRequest(je.errors.mkString(","))
                case e: Throwable => asyncBadRequest(e)
              }
          }
        } catch {
          case e: Throwable => asyncBadRequest(e)
        }
      }.getOrElse(asyncBadRequest("Bad Json"))
  }

  def deleteProduct(listId: String) =
    Action.async {
      implicit request =>
        authorize {
          implicit authInfo =>
            listModule.deleteProduct(listId) map (response(_))
        }
    }


}
