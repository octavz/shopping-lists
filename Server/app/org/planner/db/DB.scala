package org.planner.db


// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
//object DB extends {
//  val profile = slick.driver.H2Driver
//} with DB

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait DB {
  val profile: slick.driver.JdbcProfile
  import profile.api._
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
  lazy val schema = Array(AccessTokens.schema, Actions.schema, AuthCodes.schema, ClientGrantTypes.schema, Clients.schema, EntityTypes.schema, GrantTypes.schema, Groups.schema, GroupsUsers.schema, Labels.schema, Projects.schema, Resources.schema, Tasks.schema, Users.schema, UserSessions.schema, UserStatuses.schema, Verbs.schema).reduceLeft(_ ++ _)
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** GetResult implicit for fetching AccessToken objects using plain SQL queries */
  implicit def GetResultAccessToken(implicit e0: GR[String], e1: GR[Option[String]], e2: GR[Int], e3: GR[java.sql.Timestamp]): GR[AccessToken] = GR{
    prs => import prs._
    AccessToken.tupled((<<[String], <<?[String], <<[String], <<?[String], <<[Int], <<[java.sql.Timestamp], <<[String]))
  }
  /** Table description of table access_tokens. Objects of this class serve as prototypes for rows in queries. */
  class AccessTokens(_tableTag: Tag) extends Table[AccessToken](_tableTag, "access_tokens") {
    def * = (accessToken, refreshToken, userId, scope, expiresIn, created, clientId) <> (AccessToken.tupled, AccessToken.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(accessToken), refreshToken, Rep.Some(userId), scope, Rep.Some(expiresIn), Rep.Some(created), Rep.Some(clientId)).shaped.<>({r=>import r._; _1.map(_=> AccessToken.tupled((_1.get, _2, _3.get, _4, _5.get, _6.get, _7.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column access_token SqlType(varchar), PrimaryKey, Length(254,true) */
    val accessToken: Rep[String] = column[String]("access_token", O.PrimaryKey, O.Length(254,varying=true))
    /** Database column refresh_token SqlType(varchar), Length(254,true), Default(None) */
    val refreshToken: Rep[Option[String]] = column[Option[String]]("refresh_token", O.Length(254,varying=true), O.Default(None))
    /** Database column user_id SqlType(varchar) */
    val userId: Rep[String] = column[String]("user_id")
    /** Database column scope SqlType(varchar), Length(254,true), Default(None) */
    val scope: Rep[Option[String]] = column[Option[String]]("scope", O.Length(254,varying=true), O.Default(None))
    /** Database column expires_in SqlType(int4) */
    val expiresIn: Rep[Int] = column[Int]("expires_in")
    /** Database column created SqlType(timestamp) */
    val created: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("created")
    /** Database column client_id SqlType(varchar) */
    val clientId: Rep[String] = column[String]("client_id")

    /** Foreign key referencing Clients (database name access_tokens_client_id_fkey) */
    lazy val clientsFk = foreignKey("access_tokens_client_id_fkey", clientId, Clients)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing Users (database name access_tokens_user_id_fkey) */
    lazy val usersFk = foreignKey("access_tokens_user_id_fkey", userId, Users)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table AccessTokens */
  lazy val AccessTokens = new TableQuery(tag => new AccessTokens(tag))

  /** GetResult implicit for fetching Action objects using plain SQL queries */
  implicit def GetResultAction(implicit e0: GR[String], e1: GR[Option[String]], e2: GR[Int], e3: GR[Short], e4: GR[java.sql.Timestamp]): GR[Action] = GR{
    prs => import prs._
    Action.tupled((<<[String], <<?[String], <<[String], <<[Int], <<[Short], <<[String], <<[String], <<[Int], <<[java.sql.Timestamp], <<[java.sql.Timestamp]))
  }
  /** Table description of table actions. Objects of this class serve as prototypes for rows in queries. */
  class Actions(_tableTag: Tag) extends Table[Action](_tableTag, "actions") {
    def * = (id, description, url, verbId, secured, userId, groupId, perm, created, updated) <> (Action.tupled, Action.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), description, Rep.Some(url), Rep.Some(verbId), Rep.Some(secured), Rep.Some(userId), Rep.Some(groupId), Rep.Some(perm), Rep.Some(created), Rep.Some(updated)).shaped.<>({r=>import r._; _1.map(_=> Action.tupled((_1.get, _2, _3.get, _4.get, _5.get, _6.get, _7.get, _8.get, _9.get, _10.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(varchar), PrimaryKey, Length(40,true) */
    val id: Rep[String] = column[String]("id", O.PrimaryKey, O.Length(40,varying=true))
    /** Database column description SqlType(varchar), Length(100,true), Default(None) */
    val description: Rep[Option[String]] = column[Option[String]]("description", O.Length(100,varying=true), O.Default(None))
    /** Database column url SqlType(varchar), Length(2000,true) */
    val url: Rep[String] = column[String]("url", O.Length(2000,varying=true))
    /** Database column verb_id SqlType(int4) */
    val verbId: Rep[Int] = column[Int]("verb_id")
    /** Database column secured SqlType(int2) */
    val secured: Rep[Short] = column[Short]("secured")
    /** Database column user_id SqlType(varchar), Length(40,true) */
    val userId: Rep[String] = column[String]("user_id", O.Length(40,varying=true))
    /** Database column group_id SqlType(varchar), Length(40,true) */
    val groupId: Rep[String] = column[String]("group_id", O.Length(40,varying=true))
    /** Database column perm SqlType(int4), Default(448) */
    val perm: Rep[Int] = column[Int]("perm", O.Default(448))
    /** Database column created SqlType(timestamp) */
    val created: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("created")
    /** Database column updated SqlType(timestamp) */
    val updated: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("updated")

    /** Foreign key referencing Groups (database name actions_group_id_fkey) */
    lazy val groupsFk = foreignKey("actions_group_id_fkey", groupId, Groups)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing Users (database name actions_user_id_fkey) */
    lazy val usersFk = foreignKey("actions_user_id_fkey", userId, Users)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing Verbs (database name actions_verb_id_fkey) */
    lazy val verbsFk = foreignKey("actions_verb_id_fkey", verbId, Verbs)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)

    /** Uniqueness Index over (url,verbId) (database name actions_url_verb_id_key) */
    val index1 = index("actions_url_verb_id_key", (url, verbId), unique=true)
  }
  /** Collection-like TableQuery object for table Actions */
  lazy val Actions = new TableQuery(tag => new Actions(tag))

  /** GetResult implicit for fetching AuthCode objects using plain SQL queries */
  implicit def GetResultAuthCode(implicit e0: GR[String], e1: GR[Option[String]], e2: GR[java.sql.Timestamp], e3: GR[Int]): GR[AuthCode] = GR{
    prs => import prs._
    AuthCode.tupled((<<[String], <<[String], <<?[String], <<[java.sql.Timestamp], <<?[String], <<[String], <<[Int]))
  }
  /** Table description of table auth_codes. Objects of this class serve as prototypes for rows in queries. */
  class AuthCodes(_tableTag: Tag) extends Table[AuthCode](_tableTag, "auth_codes") {
    def * = (authorizationCode, userId, redirectUri, createdAt, scope, clientId, expiresIn) <> (AuthCode.tupled, AuthCode.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(authorizationCode), Rep.Some(userId), redirectUri, Rep.Some(createdAt), scope, Rep.Some(clientId), Rep.Some(expiresIn)).shaped.<>({r=>import r._; _1.map(_=> AuthCode.tupled((_1.get, _2.get, _3, _4.get, _5, _6.get, _7.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column authorization_code SqlType(varchar), PrimaryKey, Length(254,true) */
    val authorizationCode: Rep[String] = column[String]("authorization_code", O.PrimaryKey, O.Length(254,varying=true))
    /** Database column user_id SqlType(varchar) */
    val userId: Rep[String] = column[String]("user_id")
    /** Database column redirect_uri SqlType(varchar), Length(254,true), Default(None) */
    val redirectUri: Rep[Option[String]] = column[Option[String]]("redirect_uri", O.Length(254,varying=true), O.Default(None))
    /** Database column created_at SqlType(timestamp) */
    val createdAt: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("created_at")
    /** Database column scope SqlType(varchar), Length(254,true), Default(None) */
    val scope: Rep[Option[String]] = column[Option[String]]("scope", O.Length(254,varying=true), O.Default(None))
    /** Database column client_id SqlType(varchar), Length(254,true) */
    val clientId: Rep[String] = column[String]("client_id", O.Length(254,varying=true))
    /** Database column expires_in SqlType(int4) */
    val expiresIn: Rep[Int] = column[Int]("expires_in")

    /** Foreign key referencing Clients (database name auth_codes_client_id_fkey) */
    lazy val clientsFk = foreignKey("auth_codes_client_id_fkey", clientId, Clients)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing Users (database name auth_codes_user_id_fkey) */
    lazy val usersFk = foreignKey("auth_codes_user_id_fkey", userId, Users)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table AuthCodes */
  lazy val AuthCodes = new TableQuery(tag => new AuthCodes(tag))

  /** GetResult implicit for fetching ClientGrantType objects using plain SQL queries */
  implicit def GetResultClientGrantType(implicit e0: GR[String], e1: GR[Int]): GR[ClientGrantType] = GR{
    prs => import prs._
    ClientGrantType.tupled((<<[String], <<[Int]))
  }
  /** Table description of table client_grant_types. Objects of this class serve as prototypes for rows in queries. */
  class ClientGrantTypes(_tableTag: Tag) extends Table[ClientGrantType](_tableTag, "client_grant_types") {
    def * = (clientId, grantTypeId) <> (ClientGrantType.tupled, ClientGrantType.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(clientId), Rep.Some(grantTypeId)).shaped.<>({r=>import r._; _1.map(_=> ClientGrantType.tupled((_1.get, _2.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column client_id SqlType(varchar), Length(254,true) */
    val clientId: Rep[String] = column[String]("client_id", O.Length(254,varying=true))
    /** Database column grant_type_id SqlType(int4) */
    val grantTypeId: Rep[Int] = column[Int]("grant_type_id")

    /** Primary key of ClientGrantTypes (database name client_grant_types_pkey) */
    val pk = primaryKey("client_grant_types_pkey", (clientId, grantTypeId))

    /** Foreign key referencing Clients (database name client_grant_types_client_id_fkey) */
    lazy val clientsFk = foreignKey("client_grant_types_client_id_fkey", clientId, Clients)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing GrantTypes (database name client_grant_types_grant_type_id_fkey) */
    lazy val grantTypesFk = foreignKey("client_grant_types_grant_type_id_fkey", grantTypeId, GrantTypes)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table ClientGrantTypes */
  lazy val ClientGrantTypes = new TableQuery(tag => new ClientGrantTypes(tag))

  /** GetResult implicit for fetching Client objects using plain SQL queries */
  implicit def GetResultClient(implicit e0: GR[String], e1: GR[Option[String]]): GR[Client] = GR{
    prs => import prs._
    Client.tupled((<<[String], <<?[String], <<?[String], <<?[String]))
  }
  /** Table description of table clients. Objects of this class serve as prototypes for rows in queries. */
  class Clients(_tableTag: Tag) extends Table[Client](_tableTag, "clients") {
    def * = (id, secret, redirectUri, scope) <> (Client.tupled, Client.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), secret, redirectUri, scope).shaped.<>({r=>import r._; _1.map(_=> Client.tupled((_1.get, _2, _3, _4)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(varchar), PrimaryKey, Length(254,true) */
    val id: Rep[String] = column[String]("id", O.PrimaryKey, O.Length(254,varying=true))
    /** Database column secret SqlType(varchar), Length(254,true), Default(None) */
    val secret: Rep[Option[String]] = column[Option[String]]("secret", O.Length(254,varying=true), O.Default(None))
    /** Database column redirect_uri SqlType(varchar), Length(254,true), Default(None) */
    val redirectUri: Rep[Option[String]] = column[Option[String]]("redirect_uri", O.Length(254,varying=true), O.Default(None))
    /** Database column scope SqlType(varchar), Length(254,true), Default(None) */
    val scope: Rep[Option[String]] = column[Option[String]]("scope", O.Length(254,varying=true), O.Default(None))
  }
  /** Collection-like TableQuery object for table Clients */
  lazy val Clients = new TableQuery(tag => new Clients(tag))

  /** GetResult implicit for fetching EntityType objects using plain SQL queries */
  implicit def GetResultEntityType(implicit e0: GR[String], e1: GR[Option[String]]): GR[EntityType] = GR{
    prs => import prs._
    EntityType.tupled((<<[String], <<?[String]))
  }
  /** Table description of table entity_types. Objects of this class serve as prototypes for rows in queries. */
  class EntityTypes(_tableTag: Tag) extends Table[EntityType](_tableTag, "entity_types") {
    def * = (id, description) <> (EntityType.tupled, EntityType.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), description).shaped.<>({r=>import r._; _1.map(_=> EntityType.tupled((_1.get, _2)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(varchar), PrimaryKey, Length(40,true) */
    val id: Rep[String] = column[String]("id", O.PrimaryKey, O.Length(40,varying=true))
    /** Database column description SqlType(varchar), Length(100,true), Default(None) */
    val description: Rep[Option[String]] = column[Option[String]]("description", O.Length(100,varying=true), O.Default(None))
  }
  /** Collection-like TableQuery object for table EntityTypes */
  lazy val EntityTypes = new TableQuery(tag => new EntityTypes(tag))

  /** GetResult implicit for fetching GrantType objects using plain SQL queries */
  implicit def GetResultGrantType(implicit e0: GR[Int], e1: GR[String]): GR[GrantType] = GR{
    prs => import prs._
    GrantType.tupled((<<[Int], <<[String]))
  }
  /** Table description of table grant_types. Objects of this class serve as prototypes for rows in queries. */
  class GrantTypes(_tableTag: Tag) extends Table[GrantType](_tableTag, "grant_types") {
    def * = (id, grantType) <> (GrantType.tupled, GrantType.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(grantType)).shaped.<>({r=>import r._; _1.map(_=> GrantType.tupled((_1.get, _2.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(int4), PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.PrimaryKey)
    /** Database column grant_type SqlType(varchar), Length(254,true) */
    val grantType: Rep[String] = column[String]("grant_type", O.Length(254,varying=true))
  }
  /** Collection-like TableQuery object for table GrantTypes */
  lazy val GrantTypes = new TableQuery(tag => new GrantTypes(tag))

  /** GetResult implicit for fetching Group objects using plain SQL queries */
  implicit def GetResultGroup(implicit e0: GR[String], e1: GR[Short], e2: GR[Option[String]], e3: GR[Int], e4: GR[java.sql.Timestamp]): GR[Group] = GR{
    prs => import prs._
    Group.tupled((<<[String], <<[String], <<[Short], <<[String], <<[String], <<?[String], <<[Int], <<[java.sql.Timestamp], <<[java.sql.Timestamp], <<[Int]))
  }
  /** Table description of table groups. Objects of this class serve as prototypes for rows in queries.
   *  NOTE: The following names collided with Scala keywords and were escaped: type */
  class Groups(_tableTag: Tag) extends Table[Group](_tableTag, "groups") {
    def * = (id, projectId, `type`, name, userId, groupId, perm, created, updated, permProject) <> (Group.tupled, Group.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(projectId), Rep.Some(`type`), Rep.Some(name), Rep.Some(userId), groupId, Rep.Some(perm), Rep.Some(created), Rep.Some(updated), Rep.Some(permProject)).shaped.<>({r=>import r._; _1.map(_=> Group.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6, _7.get, _8.get, _9.get, _10.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(varchar), PrimaryKey, Length(40,true) */
    val id: Rep[String] = column[String]("id", O.PrimaryKey, O.Length(40,varying=true))
    /** Database column project_id SqlType(varchar), Length(40,true) */
    val projectId: Rep[String] = column[String]("project_id", O.Length(40,varying=true))
    /** Database column type SqlType(int2), Default(0)
     *  NOTE: The name was escaped because it collided with a Scala keyword. */
    val `type`: Rep[Short] = column[Short]("type", O.Default(0))
    /** Database column name SqlType(varchar), Length(200,true) */
    val name: Rep[String] = column[String]("name", O.Length(200,varying=true))
    /** Database column user_id SqlType(varchar), Length(40,true) */
    val userId: Rep[String] = column[String]("user_id", O.Length(40,varying=true))
    /** Database column group_id SqlType(varchar), Length(40,true), Default(None) */
    val groupId: Rep[Option[String]] = column[Option[String]]("group_id", O.Length(40,varying=true), O.Default(None))
    /** Database column perm SqlType(int4), Default(448) */
    val perm: Rep[Int] = column[Int]("perm", O.Default(448))
    /** Database column created SqlType(timestamp) */
    val created: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("created")
    /** Database column updated SqlType(timestamp) */
    val updated: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("updated")
    /** Database column perm_project SqlType(int4), Default(1) */
    val permProject: Rep[Int] = column[Int]("perm_project", O.Default(1))

    /** Foreign key referencing Groups (database name groups_group_id_fkey) */
    lazy val groupsFk = foreignKey("groups_group_id_fkey", groupId, Groups)(r => Rep.Some(r.id), onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing Projects (database name groups_project_id_fkey) */
    lazy val projectsFk = foreignKey("groups_project_id_fkey", projectId, Projects)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing Users (database name groups_user_id_fkey) */
    lazy val usersFk = foreignKey("groups_user_id_fkey", userId, Users)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table Groups */
  lazy val Groups = new TableQuery(tag => new Groups(tag))

  /** GetResult implicit for fetching GroupsUser objects using plain SQL queries */
  implicit def GetResultGroupsUser(implicit e0: GR[String]): GR[GroupsUser] = GR{
    prs => import prs._
    GroupsUser.tupled((<<[String], <<[String]))
  }
  /** Table description of table groups_users. Objects of this class serve as prototypes for rows in queries. */
  class GroupsUsers(_tableTag: Tag) extends Table[GroupsUser](_tableTag, "groups_users") {
    def * = (groupId, userId) <> (GroupsUser.tupled, GroupsUser.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(groupId), Rep.Some(userId)).shaped.<>({r=>import r._; _1.map(_=> GroupsUser.tupled((_1.get, _2.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column group_id SqlType(varchar), Length(40,true) */
    val groupId: Rep[String] = column[String]("group_id", O.Length(40,varying=true))
    /** Database column user_id SqlType(varchar), Length(40,true) */
    val userId: Rep[String] = column[String]("user_id", O.Length(40,varying=true))

    /** Primary key of GroupsUsers (database name groups_users_pkey) */
    val pk = primaryKey("groups_users_pkey", (groupId, userId))

    /** Foreign key referencing Groups (database name groups_users_group_id_fkey) */
    lazy val groupsFk = foreignKey("groups_users_group_id_fkey", groupId, Groups)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing Users (database name groups_users_user_id_fkey) */
    lazy val usersFk = foreignKey("groups_users_user_id_fkey", userId, Users)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table GroupsUsers */
  lazy val GroupsUsers = new TableQuery(tag => new GroupsUsers(tag))

  /** GetResult implicit for fetching Label objects using plain SQL queries */
  implicit def GetResultLabel(implicit e0: GR[String], e1: GR[Option[String]]): GR[Label] = GR{
    prs => import prs._
    Label.tupled((<<[String], <<[String], <<[String], <<[String], <<?[String], <<?[String], <<?[String]))
  }
  /** Table description of table labels. Objects of this class serve as prototypes for rows in queries. */
  class Labels(_tableTag: Tag) extends Table[Label](_tableTag, "labels") {
    def * = (id, lang, entityId, entityTypeId, label1, label2, label3) <> (Label.tupled, Label.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(lang), Rep.Some(entityId), Rep.Some(entityTypeId), label1, label2, label3).shaped.<>({r=>import r._; _1.map(_=> Label.tupled((_1.get, _2.get, _3.get, _4.get, _5, _6, _7)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(varchar), PrimaryKey, Length(40,true) */
    val id: Rep[String] = column[String]("id", O.PrimaryKey, O.Length(40,varying=true))
    /** Database column lang SqlType(varchar), Length(10,true) */
    val lang: Rep[String] = column[String]("lang", O.Length(10,varying=true))
    /** Database column entity_id SqlType(varchar), Length(40,true) */
    val entityId: Rep[String] = column[String]("entity_id", O.Length(40,varying=true))
    /** Database column entity_type_id SqlType(varchar), Length(40,true) */
    val entityTypeId: Rep[String] = column[String]("entity_type_id", O.Length(40,varying=true))
    /** Database column label1 SqlType(varchar), Length(1000,true), Default(None) */
    val label1: Rep[Option[String]] = column[Option[String]]("label1", O.Length(1000,varying=true), O.Default(None))
    /** Database column label2 SqlType(varchar), Length(1000,true), Default(None) */
    val label2: Rep[Option[String]] = column[Option[String]]("label2", O.Length(1000,varying=true), O.Default(None))
    /** Database column label3 SqlType(varchar), Length(1000,true), Default(None) */
    val label3: Rep[Option[String]] = column[Option[String]]("label3", O.Length(1000,varying=true), O.Default(None))
  }
  /** Collection-like TableQuery object for table Labels */
  lazy val Labels = new TableQuery(tag => new Labels(tag))

  /** GetResult implicit for fetching Project objects using plain SQL queries */
  implicit def GetResultProject(implicit e0: GR[String], e1: GR[Option[String]], e2: GR[Short], e3: GR[Int], e4: GR[java.sql.Timestamp]): GR[Project] = GR{
    prs => import prs._
    Project.tupled((<<[String], <<[String], <<[String], <<?[String], <<?[String], <<[Short], <<[Int], <<[java.sql.Timestamp], <<[java.sql.Timestamp]))
  }
  /** Table description of table projects. Objects of this class serve as prototypes for rows in queries. */
  class Projects(_tableTag: Tag) extends Table[Project](_tableTag, "projects") {
    def * = (id, userId, name, description, parentId, status, perm, created, updated) <> (Project.tupled, Project.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(userId), Rep.Some(name), description, parentId, Rep.Some(status), Rep.Some(perm), Rep.Some(created), Rep.Some(updated)).shaped.<>({r=>import r._; _1.map(_=> Project.tupled((_1.get, _2.get, _3.get, _4, _5, _6.get, _7.get, _8.get, _9.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(varchar), PrimaryKey, Length(40,true) */
    val id: Rep[String] = column[String]("id", O.PrimaryKey, O.Length(40,varying=true))
    /** Database column user_id SqlType(varchar), Length(40,true) */
    val userId: Rep[String] = column[String]("user_id", O.Length(40,varying=true))
    /** Database column name SqlType(varchar), Length(255,true) */
    val name: Rep[String] = column[String]("name", O.Length(255,varying=true))
    /** Database column description SqlType(text), Default(None) */
    val description: Rep[Option[String]] = column[Option[String]]("description", O.Default(None))
    /** Database column parent_id SqlType(varchar), Length(40,true), Default(None) */
    val parentId: Rep[Option[String]] = column[Option[String]]("parent_id", O.Length(40,varying=true), O.Default(None))
    /** Database column status SqlType(int2), Default(0) */
    val status: Rep[Short] = column[Short]("status", O.Default(0))
    /** Database column perm SqlType(int4), Default(448) */
    val perm: Rep[Int] = column[Int]("perm", O.Default(448))
    /** Database column created SqlType(timestamp) */
    val created: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("created")
    /** Database column updated SqlType(timestamp) */
    val updated: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("updated")

    /** Uniqueness Index over (userId,name) (database name projects_user_id_name_key) */
    val index1 = index("projects_user_id_name_key", (userId, name), unique=true)
  }
  /** Collection-like TableQuery object for table Projects */
  lazy val Projects = new TableQuery(tag => new Projects(tag))

  /** GetResult implicit for fetching Resource objects using plain SQL queries */
  implicit def GetResultResource(implicit e0: GR[String], e1: GR[Option[String]], e2: GR[Int], e3: GR[java.sql.Timestamp]): GR[Resource] = GR{
    prs => import prs._
    Resource.tupled((<<[String], <<?[String], <<[String], <<[String], <<[String], <<[Int], <<[java.sql.Timestamp], <<[java.sql.Timestamp]))
  }
  /** Table description of table resources. Objects of this class serve as prototypes for rows in queries. */
  class Resources(_tableTag: Tag) extends Table[Resource](_tableTag, "resources") {
    def * = (id, content, entityTypeId, userId, groupId, perm, created, updated) <> (Resource.tupled, Resource.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), content, Rep.Some(entityTypeId), Rep.Some(userId), Rep.Some(groupId), Rep.Some(perm), Rep.Some(created), Rep.Some(updated)).shaped.<>({r=>import r._; _1.map(_=> Resource.tupled((_1.get, _2, _3.get, _4.get, _5.get, _6.get, _7.get, _8.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(varchar), PrimaryKey, Length(40,true) */
    val id: Rep[String] = column[String]("id", O.PrimaryKey, O.Length(40,varying=true))
    /** Database column content SqlType(varchar), Length(1000,true), Default(None) */
    val content: Rep[Option[String]] = column[Option[String]]("content", O.Length(1000,varying=true), O.Default(None))
    /** Database column entity_type_id SqlType(varchar), Length(40,true) */
    val entityTypeId: Rep[String] = column[String]("entity_type_id", O.Length(40,varying=true))
    /** Database column user_id SqlType(varchar), Length(40,true) */
    val userId: Rep[String] = column[String]("user_id", O.Length(40,varying=true))
    /** Database column group_id SqlType(varchar), Length(40,true) */
    val groupId: Rep[String] = column[String]("group_id", O.Length(40,varying=true))
    /** Database column perm SqlType(int4), Default(448) */
    val perm: Rep[Int] = column[Int]("perm", O.Default(448))
    /** Database column created SqlType(timestamp) */
    val created: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("created")
    /** Database column updated SqlType(timestamp) */
    val updated: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("updated")

    /** Foreign key referencing Groups (database name resources_group_id_fkey) */
    lazy val groupsFk = foreignKey("resources_group_id_fkey", groupId, Groups)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing Users (database name resources_user_id_fkey) */
    lazy val usersFk = foreignKey("resources_user_id_fkey", userId, Users)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table Resources */
  lazy val Resources = new TableQuery(tag => new Resources(tag))

  /** GetResult implicit for fetching Task objects using plain SQL queries */
  implicit def GetResultTask(implicit e0: GR[String], e1: GR[Option[String]], e2: GR[Short], e3: GR[Int], e4: GR[java.sql.Timestamp]): GR[Task] = GR{
    prs => import prs._
    Task.tupled((<<[String], <<[String], <<[String], <<[String], <<[String], <<?[String], <<?[String], <<[Short], <<[Int], <<[java.sql.Timestamp], <<[java.sql.Timestamp]))
  }
  /** Table description of table tasks. Objects of this class serve as prototypes for rows in queries. */
  class Tasks(_tableTag: Tag) extends Table[Task](_tableTag, "tasks") {
    def * = (id, projectId, userId, groupId, subject, description, parentId, status, perm, created, updated) <> (Task.tupled, Task.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(projectId), Rep.Some(userId), Rep.Some(groupId), Rep.Some(subject), description, parentId, Rep.Some(status), Rep.Some(perm), Rep.Some(created), Rep.Some(updated)).shaped.<>({r=>import r._; _1.map(_=> Task.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6, _7, _8.get, _9.get, _10.get, _11.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(varchar), PrimaryKey, Length(40,true) */
    val id: Rep[String] = column[String]("id", O.PrimaryKey, O.Length(40,varying=true))
    /** Database column project_id SqlType(varchar), Length(40,true) */
    val projectId: Rep[String] = column[String]("project_id", O.Length(40,varying=true))
    /** Database column user_id SqlType(varchar), Length(40,true) */
    val userId: Rep[String] = column[String]("user_id", O.Length(40,varying=true))
    /** Database column group_id SqlType(varchar), Length(40,true) */
    val groupId: Rep[String] = column[String]("group_id", O.Length(40,varying=true))
    /** Database column subject SqlType(varchar), Length(255,true) */
    val subject: Rep[String] = column[String]("subject", O.Length(255,varying=true))
    /** Database column description SqlType(text), Default(None) */
    val description: Rep[Option[String]] = column[Option[String]]("description", O.Default(None))
    /** Database column parent_id SqlType(varchar), Length(40,true), Default(None) */
    val parentId: Rep[Option[String]] = column[Option[String]]("parent_id", O.Length(40,varying=true), O.Default(None))
    /** Database column status SqlType(int2), Default(0) */
    val status: Rep[Short] = column[Short]("status", O.Default(0))
    /** Database column perm SqlType(int4), Default(448) */
    val perm: Rep[Int] = column[Int]("perm", O.Default(448))
    /** Database column created SqlType(timestamp) */
    val created: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("created")
    /** Database column updated SqlType(timestamp) */
    val updated: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("updated")

    /** Foreign key referencing Groups (database name tasks_group_id_fkey) */
    lazy val groupsFk = foreignKey("tasks_group_id_fkey", groupId, Groups)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing Projects (database name tasks_project_id_fkey) */
    lazy val projectsFk = foreignKey("tasks_project_id_fkey", projectId, Projects)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing Tasks (database name tasks_parent_id_fkey) */
    lazy val tasksFk = foreignKey("tasks_parent_id_fkey", parentId, Tasks)(r => Rep.Some(r.id), onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing Users (database name tasks_user_id_fkey) */
    lazy val usersFk = foreignKey("tasks_user_id_fkey", userId, Users)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table Tasks */
  lazy val Tasks = new TableQuery(tag => new Tasks(tag))

  /** GetResult implicit for fetching User objects using plain SQL queries */
  implicit def GetResultUser(implicit e0: GR[String], e1: GR[Int], e2: GR[Option[String]], e3: GR[Option[java.sql.Timestamp]], e4: GR[java.sql.Timestamp]): GR[User] = GR{
    prs => import prs._
    User.tupled((<<[String], <<[String], <<[String], <<[Int], <<?[String], <<?[java.sql.Timestamp], <<[Int], <<[String], <<?[String], <<?[String], <<[Int], <<[java.sql.Timestamp], <<[java.sql.Timestamp]))
  }
  /** Table description of table users. Objects of this class serve as prototypes for rows in queries. */
  class Users(_tableTag: Tag) extends Table[User](_tableTag, "users") {
    def * = (id, login, nick, provider, providerToken, lastLogin, status, password, userId, groupId, perm, created, updated) <> (User.tupled, User.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(login), Rep.Some(nick), Rep.Some(provider), providerToken, lastLogin, Rep.Some(status), Rep.Some(password), userId, groupId, Rep.Some(perm), Rep.Some(created), Rep.Some(updated)).shaped.<>({r=>import r._; _1.map(_=> User.tupled((_1.get, _2.get, _3.get, _4.get, _5, _6, _7.get, _8.get, _9, _10, _11.get, _12.get, _13.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(varchar), PrimaryKey, Length(40,true) */
    val id: Rep[String] = column[String]("id", O.PrimaryKey, O.Length(40,varying=true))
    /** Database column login SqlType(varchar), Length(255,true) */
    val login: Rep[String] = column[String]("login", O.Length(255,varying=true))
    /** Database column nick SqlType(varchar), Length(255,true) */
    val nick: Rep[String] = column[String]("nick", O.Length(255,varying=true))
    /** Database column provider SqlType(int4), Default(0) */
    val provider: Rep[Int] = column[Int]("provider", O.Default(0))
    /** Database column provider_token SqlType(varchar), Length(255,true), Default(None) */
    val providerToken: Rep[Option[String]] = column[Option[String]]("provider_token", O.Length(255,varying=true), O.Default(None))
    /** Database column last_login SqlType(timestamp), Default(None) */
    val lastLogin: Rep[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("last_login", O.Default(None))
    /** Database column status SqlType(int4), Default(0) */
    val status: Rep[Int] = column[Int]("status", O.Default(0))
    /** Database column password SqlType(varchar), Length(100,true), Default() */
    val password: Rep[String] = column[String]("password", O.Length(100,varying=true), O.Default(""))
    /** Database column user_id SqlType(varchar), Length(40,true), Default(None) */
    val userId: Rep[Option[String]] = column[Option[String]]("user_id", O.Length(40,varying=true), O.Default(None))
    /** Database column group_id SqlType(varchar), Length(40,true), Default(None) */
    val groupId: Rep[Option[String]] = column[Option[String]]("group_id", O.Length(40,varying=true), O.Default(None))
    /** Database column perm SqlType(int4), Default(448) */
    val perm: Rep[Int] = column[Int]("perm", O.Default(448))
    /** Database column created SqlType(timestamp) */
    val created: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("created")
    /** Database column updated SqlType(timestamp) */
    val updated: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("updated")

    /** Uniqueness Index over (login) (database name users_login_key) */
    val index1 = index("users_login_key", login, unique=true)
    /** Uniqueness Index over (nick) (database name users_nick_key) */
    val index2 = index("users_nick_key", nick, unique=true)
  }
  /** Collection-like TableQuery object for table Users */
  lazy val Users = new TableQuery(tag => new Users(tag))

  /** GetResult implicit for fetching UserSession objects using plain SQL queries */
  implicit def GetResultUserSession(implicit e0: GR[String]): GR[UserSession] = GR{
    prs => import prs._
    UserSession.tupled((<<[String], <<[String]))
  }
  /** Table description of table user_sessions. Objects of this class serve as prototypes for rows in queries. */
  class UserSessions(_tableTag: Tag) extends Table[UserSession](_tableTag, "user_sessions") {
    def * = (id, userId) <> (UserSession.tupled, UserSession.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(userId)).shaped.<>({r=>import r._; _1.map(_=> UserSession.tupled((_1.get, _2.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(varchar), PrimaryKey, Length(254,true) */
    val id: Rep[String] = column[String]("id", O.PrimaryKey, O.Length(254,varying=true))
    /** Database column user_id SqlType(varchar) */
    val userId: Rep[String] = column[String]("user_id")

    /** Foreign key referencing Users (database name user_sessions_user_id_fkey) */
    lazy val usersFk = foreignKey("user_sessions_user_id_fkey", userId, Users)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table UserSessions */
  lazy val UserSessions = new TableQuery(tag => new UserSessions(tag))

  /** GetResult implicit for fetching UserStatus objects using plain SQL queries */
  implicit def GetResultUserStatus(implicit e0: GR[Int], e1: GR[Option[String]]): GR[UserStatus] = GR{
    prs => import prs._
    UserStatus.tupled((<<[Int], <<?[String]))
  }
  /** Table description of table user_statuses. Objects of this class serve as prototypes for rows in queries. */
  class UserStatuses(_tableTag: Tag) extends Table[UserStatus](_tableTag, "user_statuses") {
    def * = (id, description) <> (UserStatus.tupled, UserStatus.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), description).shaped.<>({r=>import r._; _1.map(_=> UserStatus.tupled((_1.get, _2)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(int4), PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.PrimaryKey)
    /** Database column description SqlType(varchar), Length(100,true), Default(None) */
    val description: Rep[Option[String]] = column[Option[String]]("description", O.Length(100,varying=true), O.Default(None))
  }
  /** Collection-like TableQuery object for table UserStatuses */
  lazy val UserStatuses = new TableQuery(tag => new UserStatuses(tag))

  /** GetResult implicit for fetching Verb objects using plain SQL queries */
  implicit def GetResultVerb(implicit e0: GR[Int], e1: GR[Option[String]]): GR[Verb] = GR{
    prs => import prs._
    Verb.tupled((<<[Int], <<?[String]))
  }
  /** Table description of table verbs. Objects of this class serve as prototypes for rows in queries. */
  class Verbs(_tableTag: Tag) extends Table[Verb](_tableTag, "verbs") {
    def * = (id, description) <> (Verb.tupled, Verb.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), description).shaped.<>({r=>import r._; _1.map(_=> Verb.tupled((_1.get, _2)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(int4), PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.PrimaryKey)
    /** Database column description SqlType(varchar), Length(100,true), Default(None) */
    val description: Rep[Option[String]] = column[Option[String]]("description", O.Length(100,varying=true), O.Default(None))
  }
  /** Collection-like TableQuery object for table Verbs */
  lazy val Verbs = new TableQuery(tag => new Verbs(tag))

}
/** Entity class storing rows of table AccessTokens
  *  @param accessToken Database column access_token SqlType(varchar), PrimaryKey, Length(254,true)
  *  @param refreshToken Database column refresh_token SqlType(varchar), Length(254,true), Default(None)
  *  @param userId Database column user_id SqlType(varchar)
  *  @param scope Database column scope SqlType(varchar), Length(254,true), Default(None)
  *  @param expiresIn Database column expires_in SqlType(int4)
  *  @param created Database column created SqlType(timestamp)
  *  @param clientId Database column client_id SqlType(varchar) */
case class AccessToken(accessToken: String, refreshToken: Option[String] = None, userId: String, scope: Option[String] = None, expiresIn: Int, created: java.sql.Timestamp, clientId: String)
/** Entity class storing rows of table Actions
  *  @param id Database column id SqlType(varchar), PrimaryKey, Length(40,true)
  *  @param description Database column description SqlType(varchar), Length(100,true), Default(None)
  *  @param url Database column url SqlType(varchar), Length(2000,true)
  *  @param verbId Database column verb_id SqlType(int4)
  *  @param secured Database column secured SqlType(int2)
  *  @param userId Database column user_id SqlType(varchar), Length(40,true)
  *  @param groupId Database column group_id SqlType(varchar), Length(40,true)
  *  @param perm Database column perm SqlType(int4), Default(448)
  *  @param created Database column created SqlType(timestamp)
  *  @param updated Database column updated SqlType(timestamp) */
case class Action(id: String, description: Option[String] = None, url: String, verbId: Int, secured: Short, userId: String, groupId: String, perm: Int = 448, created: java.sql.Timestamp, updated: java.sql.Timestamp)
/** Entity class storing rows of table AuthCodes
  *  @param authorizationCode Database column authorization_code SqlType(varchar), PrimaryKey, Length(254,true)
  *  @param userId Database column user_id SqlType(varchar)
  *  @param redirectUri Database column redirect_uri SqlType(varchar), Length(254,true), Default(None)
  *  @param createdAt Database column created_at SqlType(timestamp)
  *  @param scope Database column scope SqlType(varchar), Length(254,true), Default(None)
  *  @param clientId Database column client_id SqlType(varchar), Length(254,true)
  *  @param expiresIn Database column expires_in SqlType(int4) */
case class AuthCode(authorizationCode: String, userId: String, redirectUri: Option[String] = None, createdAt: java.sql.Timestamp, scope: Option[String] = None, clientId: String, expiresIn: Int)
/** Entity class storing rows of table ClientGrantTypes
  *  @param clientId Database column client_id SqlType(varchar), Length(254,true)
  *  @param grantTypeId Database column grant_type_id SqlType(int4) */
case class ClientGrantType(clientId: String, grantTypeId: Int)
/** Entity class storing rows of table Clients
  *  @param id Database column id SqlType(varchar), PrimaryKey, Length(254,true)
  *  @param secret Database column secret SqlType(varchar), Length(254,true), Default(None)
  *  @param redirectUri Database column redirect_uri SqlType(varchar), Length(254,true), Default(None)
  *  @param scope Database column scope SqlType(varchar), Length(254,true), Default(None) */
case class Client(id: String, secret: Option[String] = None, redirectUri: Option[String] = None, scope: Option[String] = None)
/** Entity class storing rows of table EntityTypes
  *  @param id Database column id SqlType(varchar), PrimaryKey, Length(40,true)
  *  @param description Database column description SqlType(varchar), Length(100,true), Default(None) */
case class EntityType(id: String, description: Option[String] = None)
/** Entity class storing rows of table GrantTypes
  *  @param id Database column id SqlType(int4), PrimaryKey
  *  @param grantType Database column grant_type SqlType(varchar), Length(254,true) */
case class GrantType(id: Int, grantType: String)
/** Entity class storing rows of table Groups
  *  @param id Database column id SqlType(varchar), PrimaryKey, Length(40,true)
  *  @param projectId Database column project_id SqlType(varchar), Length(40,true)
  *  @param `type` Database column type SqlType(int2), Default(0)
  *  @param name Database column name SqlType(varchar), Length(200,true)
  *  @param userId Database column user_id SqlType(varchar), Length(40,true)
  *  @param groupId Database column group_id SqlType(varchar), Length(40,true), Default(None)
  *  @param perm Database column perm SqlType(int4), Default(448)
  *  @param created Database column created SqlType(timestamp)
  *  @param updated Database column updated SqlType(timestamp)
  *  @param permProject Database column perm_project SqlType(int4), Default(1) */
case class Group(id: String, projectId: String, `type`: Short = 0, name: String, userId: String, groupId: Option[String] = None, perm: Int = 448, created: java.sql.Timestamp, updated: java.sql.Timestamp, permProject: Int = 1)
/** Entity class storing rows of table GroupsUsers
  *  @param groupId Database column group_id SqlType(varchar), Length(40,true)
  *  @param userId Database column user_id SqlType(varchar), Length(40,true) */
case class GroupsUser(groupId: String, userId: String)
/** Entity class storing rows of table Labels
  *  @param id Database column id SqlType(varchar), PrimaryKey, Length(40,true)
  *  @param lang Database column lang SqlType(varchar), Length(10,true)
  *  @param entityId Database column entity_id SqlType(varchar), Length(40,true)
  *  @param entityTypeId Database column entity_type_id SqlType(varchar), Length(40,true)
  *  @param label1 Database column label1 SqlType(varchar), Length(1000,true), Default(None)
  *  @param label2 Database column label2 SqlType(varchar), Length(1000,true), Default(None)
  *  @param label3 Database column label3 SqlType(varchar), Length(1000,true), Default(None) */
case class Label(id: String, lang: String, entityId: String, entityTypeId: String, label1: Option[String] = None, label2: Option[String] = None, label3: Option[String] = None)
/** Entity class storing rows of table Projects
  *  @param id Database column id SqlType(varchar), PrimaryKey, Length(40,true)
  *  @param userId Database column user_id SqlType(varchar), Length(40,true)
  *  @param name Database column name SqlType(varchar), Length(255,true)
  *  @param description Database column description SqlType(text), Default(None)
  *  @param parentId Database column parent_id SqlType(varchar), Length(40,true), Default(None)
  *  @param status Database column status SqlType(int2), Default(0)
  *  @param perm Database column perm SqlType(int4), Default(448)
  *  @param created Database column created SqlType(timestamp)
  *  @param updated Database column updated SqlType(timestamp) */
case class Project(id: String, userId: String, name: String, description: Option[String] = None, parentId: Option[String] = None, status: Short = 0, perm: Int = 448, created: java.sql.Timestamp, updated: java.sql.Timestamp)
/** Entity class storing rows of table Resources
  *  @param id Database column id SqlType(varchar), PrimaryKey, Length(40,true)
  *  @param content Database column content SqlType(varchar), Length(1000,true), Default(None)
  *  @param entityTypeId Database column entity_type_id SqlType(varchar), Length(40,true)
  *  @param userId Database column user_id SqlType(varchar), Length(40,true)
  *  @param groupId Database column group_id SqlType(varchar), Length(40,true)
  *  @param perm Database column perm SqlType(int4), Default(448)
  *  @param created Database column created SqlType(timestamp)
  *  @param updated Database column updated SqlType(timestamp) */
case class Resource(id: String, content: Option[String] = None, entityTypeId: String, userId: String, groupId: String, perm: Int = 448, created: java.sql.Timestamp, updated: java.sql.Timestamp)
/** Entity class storing rows of table Tasks
  *  @param id Database column id SqlType(varchar), PrimaryKey, Length(40,true)
  *  @param projectId Database column project_id SqlType(varchar), Length(40,true)
  *  @param userId Database column user_id SqlType(varchar), Length(40,true)
  *  @param groupId Database column group_id SqlType(varchar), Length(40,true)
  *  @param subject Database column subject SqlType(varchar), Length(255,true)
  *  @param description Database column description SqlType(text), Default(None)
  *  @param parentId Database column parent_id SqlType(varchar), Length(40,true), Default(None)
  *  @param status Database column status SqlType(int2), Default(0)
  *  @param perm Database column perm SqlType(int4), Default(448)
  *  @param created Database column created SqlType(timestamp)
  *  @param updated Database column updated SqlType(timestamp) */
case class Task(id: String, projectId: String, userId: String, groupId: String, subject: String, description: Option[String] = None, parentId: Option[String] = None, status: Short = 0, perm: Int = 448, created: java.sql.Timestamp, updated: java.sql.Timestamp)
/** Entity class storing rows of table Users
  *  @param id Database column id SqlType(varchar), PrimaryKey, Length(40,true)
  *  @param login Database column login SqlType(varchar), Length(255,true)
  *  @param nick Database column nick SqlType(varchar), Length(255,true)
  *  @param provider Database column provider SqlType(int4), Default(0)
  *  @param providerToken Database column provider_token SqlType(varchar), Length(255,true), Default(None)
  *  @param lastLogin Database column last_login SqlType(timestamp), Default(None)
  *  @param status Database column status SqlType(int4), Default(0)
  *  @param password Database column password SqlType(varchar), Length(100,true), Default()
  *  @param userId Database column user_id SqlType(varchar), Length(40,true), Default(None)
  *  @param groupId Database column group_id SqlType(varchar), Length(40,true), Default(None)
  *  @param perm Database column perm SqlType(int4), Default(448)
  *  @param created Database column created SqlType(timestamp)
  *  @param updated Database column updated SqlType(timestamp) */
case class User(id: String, login: String, nick: String, provider: Int = 0, providerToken: Option[String] = None, lastLogin: Option[java.sql.Timestamp] = None, status: Int = 0, password: String = "", userId: Option[String] = None, groupId: Option[String] = None, perm: Int = 448, created: java.sql.Timestamp, updated: java.sql.Timestamp)
/** Entity class storing rows of table UserSessions
  *  @param id Database column id SqlType(varchar), PrimaryKey, Length(254,true)
  *  @param userId Database column user_id SqlType(varchar) */
case class UserSession(id: String, userId: String)
/** Entity class storing rows of table UserStatuses
  *  @param id Database column id SqlType(int4), PrimaryKey
  *  @param description Database column description SqlType(varchar), Length(100,true), Default(None) */
case class UserStatus(id: Int, description: Option[String] = None)
/** Entity class storing rows of table Verbs
  *  @param id Database column id SqlType(int4), PrimaryKey
  *  @param description Database column description SqlType(varchar), Length(100,true), Default(None) */
case class Verb(id: Int, description: Option[String] = None)
