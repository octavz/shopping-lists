package org.shopping.db

import org.shopping.models._

object DB {

  def apply(p: slick.driver.JdbcProfile): DB = new DB {
    override val profile = p
  }
}

trait DB {

  import slick.model.ForeignKeyAction

  val profile: slick.driver.JdbcProfile

  import profile.api._

  class AccessTokens(_tableTag: Tag) extends Table[AccessToken](_tableTag, "access_tokens") {
    def * = (accessToken, refreshToken, userId, scope, expiresIn, created, clientId) <> (AccessToken.tupled, AccessToken.unapply)

    val accessToken = column[String]("access_token", O.PrimaryKey, O.Length(254, varying = true))
    val refreshToken = column[Option[String]]("refresh_token", O.Length(254, varying = true), O.Default(None))
    val userId = column[String]("user_id")
    val scope = column[Option[String]]("scope", O.Length(254, varying = true), O.Default(None))
    val expiresIn = column[Int]("expires_in")
    val created = column[Long]("created")
    val clientId = column[String]("client_id")

    lazy val clientsFk = foreignKey("access_tokens_client_id_fkey", clientId, Clients)(
      r => r.id, onUpdate = ForeignKeyAction.NoAction, onDelete = ForeignKeyAction.NoAction)
    lazy val usersFk = foreignKey("access_tokens_user_id_fkey", userId, Users)(
      r => r.id, onUpdate = ForeignKeyAction.NoAction, onDelete = ForeignKeyAction.NoAction)
  }

  class AuthCodes(_tableTag: Tag) extends Table[AuthCode](_tableTag, "auth_codes") {
    def * = (authorizationCode, userId, redirectUri, createdAt, scope, clientId, expiresIn) <> (AuthCode.tupled, AuthCode.unapply)

    val authorizationCode = column[String]("authorization_code", O.PrimaryKey, O.Length(254, varying = true))
    val userId = column[String]("user_id")
    val redirectUri = column[Option[String]]("redirect_uri", O.Length(254, varying = true), O.Default(None))
    val createdAt = column[Long]("created_at")
    val scope = column[Option[String]]("scope", O.Length(254, varying = true), O.Default(None))
    val clientId = column[String]("client_id", O.Length(254, varying = true))
    val expiresIn = column[Int]("expires_in")

    lazy val clientsFk = foreignKey("auth_codes_client_id_fkey", clientId, Clients)(
      r => r.id, onUpdate = ForeignKeyAction.NoAction, onDelete = ForeignKeyAction.NoAction)
    lazy val usersFk = foreignKey("auth_codes_user_id_fkey", userId, Users)(
      r => r.id, onUpdate = ForeignKeyAction.NoAction, onDelete = ForeignKeyAction.NoAction)
  }

  class ClientGrantTypes(
    _tableTag: Tag) extends Table[ClientGrantType](_tableTag, "client_grant_types") {
    def * = (clientId, grantTypeId) <> (ClientGrantType.tupled, ClientGrantType.unapply)

    val clientId = column[String]("client_id", O.Length(254, varying = true))
    val grantTypeId = column[Int]("grant_type_id")

    val pk = primaryKey("client_grant_types_pkey", (clientId, grantTypeId))

    lazy val clientsFk = foreignKey("client_grant_types_client_id_fkey", clientId, Clients)(
      r => r.id, onUpdate = ForeignKeyAction.NoAction, onDelete = ForeignKeyAction.NoAction)
    lazy val grantTypesFk = foreignKey("client_grant_types_grant_type_id_fkey", grantTypeId, GrantTypes)(
      r => r.id, onUpdate = ForeignKeyAction.NoAction, onDelete = ForeignKeyAction.NoAction)
  }

  class Clients(_tableTag: Tag) extends Table[Client](_tableTag, "clients") {
    def * = (id, secret, redirectUri, scope) <> (Client.tupled, Client.unapply)

    val id = column[String]("id", O.PrimaryKey, O.Length(254, varying = true))
    val secret = column[Option[String]]("secret", O.Length(254, varying = true), O.Default(None))
    val redirectUri = column[Option[String]]("redirect_uri", O.Length(254, varying = true), O.Default(None))
    val scope = column[Option[String]]("scope", O.Length(254, varying = true), O.Default(None))
  }

  class GrantTypes(_tableTag: Tag) extends Table[GrantType](_tableTag, "grant_types") {
    def * = (id, grantType) <> (GrantType.tupled, GrantType.unapply)

    val id = column[Int]("id", O.PrimaryKey)
    val grantType = column[String]("grant_type", O.Length(254, varying = true))
  }

  class ListDefs(_tableTag: Tag) extends Table[ListDef](_tableTag, "list_defs") {
    def * = (id, userId, name, description, status, createdClient, created, updated) <> (ListDef.tupled, ListDef.unapply)

    val id = column[String]("id", O.PrimaryKey, O.Length(40, varying = true))
    val userId = column[String]("user_id", O.Length(40, varying = true))
    val name = column[String]("name", O.Length(255, varying = true))
    val description = column[Option[String]]("description", O.Default(None))
    val status = column[Short]("status", O.Default(0))
    val createdClient = column[Long]("created_client")
    val created = column[Long]("created")
    val updated = column[Long]("updated")
  }

  class Products(_tableTag: Tag) extends Table[Product](_tableTag, "products") {
    def * = (id, userId, name, description, status, created, updated) <> (Product.tupled, Product.unapply)

    val id = column[String]("id", O.PrimaryKey, O.Length(40, varying = true))
    val userId = column[String]("user_id", O.Length(40, varying = true))
    val name = column[String]("name", O.Length(255, varying = true))
    val description = column[Option[String]]("description", O.Default(None))
    val status = column[Short]("status", O.Default(0))
    val created = column[Long]("created")
    val updated = column[Long]("updated")

    lazy val usersFk = foreignKey("tasks_user_id_fkey", userId, Users)(
      r => r.id, onUpdate = ForeignKeyAction.NoAction, onDelete = ForeignKeyAction.NoAction)
  }

  class Users(_tableTag: Tag) extends Table[User](_tableTag, "users") {
    def * = (id, login, nick, provider, providerToken, lastLogin, status, password, created, updated) <> (User.tupled, User.unapply)

    val id = column[String]("id", O.PrimaryKey, O.Length(40, varying = true))
    val login = column[String]("login", O.Length(255, varying = true))
    val nick = column[String]("nick", O.Length(255, varying = true))
    val provider = column[Int]("provider", O.Default(0))
    val providerToken = column[Option[String]]("provider_token", O.Length(255, varying = true), O.Default(None))
    val lastLogin = column[Option[Long]]("last_login", O.Default(None))
    val status = column[Int]("status", O.Default(0))
    val password = column[String]("password", O.Length(100, varying = true), O.Default(""))
    val created = column[Long]("created")
    val updated = column[Long]("updated")

    val index1 = index("users_login_key", login, unique = true)
    val index2 = index("users_nick_key", nick, unique = true)
  }

  class UserSessions(_tableTag: Tag) extends Table[UserSession](_tableTag, "user_sessions") {
    def * = (id, userId, created, updated) <> (UserSession.tupled, UserSession.unapply)

    val id = column[String]("id", O.PrimaryKey, O.Length(254, varying = true))
    val userId = column[String]("user_id")
    val created = column[Long]("created")
    val updated = column[Long]("updated")

    lazy val usersFk = foreignKey("user_sessions_user_id_fkey", userId, Users)(
      r => r.id, onUpdate = ForeignKeyAction.NoAction, onDelete = ForeignKeyAction.NoAction)
  }

  class UserStatuses(_tableTag: Tag) extends Table[UserStatus](_tableTag, "user_statuses") {
    def * = (id, description) <> (UserStatus.tupled, UserStatus.unapply)

    val id = column[Int]("id", O.PrimaryKey)
    val description = column[Option[String]]("description", O.Length(100, varying = true), O.Default(None))
  }

  class ListDefProducts(_tableTag: Tag) extends Table[ListDefProduct](_tableTag, "list_def_products") {
    def * = (listDefId, productId, description, bought, quantity, created, updated) <> (ListDefProduct.tupled, ListDefProduct.unapply)

    val listDefId = column[String]("list_def_id", O.PrimaryKey, O.Length(40, varying = true))
    val productId = column[String]("product_id", O.PrimaryKey, O.Length(40, varying = true))

    val quantity = column[Int]("quantity", O.Default(0))
    val created = column[Long]("created")
    val updated = column[Long]("updated")
    val description = column[Option[String]]("description", O.Length(100, varying = true), O.Default(None))
    val bought = column[Short]("status", O.Default(0))

    lazy val listsFk = foreignKey("list_products_list_def_id_fkey", listDefId, ListDefs)(
      r => r.id, onUpdate = ForeignKeyAction.NoAction, onDelete = ForeignKeyAction.NoAction)
    lazy val productsFk = foreignKey("list_products_product_id_fkey", productId, Products)(
      r => r.id, onUpdate = ForeignKeyAction.NoAction, onDelete = ForeignKeyAction.NoAction)
  }

  class ListsUsers(_tableTag: Tag) extends Table[ListUser](_tableTag, "lists_users") {
    def * = (listDefId, userId) <> (ListUser.tupled, ListUser.unapply)

    val listDefId = column[String]("list_def_id", O.PrimaryKey)
    val userId = column[String]("user_id", O.PrimaryKey)

    lazy val usersFk = foreignKey("lists_user_user_id_fkey", userId, Users)(
      r => r.id, onUpdate = ForeignKeyAction.NoAction, onDelete = ForeignKeyAction.NoAction)
    lazy val listsFk = foreignKey("lists_user_list_def_id_fkey", listDefId, ListDefs)(
      r => r.id, onUpdate = ForeignKeyAction.NoAction, onDelete = ForeignKeyAction.NoAction)
  }

  class Suppliers(_tableTag: Tag) extends Table[Supplier](_tableTag, "suppliers") {
    def * = (id, name, description, created, updated) <> (Supplier.tupled, Supplier.unapply)

    val id = column[String]("id", O.PrimaryKey)
    val name = column[String]("name", O.Length(255, varying = true))
    val description = column[Option[String]]("description", O.Length(1000, varying = true), O.Default(None))
    val created = column[Long]("created")
    val updated = column[Long]("updated")
  }

  class ProductPrices(_tableTag: Tag) extends Table[ProductPrice](_tableTag, "product_price") {
    def * = (userId, productId, supplierId, price, created, updated) <> (ProductPrice.tupled, ProductPrice.unapply)

    val userId = column[String]("user_id")
    val productId = column[String]("product_id", O.PrimaryKey, O.Length(40, varying = true))
    val supplierId = column[String]("id", O.PrimaryKey, O.Length(40, varying = true))
    val price = column[BigDecimal]("price", O.Default(0))
    val created = column[Long]("created")
    val updated = column[Long]("updated")
  }

  lazy val ClientGrantTypes = new TableQuery(tag => new ClientGrantTypes(tag))
  lazy val AccessTokens = new TableQuery(tag => new AccessTokens(tag))
  lazy val AuthCodes = new TableQuery(tag => new AuthCodes(tag))
  lazy val Clients = new TableQuery(tag => new Clients(tag))
  lazy val GrantTypes = new TableQuery(tag => new GrantTypes(tag))
  lazy val ListDefs = new TableQuery(tag => new ListDefs(tag))
  lazy val Products = new TableQuery(tag => new Products(tag))
  lazy val Users = new TableQuery(tag => new Users(tag))
  lazy val UserSessions = new TableQuery(tag => new UserSessions(tag))
  lazy val UserStatuses = new TableQuery(tag => new UserStatuses(tag))
  lazy val ListDefProducts = new TableQuery(tag => new ListDefProducts(tag))
  lazy val ListsUsers = new TableQuery(tag => new ListsUsers(tag))
  lazy val ProductPrices = new TableQuery(tag => new ProductPrices(tag))
  lazy val Suppliers = new TableQuery(tag => new Suppliers(tag))
}


