package org.shopping

import org.shopping.util.Time._

package object models {

  import scala.language.higherKinds

  trait BaseModel {
    val created: Long
    val updated: Long
    type T <: BaseModel

    def insert[S[_]](c: T => S[T]): S[T] = {
      val n = now()
      c(copyFn2(n, n))
    }

    def update[S[_]](c: T => S[T]): S[T] = c(copyFn1(now()))

    def copyFn1(a: Long): T

    def copyFn2(a: Long, b: Long): T

  }

  case class AccessToken(accessToken: String, refreshToken: Option[String] = None, userId: String,
    scope: Option[String] = None, expiresIn: Int, override val created: Long = 0, clientId: String) extends BaseModel {
    override val updated = 0L
    type T = AccessToken

    override def copyFn1(a: Long): T = this

    override def copyFn2(a: Long, b: Long): T = copy(created = a)
  }

  case class Action(id: String, description: Option[String] = None, url: String, verbId: Int,
    secured: Short, userId: String, groupId: String, perm: Int = 448,
    override val created: Long = 0,
    override val updated: Long = 0) extends BaseModel {
    type T = Action

    override def copyFn1(a: Long): T = copy(updated = a)

    override def copyFn2(a: Long, b: Long): T = copy(created = a, updated = b)
  }

  case class AuthCode(authorizationCode: String, userId: String, redirectUri: Option[String] = None,
    override val created: Long = 0, scope: Option[String] = None, clientId: String, expiresIn: Int) extends BaseModel {
    override val updated = 0L
    type T = AuthCode

    override def copyFn1(a: Long): T = this

    override def copyFn2(a: Long, b: Long): T = copy(created = a)
  }

  case class Client(id: String, secret: Option[String] = None, redirectUri: Option[String] = None,
    scope: Option[String] = None)

  case class ClientGrantType(clientId: String, grantTypeId: Int)

  case class GrantType(id: Int, grantType: String)

  case class ListDef(id: String, userId: String, name: String, description: Option[String] = None,
    status: Short = 0, createdClient: Long = 0,
    override val created: Long = 0,
    override val updated: Long = 0) extends BaseModel {
    type T = ListDef

    override def copyFn1(a: Long): T = copy(updated = a)

    override def copyFn2(a: Long, b: Long): T = copy(created = a, updated = b)
  }

  case class ListDefProduct(listDefId: String, productId: String, description: Option[String],
    bought: Short = 0, quantity: Int = 0,
    override val created: Long = 0,
    override val updated: Long = 0) extends BaseModel {
    type T = ListDefProduct

    override def copyFn1(a: Long): T = copy(updated = a)

    override def copyFn2(a: Long, b: Long): T = copy(created = a, updated = b)
  }

  case class ListUser(listDefId: String, userId: String)

  case class Product(id: String, userId: String, name: String, description: Option[String] = None,
    status: Short = 0,
    override val created: Long = 0,
    override val updated: Long = 0) extends BaseModel {
    type T = Product

    override def copyFn1(a: Long): T = copy(updated = a)

    override def copyFn2(a: Long, b: Long): T = copy(created = a, updated = b)
  }


  case class User(id: String, login: String, nick: String, provider: Int = 0,
    providerToken: Option[String] = None, lastLogin: Option[Long] = None,
    status: Int = 0, password: String = "",
    override val created: Long = 0,
    override val updated: Long = 0) extends BaseModel {
    type T = User

    override def copyFn1(a: Long): T = copy(updated = a)

    override def copyFn2(a: Long, b: Long): T = copy(created = a, updated = b)
  }

  case class UserSession(id: String, userId: String,
    override val created: Long = 0,
    override val updated: Long = 0) extends BaseModel {
    type T = UserSession

    override def copyFn1(a: Long): T = copy(updated = a)

    override def copyFn2(a: Long, b: Long): T = copy(created = a, updated = b)
  }

  case class UserStatus(id: Int, description: Option[String] = None)

  case class Supplier(id: String, name: String, description: Option[String] = None,
    override val created: Long = 0,
    override val updated: Long = 0) extends BaseModel {
    type T = Supplier

    override def copyFn1(a: Long): T = copy(updated = a)

    override def copyFn2(a: Long, b: Long): T = copy(created = a, updated = b)
  }

  case class ProductPrice(userId: String, productId: String, supplierId: String, price: BigDecimal,
    override val created: Long = 0, override val updated: Long = 0) extends BaseModel {
    type T = ProductPrice

    override def copyFn1(a: Long): T = copy(updated = a)

    override def copyFn2(a: Long, b: Long): T = copy(created = a, updated = b)
  }

}
