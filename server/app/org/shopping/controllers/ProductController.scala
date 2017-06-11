package org.shopping.controllers

import com.google.inject.Inject
import org.shopping.dto._
import org.shopping.services.{ListService, ProductService}
import play.api.libs.json.JsResultException
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global

class ProductController @Inject()(productService: ProductService) extends BaseController(productService) {

  def insertProduct(): Action[AnyContent] = Action.async {
    implicit request =>
      request.body.asJson.map {
        json =>
          try {
            authorize {
              implicit authInfo =>
                try {
                  val dto = json.as[ProductDTO]
                  productService.insertProduct(dto) map (response(_))
                } catch {
                  case je: JsResultException => asyncBadRequest(je.errors.mkString(","))
                }
            }
          } catch {
            case e: Throwable => asyncBadRequest(e)
          }
      }.getOrElse(asyncBadRequest("Bad Json"))
  }

  def insertSupplier(): Action[AnyContent] = Action.async {
    implicit request =>
      request.body.asJson.map {
        json =>
          try {
            authorize {
              implicit authInfo =>
                try {
                  val dto = json.as[SupplierDTO]
                  productService.insertSupplier(dto) map (response(_))
                } catch {
                  case je: JsResultException => asyncBadRequest(je.errors.mkString(","))
                }
            }
          } catch {
            case e: Throwable => asyncBadRequest(e)
          }
      }.getOrElse(asyncBadRequest("Bad Json"))
  }

  def insertProductPrice(): Action[AnyContent] = Action.async {
    implicit request =>
      request.body.asJson.map {
        json =>
          try {
            authorize {
              implicit authInfo =>
                try {
                  val dto = json.as[ProductPriceDTO]
                  productService.insertProductPrice(dto) map (response(_))
                } catch {
                  case je: JsResultException => asyncBadRequest(je.errors.mkString(","))
                }
            }
          } catch {
            case e: Throwable => asyncBadRequest(e)
          }
      }.getOrElse(asyncBadRequest("Bad Json"))
  }

  def updateProduct(id: String): Action[AnyContent] = Action.async {
    implicit request =>
      request.body.asJson.map {
        json =>
          try {
            authorize {
              implicit authInfo =>
                try {
                  val dto = json.as[ProductDTO].copy(id = Some(id))
                  productService.updateProduct(dto) map (response(_))
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

  def deleteProduct(listId: String): Action[AnyContent] = Action.async {
    implicit request =>
      authorize {
        implicit authInfo =>
          productService.deleteProduct(listId) map (response(_))
      }
  }

  def updateProductPrice(): Action[AnyContent] = Action.async {
    implicit request =>
      request.body.asJson.map {
        json =>
          try {
            authorize {
              implicit authInfo =>
                try {
                  val dto = json.as[ProductPriceDTO]
                  productService.updateProductPrice(dto) map (response(_))
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


  def getAllSuppliers(): Action[AnyContent] =
    Action.async {
      implicit request =>
        try {
          authorize {
            implicit authInfo =>
              productService.getAllSuppliers map (response(_))
          }
        } catch {
          case e: Throwable => asyncBadRequest(e)
        }

    }

  def searchProducts(q: String, offset: Int, count: Int): Action[AnyContent] =
    Action.async {
      implicit request =>
        try {
          authorize {
            implicit authInfo =>
              productService.searchProduct(q, offset, count) map (response(_))
          }
        } catch {
          case e: Throwable => asyncBadRequest(e)
        }
    }

  def syncProducts(since: Long): Action[AnyContent] =
    Action.async {
      implicit request =>
        try {
          productService.syncUserProducts(since)(None) map (response(_))
        } catch {
          case e: Throwable => asyncBadRequest(e)
        }
    }

  def syncUserProducts(since: Long): Action[AnyContent] =
    Action.async {
      implicit request =>
        try {
          authorize {
            implicit authInfo =>
              productService.syncUserProducts(since)(Some(authInfo)) map (response(_))
          }
        } catch {
          case e: Throwable => asyncBadRequest(e)
        }
    }
}
