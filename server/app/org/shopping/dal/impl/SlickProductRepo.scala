package org.shopping.dal.impl

import com.google.inject.Inject
import org.shopping.dal._
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

  override def insertProduct(model: Product): DAL[Product] = {
    db.run(Products += model).map(_ => model)
  }

  override def updateProduct(model: Product): DAL[Product] = {
    val newModel = model.copy(updated = now())
    db.run(Products.filter(_.id === model.id).update(newModel)) map (_ => model)
  }

  override def getProductById(id: String): DAL[Option[Product]] =
    db.run {
      Products.filter(_.id === id).take(1).result.headOption
    }

  override def insertSupplier(model: Supplier): DAL[Supplier] = {
    db.run(Suppliers += model).map(_ => model)
  }

  override def insertProductPrice(model: ProductPrice): DAL[ProductPrice] = {
    db.run(ProductPrices += model).map(_ => model)

  }
}
