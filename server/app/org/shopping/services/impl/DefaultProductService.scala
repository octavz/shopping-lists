package org.shopping.services.impl

import com.google.inject.Inject
import org.shopping.dal._
import org.shopping.dto._
import org.shopping.services.{ProductService, _}
import org.shopping.util.{Constants, ErrorMessages, Gen, Time}
import play.api.http.Status

import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent._

class DefaultProductService @Inject()(dalUser: UserRepo,
  dalProduct: ProductRepo) extends ProductService {

  override def insertProduct(dto: ProductDTO): Result[ProductDTO] = {
    val model = dto.toModel(userId)

    dalProduct
      .insertProduct(model)
      .map(p => resultSync(new ProductDTO(model)))
      .recover { case e: Throwable => resultExSync(e, "insertProduct") }
  }

  //  def createIfNotExists(productId: String): Future[Product] =
  //    dalProduct.getProductById(productId) flatMap {
  //      case Some(list) =>
  //        val uid = authData.user.id
  //        if (list.userId != userId) {
  //          val ts = Time.now()
  //          dalProduct.insertProduct(list.copy(id = Gen.guid, userId = uid, created = ts, createdClient = ts, updated = ts))
  //        } else Future.successful(list)
  //      case _ => throw new Exception("I couldn't find the list you look for.")
  //    }

  override def updateProduct(dto: ProductDTO): Result[ProductDTO] = {
    val f = if (dto.id.isEmpty) Future.failed(new Exception("Product has empty id"))
    else checkUser(valid(dto.id.get)) {
      dalProduct.getProductById(dto.id.get) flatMap {
        case None => resultError(Status.NOT_FOUND, "Product not found")
        case Some(existing) =>
          val newProduct = existing.copy(name = dto.name, description = dto.description)
          dalProduct.updateProduct(newProduct) map { p =>
            resultSync(new ProductDTO(p))
          }
      }
    }

    f recover { case e: Throwable => resultExSync(e, "updateProduct") }
  }

  private def valid(id: String): Future[Boolean] = Future.successful(true)

  override def deleteProduct(productId: String): Result[BooleanDTO] =
    checkUser(valid(productId)) {
      dalProduct.getProductById(productId) flatMap {
        case Some(list) =>
          val newProduct = list.copy(status = Constants.STATUS_DELETE)
          dalProduct.updateProduct(newProduct) map {
            x =>
              resultSync(BooleanDTO(true))
          }
        case None =>
          resultError(Status.NOT_FOUND, "Product not found")
      }
    } recover {
      case e: Throwable => resultExSync(e, "deleteProduct")
    }

}
