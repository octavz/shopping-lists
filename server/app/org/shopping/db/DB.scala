package org.shopping.db

import java.sql.Timestamp

object DB {

  def apply(p: slick.driver.JdbcProfile): DB = new DB {
    override val profile = p
  }
}

trait DB {

  import slick.jdbc.{GetResult => GR}
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
    val created = column[java.sql.Timestamp]("created")
    val clientId = column[String]("client_id")

    lazy val clientsFk = foreignKey("access_tokens_client_id_fkey", clientId, Clients)(r => r.id, onUpdate = ForeignKeyAction.NoAction, onDelete = ForeignKeyAction.NoAction)
    lazy val usersFk = foreignKey("access_tokens_user_id_fkey", userId, Users)(r => r.id, onUpdate = ForeignKeyAction.NoAction, onDelete = ForeignKeyAction.NoAction)
  }

  lazy val AccessTokens = new TableQuery(tag => new AccessTokens(tag))

  class AuthCodes(_tableTag: Tag) extends Table[AuthCode](_tableTag, "auth_codes") {
    def * = (authorizationCode, userId, redirectUri, createdAt, scope, clientId, expiresIn) <> (AuthCode.tupled, AuthCode.unapply)

    val authorizationCode = column[String]("authorization_code", O.PrimaryKey, O.Length(254, varying = true))
    val userId = column[String]("user_id")
    val redirectUri = column[Option[String]]("redirect_uri", O.Length(254, varying = true), O.Default(None))
    val createdAt = column[java.sql.Timestamp]("created_at")
    val scope = column[Option[String]]("scope", O.Length(254, varying = true), O.Default(None))
    val clientId = column[String]("client_id", O.Length(254, varying = true))
    val expiresIn = column[Int]("expires_in")

    lazy val clientsFk = foreignKey("auth_codes_client_id_fkey", clientId, Clients)(r => r.id, onUpdate = ForeignKeyAction.NoAction, onDelete = ForeignKeyAction.NoAction)
    lazy val usersFk = foreignKey("auth_codes_user_id_fkey", userId, Users)(r => r.id, onUpdate = ForeignKeyAction.NoAction, onDelete = ForeignKeyAction.NoAction)
  }

  lazy val AuthCodes = new TableQuery(tag => new AuthCodes(tag))

  class ClientGrantTypes(_tableTag: Tag) extends Table[ClientGrantType](_tableTag, "client_grant_types") {
    def * = (clientId, grantTypeId) <> (ClientGrantType.tupled, ClientGrantType.unapply)

    val clientId = column[String]("client_id", O.Length(254, varying = true))
    val grantTypeId = column[Int]("grant_type_id")

    val pk = primaryKey("client_grant_types_pkey", (clientId, grantTypeId))

    lazy val clientsFk = foreignKey("client_grant_types_client_id_fkey", clientId, Clients)(r => r.id, onUpdate = ForeignKeyAction.NoAction, onDelete = ForeignKeyAction.NoAction)
    lazy val grantTypesFk = foreignKey("client_grant_types_grant_type_id_fkey", grantTypeId, GrantTypes)(r => r.id, onUpdate = ForeignKeyAction.NoAction, onDelete = ForeignKeyAction.NoAction)
  }

  lazy val ClientGrantTypes = new TableQuery(tag => new ClientGrantTypes(tag))

  class Clients(_tableTag: Tag) extends Table[Client](_tableTag, "clients") {
    def * = (id, secret, redirectUri, scope) <> (Client.tupled, Client.unapply)

    val id = column[String]("id", O.PrimaryKey, O.Length(254, varying = true))
    val secret = column[Option[String]]("secret", O.Length(254, varying = true), O.Default(None))
    val redirectUri = column[Option[String]]("redirect_uri", O.Length(254, varying = true), O.Default(None))
    val scope = column[Option[String]]("scope", O.Length(254, varying = true), O.Default(None))
  }

  lazy val Clients = new TableQuery(tag => new Clients(tag))

  class EntityTypes(_tableTag: Tag) extends Table[EntityType](_tableTag, "entity_types") {
    def * = (id, description) <> (EntityType.tupled, EntityType.unapply)

    val id = column[String]("id", O.PrimaryKey, O.Length(40, varying = true))
    val description = column[Option[String]]("description", O.Length(100, varying = true), O.Default(None))
  }

  lazy val EntityTypes = new TableQuery(tag => new EntityTypes(tag))

  class GrantTypes(_tableTag: Tag) extends Table[GrantType](_tableTag, "grant_types") {
    def * = (id, grantType) <> (GrantType.tupled, GrantType.unapply)

    val id = column[Int]("id", O.PrimaryKey)
    val grantType = column[String]("grant_type", O.Length(254, varying = true))
  }

  lazy val GrantTypes = new TableQuery(tag => new GrantTypes(tag))

  class Labels(_tableTag: Tag) extends Table[Label](_tableTag, "labels") {
    def * = (id, lang, entityId, entityTypeId, label1, label2, label3) <> (Label.tupled, Label.unapply)

    val id = column[String]("id", O.PrimaryKey, O.Length(40, varying = true))
    val lang = column[String]("lang", O.Length(10, varying = true))
    val entityId = column[String]("entity_id", O.Length(40, varying = true))
    val entityTypeId = column[String]("entity_type_id", O.Length(40, varying = true))
    val label1 = column[Option[String]]("label1", O.Length(1000, varying = true), O.Default(None))
    val label2 = column[Option[String]]("label2", O.Length(1000, varying = true), O.Default(None))
    val label3 = column[Option[String]]("label3", O.Length(1000, varying = true), O.Default(None))
  }

  lazy val Labels = new TableQuery(tag => new Labels(tag))

  class Lists(_tableTag: Tag) extends Table[List](_tableTag, "lists") {
    def * = (id, userId, name, description, status, created, updated, createdClient) <> (List.tupled, List.unapply)

    val id = column[String]("id", O.PrimaryKey, O.Length(40, varying = true))
    val userId = column[String]("user_id", O.Length(40, varying = true))
    val name = column[String]("name", O.Length(255, varying = true))
    val description = column[Option[String]]("description", O.Default(None))
    val status = column[Short]("status", O.Default(0))
    val created = column[java.sql.Timestamp]("created")
    val updated = column[java.sql.Timestamp]("updated")
    val createdClient = column[java.sql.Timestamp]("created")

    val index1 = index("projects_user_id_name_key", (userId, name), unique = true)
  }

  lazy val Lists = new TableQuery(tag => new Lists(tag))

  class Products(_tableTag: Tag) extends Table[Product](_tableTag, "products") {
    def * = (id, userId, subject, description, status, created, updated) <> (Product.tupled, Product.unapply)

    val id = column[String]("id", O.PrimaryKey, O.Length(40, varying = true))
    val userId = column[String]("user_id", O.Length(40, varying = true))
    val subject = column[String]("subject", O.Length(255, varying = true))
    val description = column[Option[String]]("description", O.Default(None))
    val status = column[Short]("status", O.Default(0))
    val created = column[java.sql.Timestamp]("created")
    val updated = column[java.sql.Timestamp]("updated")

    lazy val usersFk = foreignKey("tasks_user_id_fkey", userId, Users)(r => r.id, onUpdate = ForeignKeyAction.NoAction, onDelete = ForeignKeyAction.NoAction)
  }

  lazy val Products = new TableQuery(tag => new Products(tag))

  class Users(_tableTag: Tag) extends Table[User](_tableTag, "users") {
    def * = (id, login, nick, provider, providerToken, lastLogin, status, password, created, updated) <> (User.tupled, User.unapply)

    val id = column[String]("id", O.PrimaryKey, O.Length(40, varying = true))
    val login = column[String]("login", O.Length(255, varying = true))
    val nick = column[String]("nick", O.Length(255, varying = true))
    val provider = column[Int]("provider", O.Default(0))
    val providerToken = column[Option[String]]("provider_token", O.Length(255, varying = true), O.Default(None))
    val lastLogin = column[Option[java.sql.Timestamp]]("last_login", O.Default(None))
    val status = column[Int]("status", O.Default(0))
    val password = column[String]("password", O.Length(100, varying = true), O.Default(""))
    val created = column[java.sql.Timestamp]("created")
    val updated = column[java.sql.Timestamp]("updated")

    val index1 = index("users_login_key", login, unique = true)
    val index2 = index("users_nick_key", nick, unique = true)
  }

  lazy val Users = new TableQuery(tag => new Users(tag))

  class UserSessions(_tableTag: Tag) extends Table[UserSession](_tableTag, "user_sessions") {
    def * = (id, userId) <> (UserSession.tupled, UserSession.unapply)

    val id = column[String]("id", O.PrimaryKey, O.Length(254, varying = true))
    val userId = column[String]("user_id")

    lazy val usersFk = foreignKey("user_sessions_user_id_fkey", userId, Users)(r => r.id, onUpdate = ForeignKeyAction.NoAction, onDelete = ForeignKeyAction.NoAction)
  }

  lazy val UserSessions = new TableQuery(tag => new UserSessions(tag))

  class UserStatuses(_tableTag: Tag) extends Table[UserStatus](_tableTag, "user_statuses") {
    def * = (id, description) <> (UserStatus.tupled, UserStatus.unapply)

    val id = column[Int]("id", O.PrimaryKey)
    val description = column[Option[String]]("description", O.Length(100, varying = true), O.Default(None))
  }

  lazy val UserStatuses = new TableQuery(tag => new UserStatuses(tag))

  class ListProducts(_tableTag: Tag) extends Table[ListItem](_tableTag, "list_products") {
    def * = (listId, productId, userId, description, status, quantity, created, updated) <> (ListItem.tupled, ListItem.unapply)

    val listId = column[String]("list_id", O.PrimaryKey, O.Length(40, varying = true))
    val productId = column[String]("product_id", O.PrimaryKey, O.Length(40, varying = true))

    val userId = column[String]("user_id", O.Length(40, varying = true))
    val description = column[Option[String]]("description", O.Default(None))
    val status = column[Short]("status", O.Default(0))
    val quantity = column[Int]("quantity", O.Default(0))
    val created = column[java.sql.Timestamp]("created")
    val updated = column[java.sql.Timestamp]("updated")

    lazy val usersFk = foreignKey("list_products_user_id_fkey", userId, Users)(r => r.id, onUpdate = ForeignKeyAction.NoAction, onDelete = ForeignKeyAction.NoAction)
    lazy val listsFk = foreignKey("list_products_list_id_fkey", listId, Lists)(r => r.id, onUpdate = ForeignKeyAction.NoAction, onDelete = ForeignKeyAction.NoAction)
    lazy val productsFk = foreignKey("list_products_product_id_fkey", productId, Products)(r => r.id, onUpdate = ForeignKeyAction.NoAction, onDelete = ForeignKeyAction.NoAction)
  }

  lazy val ListProducts = new TableQuery(tag => new ListProducts(tag))

  class ListsUsers(_tableTag: Tag) extends Table[ListUser](_tableTag, "lists_users") {
    def * = (listId, userId) <> (ListUser.tupled, ListUser.unapply)

    val listId = column[String]("list_id", O.PrimaryKey)
    val userId = column[String]("user_id", O.PrimaryKey)

    lazy val usersFk = foreignKey("lists_user_user_id_fkey", userId, Users)(r => r.id, onUpdate = ForeignKeyAction.NoAction, onDelete = ForeignKeyAction.NoAction)
    lazy val listsFk = foreignKey("lists_user_list_id_fkey", listId, Lists)(r => r.id, onUpdate = ForeignKeyAction.NoAction, onDelete = ForeignKeyAction.NoAction)
  }

  lazy val ListsUsers = new TableQuery(tag => new ListsUsers(tag))
}

case class AccessToken(accessToken: String, refreshToken: Option[String] = None, userId: String, scope: Option[String] = None, expiresIn: Int, created: java.sql.Timestamp, clientId: String)

case class Action(id: String, description: Option[String] = None, url: String, verbId: Int, secured: Short, userId: String, groupId: String, perm: Int = 448, created: java.sql.Timestamp, updated: java.sql.Timestamp)

case class AuthCode(authorizationCode: String, userId: String, redirectUri: Option[String] = None, createdAt: java.sql.Timestamp, scope: Option[String] = None, clientId: String, expiresIn: Int)

case class ClientGrantType(clientId: String, grantTypeId: Int)

case class Client(id: String, secret: Option[String] = None, redirectUri: Option[String] = None, scope: Option[String] = None)

case class EntityType(id: String, description: Option[String] = None)

case class GrantType(id: Int, grantType: String)

case class Label(id: String, lang: String, entityId: String, entityTypeId: String, label1: Option[String] = None, label2: Option[String] = None, label3: Option[String] = None)

case class List(id: String, userId: String, name: String, description: Option[String] = None, status: Short = 0, created: java.sql.Timestamp, updated: java.sql.Timestamp, createdClient: Timestamp)

case class Product(id: String, userId: String, subject: String, description: Option[String] = None, status: Short = 0, created: java.sql.Timestamp, updated: java.sql.Timestamp)

case class ListItem(listId: String, productId: String, userId: String, description: Option[String], status: Short = 0, quantity: Int, created: java.sql.Timestamp, updated: java.sql.Timestamp)

case class User(id: String, login: String, nick: String, provider: Int = 0, providerToken: Option[String] = None, lastLogin: Option[java.sql.Timestamp] = None, status: Int = 0, password: String = "", created: java.sql.Timestamp, updated: java.sql.Timestamp)

case class UserSession(id: String, userId: String)

case class UserStatus(id: Int, description: Option[String] = None)

case class ListUser(listId: String, userId: String)

