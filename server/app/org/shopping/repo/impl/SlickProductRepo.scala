package org.shopping.repo.impl

import com.google.inject.Inject
import org.shopping.db._
import org.shopping.models._
import org.shopping.repo._
import org.shopping.util.Constants
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class SlickProductRepo @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
  extends ProductRepo
    with DB
    with HasDatabaseConfigProvider[JdbcProfile] {
  val profile = dbConfig.driver

  import dbConfig.driver.api._

  override def insertProduct(model: Product): Repo[Product] = model.touch2 { m =>
    db.run(Products += m).map(_ => m)
  }

  override def insertProducts(model: Seq[Product]): Repo[Seq[Product]] =
    if (model.isEmpty) {
      Future.successful(Nil)
    } else {
      val newModel = model.map(touch2(_))
      db.run(Products ++= newModel).map(_ => newModel)
    }

  override def updateProduct(model: Product): Repo[Product] = model.touch1 { m =>
    db.run(Products.filter(_.id === model.id).update(m)).map(_ => m)
  }

  override def getProductById(id: String): Repo[Option[Product]] =
    db.run {
      Products.filter(_.id === id).result.headOption
    }

  override def insertSupplier(model: Supplier): Repo[Supplier] = model.touch2 { m =>
    db.run(Suppliers += model).map(_ => model)
  }

  override def insertProductPrice(model: ProductPrice): Repo[ProductPrice] = model.touch2 { m =>
    db.run(ProductPrices += m).map(_ => m)
  }

  override def searchProduct(name: String, offset: Int, count: Int): Repo[(Seq[Product], Int)] = db.run {
    val q = Products.filter(p => (p.tags like s"%${name.toLowerCase()}%") && (p.status =!= Constants.STATUS_DELETE))
    for {
      res <- q.drop(offset).take(count).result
      total <- q.length.result
    } yield (res, total)
  }

  override def getModifiedProductsSince(since: Long): Repo[(Seq[Product], Int)] = db.run {
    val q = Products.filter(p => (p.updated < since) && (p.status =!= Constants.STATUS_DELETE))
    for {
      res <- q.result
      total <- q.length.result
    } yield (res, total)
  }

  override def getProductPrice(productId: String, supplierId: String): Repo[Option[ProductPrice]] = {
    db.run {
      ProductPrices.filter(p => p.supplierId === supplierId && p.productId === productId).result.headOption
    }
  }

  override def updateProductPrice(model: ProductPrice): Repo[ProductPrice] = model.touch1 {
    m =>
      db.run {
        ProductPrices
          .filter(p => p.productId === m.productId && p.supplierId === m.supplierId)
          .update(m)
          .map(_ => m)
      }
  }

  override def getAllSuppliers: Repo[Seq[Supplier]] =
    db.run(Suppliers.result)
}
