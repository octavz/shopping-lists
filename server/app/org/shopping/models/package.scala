package org.shopping

package object models {

  case class AccessToken(accessToken: String, refreshToken: Option[String] = None, userId: String,
    scope: Option[String] = None, expiresIn: Int, created: Long, clientId: String)

  case class Action(id: String, description: Option[String] = None, url: String, verbId: Int,
    secured: Short, userId: String, groupId: String, perm: Int = 448, created: Long, updated: Long)

  case class AuthCode(authorizationCode: String, userId: String, redirectUri: Option[String] = None,
    created: Long, scope: Option[String] = None, clientId: String, expiresIn: Int)

  case class Client(id: String, secret: Option[String] = None, redirectUri: Option[String] = None,
    scope: Option[String] = None)

  case class ClientGrantType(clientId: String, grantTypeId: Int)

  case class GrantType(id: Int, grantType: String)

  case class ListDef(id: String, userId: String, name: String, description: Option[String] = None,
    status: Short = 0, createdClient: Long, created: Long, updated: Long)

  case class ListDefProduct(listDefId: String, productId: String, description: Option[String],
    bought: Short = 0, quantity: Int = 0, created: Long, updated: Long)

  case class ListUser(listDefId: String, userId: String)

  case class Product(id: String, userId: String, name: String, description: Option[String] = None,
    status: Short = 0, created: Long, updated: Long)

  case class User(id: String, login: String, nick: String, provider: Int = 0,
    providerToken: Option[String] = None, lastLogin: Option[Long] = None,
    status: Int = 0, password: String = "", created: Long, updated: Long)

  case class UserSession(id: String, userId: String)

  case class UserStatus(id: Int, description: Option[String] = None)

  case class Supplier(id: String, name: String, description: Option[String] = None)

  case class ProductPrice(userId: String, productId: String, supplierId: String, price: BigDecimal)

}
