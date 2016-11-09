package org.shopping.repo.impl

import com.google.inject.Inject
import org.shopping.repo._
import org.shopping.db._
import org.shopping.models.{Product, ProductPrice, Supplier}
import org.shopping.util.Time._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile

import scala.concurrent.ExecutionContext.Implicits.global

class SlickProductRepo @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
  extends ProductRepo
    with DB
    with HasDatabaseConfigProvider[JdbcProfile] {
  val profile = dbConfig.driver

  import dbConfig.driver.api._

  override def insertProduct(model: Product): Repo[Product] = model.insert { m =>
    db.run(Products += m).map(_ => m)
  }

  override def insertProducts(model: Seq[Product]): Repo[Seq[Product]] = {
    val n = now()
    val newModel = model.map(_.copy(created = n, updated = n))
    db.run(Products ++= newModel).map(_ => newModel)
  }

  override def updateProduct(model: Product): Repo[Product] = model.update { m =>
    db.run(Products.filter(_.id === model.id).update(m)) map (_ => m)
  }

  override def getProductById(id: String): Repo[Option[Product]] =
    db.run {
      Products.filter(_.id === id).result.headOption
    }

  override def insertSupplier(model: Supplier): Repo[Supplier] = model.insert { m =>
    db.run(Suppliers += model).map(_ => model)
  }

  override def insertProductPrice(model: ProductPrice): Repo[ProductPrice] = model.insert { m =>
    db.run(ProductPrices += m).map(_ => m)
  }

  override def searchProduct(name: String): Repo[Seq[Product]] = {
    db.run(Products.filter(_.name like "%name%").result)
  }

  override def getProductPrice(productId: String, supplierId: String): Repo[Option[ProductPrice]] = {
    db.run {
      ProductPrices.filter(p => p.supplierId === supplierId && p.productId === productId).result.headOption
    }
  }

  override def updateProductPrice(model: ProductPrice): Repo[ProductPrice] = model.update { m =>
    db.run {
      ProductPrices
        .filter(p => p.productId === m.productId && p.supplierId === m.supplierId)
        .update(m)
        .map(_ => m)
    }
  }

  override def getAllSuppliers: Repo[Seq[Supplier]] = db.run(Suppliers.result)
}
