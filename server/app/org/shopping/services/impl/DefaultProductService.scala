package org.shopping.services.impl

import com.google.inject.Inject
import org.shopping.repo._
import org.shopping.dto._
import org.shopping.services.{ProductService, _}
import org.shopping.util.{Constants, ErrorMessages, Gen}
import play.api.http.Status

import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent._

class DefaultProductService @Inject()(dalUser: UserRepo, dalProduct: ProductRepo) extends ProductService {

  override def insertProduct(dto: ProductDTO): Result[ProductDTO] = {
    val model = dto.toModel(userId, Gen.guid)
    dalProduct
      .insertProduct(model)
      .map(p => resultSync(new ProductDTO(model)))
      .recover { case e: Throwable => exSync(e) }
  }

  override def updateProduct(dto: ProductDTO): Result[ProductDTO] = {
    val f = if (dto.id.isEmpty) error(401 -> ErrorMessages.EMPTY_ID)
    else checkUser(valid(dto.id.get)) {
      dalProduct.getProductById(dto.id.get) flatMap {
        case None => error(Status.NOT_FOUND -> ErrorMessages.NOT_FOUND)
        case Some(existing) =>
          val newProduct = existing.copy(name = dto.name, description = dto.description)
          dalProduct.updateProduct(newProduct) map { p =>
            resultSync(new ProductDTO(p))
          }
      }
    }

    f recover { case e: Throwable => exSync(e) }
  }

  private def valid(id: String): Future[Boolean] = Future.successful(true) //check if the current user is allowed to do this

  private def valid(): Future[Boolean] = Future.successful(true) //check if the current user is allowed to do this

  override def deleteProduct(productId: String): Result[BooleanDTO] =
    checkUser(valid(productId)) {
      dalProduct.getProductById(productId) flatMap {
        case Some(product) =>
          val newProduct = product.copy(status = Constants.STATUS_DELETE)
          dalProduct.updateProduct(newProduct) map {
            _ =>
              resultSync(BooleanDTO(true))
          }
        case None =>
          error(Status.NOT_FOUND -> ErrorMessages.NOT_FOUND)
      }
    } recover {
      case e: Throwable => exSync(e)
    }

  override def insertSupplier(dto: SupplierDTO): Result[SupplierDTO] = checkUser(valid()) {
    dalProduct
      .insertSupplier(dto.toModel(Gen.guid))
      .map(a => resultSync(new SupplierDTO(a)))
      .recover {
        case e: Throwable => exSync(e)
      }
  }

  override def insertProductPrice(dto: ProductPriceDTO): Result[ProductPriceDTO] = checkUser(valid()) {
    dalProduct
      .insertProductPrice(dto.toModel(Gen.guid))
      .map(a => resultSync(new ProductPriceDTO(a)))
      .recover {
        case e: Throwable => exSync(e)
      }
  }

  override def updateProductPrice(dto: ProductPriceDTO): Result[ProductPriceDTO] = checkUser(valid()) {
    dalProduct.getProductPrice(dto.productId, dto.supplierId) flatMap {
      case Some(pp) =>
        val newModel = pp.copy(price = dto.price)
        dalProduct.updateProductPrice(newModel).map(r => resultSync(new ProductPriceDTO(r)))
      case _ =>
        error(Status.NOT_FOUND -> ErrorMessages.NOT_FOUND)
    }
  }

  override def getAllSuppliers: Result[SuppliersDTO] = checkUser(valid()) {
    dalProduct
      .getAllSuppliers
      .map(all => resultSync(SuppliersDTO(all.map(new SupplierDTO(_)))))
  }

}
