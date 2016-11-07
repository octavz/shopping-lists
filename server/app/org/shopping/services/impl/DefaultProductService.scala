package org.shopping.services.impl

import com.google.inject.Inject
import org.shopping.dal._
import org.shopping.dto._
import org.shopping.services.{ProductService, _}
import org.shopping.util.{Constants, Gen, Time}
import play.api.http.Status

import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent._

class DefaultProductService @Inject()(dalUser: UserRepo, dalProduct: ProductRepo) extends ProductService {

  override def insertProduct(dto: ProductDTO): Result[ProductDTO] = {
    val model = dto.toModel(userId, Gen.guid).copy(created = Time.now(), updated = Time.now())
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
          val newProduct = existing.copy(name = dto.name, description = dto.description, updated = Time.now())
          dalProduct.updateProduct(newProduct) map { p =>
            resultSync(new ProductDTO(p))
          }
      }
    }

    f recover { case e: Throwable => resultExSync(e, "updateProduct") }
  }

  private def valid(id: String): Future[Boolean] = Future.successful(true)

  private def valid(): Future[Boolean] = Future.successful(true)

  override def deleteProduct(productId: String): Result[BooleanDTO] =
    checkUser(valid(productId)) {
      dalProduct.getProductById(productId) flatMap {
        case Some(product) =>
          val newProduct = product.copy(status = Constants.STATUS_DELETE, updated = Time.now())
          dalProduct.updateProduct(newProduct) map {
            _ =>
              resultSync(BooleanDTO(true))
          }
        case None =>
          resultError(Status.NOT_FOUND, "Product not found")
      }
    } recover {
      case e: Throwable => resultExSync(e, "deleteProduct")
    }

  override def insertSupplier(dto: SupplierDTO): Result[SupplierDTO] = checkUser(valid()) {
    val n = Time.now
    dalProduct
      .insertSupplier(dto.toModel(Gen.guid).copy(created = n, updated = n))
      .map(a => resultSync(new SupplierDTO(a)))
      .recover {
        case e: Throwable =>
          resultExSync(e, "insertSupplier")
      }
  }

  override def insertProductPrice(dto: ProductPriceDTO): Result[ProductPriceDTO] = checkUser(valid()) {
    val n = Time.now()
    dalProduct
      .insertProductPrice(dto.toModel(Gen.guid).copy(created = n, updated = n))
      .map(a => resultSync(new ProductPriceDTO(a)))
      .recover {
        case e: Throwable =>
          resultExSync(e, "insertProductPrice")
      }
  }

  override def updateProductPrice(dto: ProductPriceDTO): Result[ProductPriceDTO] = checkUser(valid()) {
    dalProduct.getProductPrice(dto.productId, dto.supplierId) flatMap {
      case Some(pp) =>
        val newModel = pp.copy(price = dto.price, updated = Time.now)
        dalProduct.updateProductPrice(newModel).map(r => resultSync(new ProductPriceDTO(r)))
      case _ =>
        resultError(Status.NOT_FOUND, "ProductPrice not found")
    }
  }

  override def getAllSuppliers: Result[SuppliersDTO] = checkUser(valid()) {
    dalProduct
      .getAllSuppliers
      .map(all => resultSync(SuppliersDTO(all.map(new SupplierDTO(_)))))
  }

}
