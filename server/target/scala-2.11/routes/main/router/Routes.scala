
// @GENERATOR:play-routes-compiler
// @SOURCE:/home/octav/projects/shopping-list/server/conf/routes
// @DATE:Tue Oct 04 09:41:02 EEST 2016

package router

import play.core.routing._
import play.core.routing.HandlerInvokerFactory._
import play.core.j._

import play.api.mvc._

import _root_.controllers.Assets.Asset

class Routes(
  override val errorHandler: play.api.http.HttpErrorHandler, 
  // @LINE:5
  MainController_1: javax.inject.Provider[org.shopping.controllers.MainController],
  // @LINE:7
  UserController_0: javax.inject.Provider[org.shopping.controllers.UserController],
  // @LINE:43
  ListController_2: javax.inject.Provider[org.shopping.controllers.ListController],
  // @LINE:282
  Assets_4: controllers.Assets,
  // @LINE:295
  Default_3: controllers.Default,
  val prefix: String
) extends GeneratedRouter {

   @javax.inject.Inject()
   def this(errorHandler: play.api.http.HttpErrorHandler,
    // @LINE:5
    MainController_1: javax.inject.Provider[org.shopping.controllers.MainController],
    // @LINE:7
    UserController_0: javax.inject.Provider[org.shopping.controllers.UserController],
    // @LINE:43
    ListController_2: javax.inject.Provider[org.shopping.controllers.ListController],
    // @LINE:282
    Assets_4: controllers.Assets,
    // @LINE:295
    Default_3: controllers.Default
  ) = this(errorHandler, MainController_1, UserController_0, ListController_2, Assets_4, Default_3, "/")

  import ReverseRouteContext.empty

  def withPrefix(prefix: String): Routes = {
    router.RoutesPrefix.setPrefix(prefix)
    new Routes(errorHandler, MainController_1, UserController_0, ListController_2, Assets_4, Default_3, prefix)
  }

  private[this] val defaultPrefix: String = {
    if (this.prefix.endsWith("/")) "" else "/"
  }

  def documentation = List(
    ("""OPTIONS""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """""" + "$" + """path<.+>""", """@org.shopping.controllers.MainController@.options(path:String)"""),
    ("""POST""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """api/oauth2/access_token""", """@org.shopping.controllers.UserController@.accessToken"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """login""", """@org.shopping.controllers.UserController@.loginGet"""),
    ("""POST""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """login""", """@org.shopping.controllers.UserController@.loginPostForm"""),
    ("""POST""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """api/list""", """@org.shopping.controllers.ListController@.insertList"""),
    ("""PUT""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """api/list/""" + "$" + """listId<[^/]+>""", """@org.shopping.controllers.ListController@.updateList(listId:String)"""),
    ("""DELETE""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """api/list/""" + "$" + """listId<[^/]+>""", """@org.shopping.controllers.ListController@.deleteList(listId:String)"""),
    ("""POST""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """api/list/""" + "$" + """listId<[^/]+>/items""", """@org.shopping.controllers.ListController@.addListItems(listId:String)"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """api/list/""" + "$" + """listId<[^/]+>/items""", """@org.shopping.controllers.ListController@.getListItems(listId:String)"""),
    ("""POST""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """api/login""", """@org.shopping.controllers.UserController@.login"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """api/user""", """@org.shopping.controllers.UserController@.getUserBySession"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """api/users""", """@org.shopping.controllers.UserController@.searchUsers(email:Option[String], nick:Option[String])"""),
    ("""POST""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """api/register""", """@org.shopping.controllers.UserController@.register"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """api/user/""" + "$" + """userId<[^/]+>/lists""", """@org.shopping.controllers.ListController@.getUserLists(userId:String, offset:Int, count:Int)"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """app/""" + "$" + """file<.+>""", """controllers.Assets.at(path:String = "/public/client/app", file:String)"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """public.html""", """controllers.Assets.at(path:String = "/public/client", file:String = "public.html")"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """autologin.html""", """controllers.Assets.at(path:String = "/public/client", file:String = "autologin.html")"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """index.html""", """controllers.Assets.at(path:String = "/public/client", file:String = "index.html")"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """docs/swagger-ui/""" + "$" + """file<.+>""", """controllers.Assets.at(path:String = "/public/lib/swagger-ui", file:String)"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """assets/""" + "$" + """file<.+>""", """controllers.Assets.versioned(path:String = "/public", file:Asset)"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """docs""", """controllers.Default.redirect(to:String = "/docs/swagger-ui/index.html?url=/assets/swagger.json")"""),
    Nil
  ).foldLeft(List.empty[(String,String,String)]) { (s,e) => e.asInstanceOf[Any] match {
    case r @ (_,_,_) => s :+ r.asInstanceOf[(String,String,String)]
    case l => s ++ l.asInstanceOf[List[(String,String,String)]]
  }}


  // @LINE:5
  private[this] lazy val org_shopping_controllers_MainController_options0_route = Route("OPTIONS",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), DynamicPart("path", """.+""",false)))
  )
  private[this] lazy val org_shopping_controllers_MainController_options0_invoker = createInvoker(
    MainController_1.get.options(fakeValue[String]),
    HandlerDef(this.getClass.getClassLoader,
      "router",
      "org.shopping.controllers.MainController",
      "options",
      Seq(classOf[String]),
      "OPTIONS",
      """ Routes
 This file defines all application routes (Higher priority routes first)
 ~~~~
## NoDocs ###""",
      this.prefix + """""" + "$" + """path<.+>"""
    )
  )

  // @LINE:7
  private[this] lazy val org_shopping_controllers_UserController_accessToken1_route = Route("POST",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/oauth2/access_token")))
  )
  private[this] lazy val org_shopping_controllers_UserController_accessToken1_invoker = createInvoker(
    UserController_0.get.accessToken,
    HandlerDef(this.getClass.getClassLoader,
      "router",
      "org.shopping.controllers.UserController",
      "accessToken",
      Nil,
      "POST",
      """## NoDocs ###""",
      this.prefix + """api/oauth2/access_token"""
    )
  )

  // @LINE:9
  private[this] lazy val org_shopping_controllers_UserController_loginGet2_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("login")))
  )
  private[this] lazy val org_shopping_controllers_UserController_loginGet2_invoker = createInvoker(
    UserController_0.get.loginGet,
    HandlerDef(this.getClass.getClassLoader,
      "router",
      "org.shopping.controllers.UserController",
      "loginGet",
      Nil,
      "GET",
      """## NoDocs ###""",
      this.prefix + """login"""
    )
  )

  // @LINE:12
  private[this] lazy val org_shopping_controllers_UserController_loginPostForm3_route = Route("POST",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("login")))
  )
  private[this] lazy val org_shopping_controllers_UserController_loginPostForm3_invoker = createInvoker(
    UserController_0.get.loginPostForm,
    HandlerDef(this.getClass.getClassLoader,
      "router",
      "org.shopping.controllers.UserController",
      "loginPostForm",
      Nil,
      "POST",
      """## NoDocs ###""",
      this.prefix + """login"""
    )
  )

  // @LINE:43
  private[this] lazy val org_shopping_controllers_ListController_insertList4_route = Route("POST",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/list")))
  )
  private[this] lazy val org_shopping_controllers_ListController_insertList4_invoker = createInvoker(
    ListController_2.get.insertList,
    HandlerDef(this.getClass.getClassLoader,
      "router",
      "org.shopping.controllers.ListController",
      "insertList",
      Nil,
      "POST",
      """##
 tags:
   - List operations
 summary: Create List
 consumes:
   - application/json
 produces:
   - application/json
 parameters:
   - name: body
     in: body
     description: register data
     required: true
     schema:
       """ + "$" + """ref: '#/definitions/org.shopping.dto.ListDTO'
   - name: "Authorization"
     in: "header"
     description: "Authorization code"
     required: true
     type: "string"
 responses:
   201:
     schema:
       """ + "$" + """ref: '#/definitions/org.shopping.dto.ListDTO'
   401:
     description: Unauthorized
     schema:
       """ + "$" + """ref: '#/definitions/org.shopping.dto.ErrorDTO'
##""",
      this.prefix + """api/list"""
    )
  )

  // @LINE:78
  private[this] lazy val org_shopping_controllers_ListController_updateList5_route = Route("PUT",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/list/"), DynamicPart("listId", """[^/]+""",true)))
  )
  private[this] lazy val org_shopping_controllers_ListController_updateList5_invoker = createInvoker(
    ListController_2.get.updateList(fakeValue[String]),
    HandlerDef(this.getClass.getClassLoader,
      "router",
      "org.shopping.controllers.ListController",
      "updateList",
      Seq(classOf[String]),
      "PUT",
      """##
 tags:
   - List operations
 summary: Update List
 consumes:
   - application/json
 produces:
   - application/json
 parameters:
   - name: body
     in: body
     description: register data
     required: true
     schema:
       """ + "$" + """ref: '#/definitions/org.shopping.dto.ListDTO'
   - name: "Authorization"
     in: "header"
     description: "Authorization code"
     required: true
     type: "string"
 responses:
   201:
     schema:
       """ + "$" + """ref: '#/definitions/org.shopping.dto.ListDTO'
   401:
     description: Unauthorized
     schema:
       """ + "$" + """ref: '#/definitions/org.shopping.dto.ErrorDTO'
   404:
     description: Entity not found
     schema:
       """ + "$" + """ref: '#/definitions/org.shopping.dto.ErrorDTO'
##""",
      this.prefix + """api/list/""" + "$" + """listId<[^/]+>"""
    )
  )

  // @LINE:102
  private[this] lazy val org_shopping_controllers_ListController_deleteList6_route = Route("DELETE",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/list/"), DynamicPart("listId", """[^/]+""",true)))
  )
  private[this] lazy val org_shopping_controllers_ListController_deleteList6_invoker = createInvoker(
    ListController_2.get.deleteList(fakeValue[String]),
    HandlerDef(this.getClass.getClassLoader,
      "router",
      "org.shopping.controllers.ListController",
      "deleteList",
      Seq(classOf[String]),
      "DELETE",
      """##
 tags:
   - List operations
 summary: Delete List
 parameters:
   - name: "Authorization"
     in: "header"
     description: "Authorization code"
     required: true
     type: "string"
 responses:
   204:
       description: Operation successful
   404:
     description: Entity not found
     schema:
       """ + "$" + """ref: '#/definitions/org.shopping.dto.ErrorDTO'
   401:
     description: Unauthorized
     schema:
       """ + "$" + """ref: '#/definitions/org.shopping.dto.ErrorDTO'
##""",
      this.prefix + """api/list/""" + "$" + """listId<[^/]+>"""
    )
  )

  // @LINE:137
  private[this] lazy val org_shopping_controllers_ListController_addListItems7_route = Route("POST",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/list/"), DynamicPart("listId", """[^/]+""",true), StaticPart("/items")))
  )
  private[this] lazy val org_shopping_controllers_ListController_addListItems7_invoker = createInvoker(
    ListController_2.get.addListItems(fakeValue[String]),
    HandlerDef(this.getClass.getClassLoader,
      "router",
      "org.shopping.controllers.ListController",
      "addListItems",
      Seq(classOf[String]),
      "POST",
      """##
 tags:
   - List operations
 summary: Add multiple list Items
 consumes:
   - application/json
 produces:
   - application/json
 parameters:
   - name: body
     in: body
     description: add list item data
     required: true
     schema:
       """ + "$" + """ref: '#/definitions/org.shopping.dto.ListItemDTO'
   - name: "Authorization"
     in: "header"
     description: "Authorization code"
     required: true
     type: "string"
 responses:
   201:
     schema:
       """ + "$" + """ref: '#/definitions/org.shopping.dto.ListItemsDTO'
   404:
     description: Entity not found
     schema:
       """ + "$" + """ref: '#/definitions/org.shopping.dto.ErrorDTO'
   401:
     description: Unauthorized
     schema:
       """ + "$" + """ref: '#/definitions/org.shopping.dto.ErrorDTO'
##""",
      this.prefix + """api/list/""" + "$" + """listId<[^/]+>/items"""
    )
  )

  // @LINE:164
  private[this] lazy val org_shopping_controllers_ListController_getListItems8_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/list/"), DynamicPart("listId", """[^/]+""",true), StaticPart("/items")))
  )
  private[this] lazy val org_shopping_controllers_ListController_getListItems8_invoker = createInvoker(
    ListController_2.get.getListItems(fakeValue[String]),
    HandlerDef(this.getClass.getClassLoader,
      "router",
      "org.shopping.controllers.ListController",
      "getListItems",
      Seq(classOf[String]),
      "GET",
      """##
 tags:
   - List operations
 summary: Get List by Id
 produces:
   - application/json
 parameters:
   - name: "Authorization"
     in: "header"
     description: "Authorization code"
     required: true
     type: "string"
 responses:
   200:
     schema:
       """ + "$" + """ref: '#/definitions/org.shopping.dto.ListDTO'
   404:
     description: Entity not found
     schema:
       """ + "$" + """ref: '#/definitions/org.shopping.dto.ErrorDTO'
   401:
     description: Unauthorized
     schema:
       """ + "$" + """ref: '#/definitions/org.shopping.dto.ErrorDTO'
##""",
      this.prefix + """api/list/""" + "$" + """listId<[^/]+>/items"""
    )
  )

  // @LINE:184
  private[this] lazy val org_shopping_controllers_UserController_login9_route = Route("POST",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/login")))
  )
  private[this] lazy val org_shopping_controllers_UserController_login9_invoker = createInvoker(
    UserController_0.get.login,
    HandlerDef(this.getClass.getClassLoader,
      "router",
      "org.shopping.controllers.UserController",
      "login",
      Nil,
      "POST",
      """##
 tags:
   - User operations
 summary: Login User
 consumes:
   - application/json
 parameters:
   - name: body
     in: body
     description: login data
     required: true
     schema:
       """ + "$" + """ref: '#/definitions/org.shopping.dto.LoginRequestDTO'
 responses:
   201:
     schema:
       """ + "$" + """ref: '#/definitions/org.shopping.dto.LoginResponseDTO'
##""",
      this.prefix + """api/login"""
    )
  )

  // @LINE:207
  private[this] lazy val org_shopping_controllers_UserController_getUserBySession10_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/user")))
  )
  private[this] lazy val org_shopping_controllers_UserController_getUserBySession10_invoker = createInvoker(
    UserController_0.get.getUserBySession,
    HandlerDef(this.getClass.getClassLoader,
      "router",
      "org.shopping.controllers.UserController",
      "getUserBySession",
      Nil,
      "GET",
      """##
 tags:
   - User operations
 summary: Get current user account
 produces:
   - application/json
 parameters:
   - name: "Authorization"
     in: "header"
     description: "Authorization code"
     required: true
     type: "string"
 responses:
   200:
     schema:
       """ + "$" + """ref: '#/definitions/org.shopping.dto.UserDTO'
   401:
     description: Unauthorized
     schema:
       """ + "$" + """ref: '#/definitions/org.shopping.dto.ErrorDTO'
##""",
      this.prefix + """api/user"""
    )
  )

  // @LINE:230
  private[this] lazy val org_shopping_controllers_UserController_searchUsers11_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/users")))
  )
  private[this] lazy val org_shopping_controllers_UserController_searchUsers11_invoker = createInvoker(
    UserController_0.get.searchUsers(fakeValue[Option[String]], fakeValue[Option[String]]),
    HandlerDef(this.getClass.getClassLoader,
      "router",
      "org.shopping.controllers.UserController",
      "searchUsers",
      Seq(classOf[Option[String]], classOf[Option[String]]),
      "GET",
      """##
 tags:
   - User operations
 summary: Get user users by email or nick
 produces:
   - application/json
 parameters:
   - name: "Authorization"
     in: "header"
     description: "Authorization code"
     required: true
     type: "string"
 responses:
   200:
     schema:
       """ + "$" + """ref: '#/definitions/org.shopping.dto.UsersDTO'
   401:
     description: Unauthorized
     schema:
       """ + "$" + """ref: '#/definitions/org.shopping.dto.ErrorDTO'
##""",
      this.prefix + """api/users"""
    )
  )

  // @LINE:252
  private[this] lazy val org_shopping_controllers_UserController_register12_route = Route("POST",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/register")))
  )
  private[this] lazy val org_shopping_controllers_UserController_register12_invoker = createInvoker(
    UserController_0.get.register,
    HandlerDef(this.getClass.getClassLoader,
      "router",
      "org.shopping.controllers.UserController",
      "register",
      Nil,
      "POST",
      """##
 tags:
   - User operations
 summary: Register User
 consumes:
   - application/json
 produces:
   - application/json
 parameters:
   - name: body
     in: body
     description: register data
     required: true
     schema:
       """ + "$" + """ref: '#/definitions/org.shopping.dto.RegisterRequestDTO'
 responses:
   201:
     schema:
       """ + "$" + """ref: '#/definitions/org.shopping.dto.RegisterResponseDTO'
##""",
      this.prefix + """api/register"""
    )
  )

  // @LINE:279
  private[this] lazy val org_shopping_controllers_ListController_getUserLists13_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/user/"), DynamicPart("userId", """[^/]+""",true), StaticPart("/lists")))
  )
  private[this] lazy val org_shopping_controllers_ListController_getUserLists13_invoker = createInvoker(
    ListController_2.get.getUserLists(fakeValue[String], fakeValue[Int], fakeValue[Int]),
    HandlerDef(this.getClass.getClassLoader,
      "router",
      "org.shopping.controllers.ListController",
      "getUserLists",
      Seq(classOf[String], classOf[Int], classOf[Int]),
      "GET",
      """##
 tags:
   - User operations
 summary: Get public lists by userId
 produces:
   - application/json
 parameters:
   - name: "Authorization"
     in: "header"
     description: "Authorization code"
     required: true
     type: "string"
 responses:
   200:
     schema:
       """ + "$" + """ref: '#/definitions/org.shopping.dto.ListsDTO'
   401:
     description: Unauthorized
     schema:
       """ + "$" + """ref: '#/definitions/org.shopping.dto.ErrorDTO'
   404:
     description: Entity not found
     schema:
       """ + "$" + """ref: '#/definitions/org.shopping.dto.ErrorDTO'
##""",
      this.prefix + """api/user/""" + "$" + """userId<[^/]+>/lists"""
    )
  )

  // @LINE:282
  private[this] lazy val controllers_Assets_at14_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("app/"), DynamicPart("file", """.+""",false)))
  )
  private[this] lazy val controllers_Assets_at14_invoker = createInvoker(
    Assets_4.at(fakeValue[String], fakeValue[String]),
    HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.Assets",
      "at",
      Seq(classOf[String], classOf[String]),
      "GET",
      """## NoDocs ###""",
      this.prefix + """app/""" + "$" + """file<.+>"""
    )
  )

  // @LINE:284
  private[this] lazy val controllers_Assets_at15_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("public.html")))
  )
  private[this] lazy val controllers_Assets_at15_invoker = createInvoker(
    Assets_4.at(fakeValue[String], fakeValue[String]),
    HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.Assets",
      "at",
      Seq(classOf[String], classOf[String]),
      "GET",
      """## NoDocs ###""",
      this.prefix + """public.html"""
    )
  )

  // @LINE:286
  private[this] lazy val controllers_Assets_at16_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("autologin.html")))
  )
  private[this] lazy val controllers_Assets_at16_invoker = createInvoker(
    Assets_4.at(fakeValue[String], fakeValue[String]),
    HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.Assets",
      "at",
      Seq(classOf[String], classOf[String]),
      "GET",
      """## NoDocs ###""",
      this.prefix + """autologin.html"""
    )
  )

  // @LINE:288
  private[this] lazy val controllers_Assets_at17_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("index.html")))
  )
  private[this] lazy val controllers_Assets_at17_invoker = createInvoker(
    Assets_4.at(fakeValue[String], fakeValue[String]),
    HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.Assets",
      "at",
      Seq(classOf[String], classOf[String]),
      "GET",
      """## NoDocs ###""",
      this.prefix + """index.html"""
    )
  )

  // @LINE:291
  private[this] lazy val controllers_Assets_at18_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("docs/swagger-ui/"), DynamicPart("file", """.+""",false)))
  )
  private[this] lazy val controllers_Assets_at18_invoker = createInvoker(
    Assets_4.at(fakeValue[String], fakeValue[String]),
    HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.Assets",
      "at",
      Seq(classOf[String], classOf[String]),
      "GET",
      """## NoDocs ###""",
      this.prefix + """docs/swagger-ui/""" + "$" + """file<.+>"""
    )
  )

  // @LINE:293
  private[this] lazy val controllers_Assets_versioned19_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("assets/"), DynamicPart("file", """.+""",false)))
  )
  private[this] lazy val controllers_Assets_versioned19_invoker = createInvoker(
    Assets_4.versioned(fakeValue[String], fakeValue[Asset]),
    HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.Assets",
      "versioned",
      Seq(classOf[String], classOf[Asset]),
      "GET",
      """## NoDocs ###""",
      this.prefix + """assets/""" + "$" + """file<.+>"""
    )
  )

  // @LINE:295
  private[this] lazy val controllers_Default_redirect20_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("docs")))
  )
  private[this] lazy val controllers_Default_redirect20_invoker = createInvoker(
    Default_3.redirect(fakeValue[String]),
    HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.Default",
      "redirect",
      Seq(classOf[String]),
      "GET",
      """## NoDocs ###""",
      this.prefix + """docs"""
    )
  )


  def routes: PartialFunction[RequestHeader, Handler] = {
  
    // @LINE:5
    case org_shopping_controllers_MainController_options0_route(params) =>
      call(params.fromPath[String]("path", None)) { (path) =>
        org_shopping_controllers_MainController_options0_invoker.call(MainController_1.get.options(path))
      }
  
    // @LINE:7
    case org_shopping_controllers_UserController_accessToken1_route(params) =>
      call { 
        org_shopping_controllers_UserController_accessToken1_invoker.call(UserController_0.get.accessToken)
      }
  
    // @LINE:9
    case org_shopping_controllers_UserController_loginGet2_route(params) =>
      call { 
        org_shopping_controllers_UserController_loginGet2_invoker.call(UserController_0.get.loginGet)
      }
  
    // @LINE:12
    case org_shopping_controllers_UserController_loginPostForm3_route(params) =>
      call { 
        org_shopping_controllers_UserController_loginPostForm3_invoker.call(UserController_0.get.loginPostForm)
      }
  
    // @LINE:43
    case org_shopping_controllers_ListController_insertList4_route(params) =>
      call { 
        org_shopping_controllers_ListController_insertList4_invoker.call(ListController_2.get.insertList)
      }
  
    // @LINE:78
    case org_shopping_controllers_ListController_updateList5_route(params) =>
      call(params.fromPath[String]("listId", None)) { (listId) =>
        org_shopping_controllers_ListController_updateList5_invoker.call(ListController_2.get.updateList(listId))
      }
  
    // @LINE:102
    case org_shopping_controllers_ListController_deleteList6_route(params) =>
      call(params.fromPath[String]("listId", None)) { (listId) =>
        org_shopping_controllers_ListController_deleteList6_invoker.call(ListController_2.get.deleteList(listId))
      }
  
    // @LINE:137
    case org_shopping_controllers_ListController_addListItems7_route(params) =>
      call(params.fromPath[String]("listId", None)) { (listId) =>
        org_shopping_controllers_ListController_addListItems7_invoker.call(ListController_2.get.addListItems(listId))
      }
  
    // @LINE:164
    case org_shopping_controllers_ListController_getListItems8_route(params) =>
      call(params.fromPath[String]("listId", None)) { (listId) =>
        org_shopping_controllers_ListController_getListItems8_invoker.call(ListController_2.get.getListItems(listId))
      }
  
    // @LINE:184
    case org_shopping_controllers_UserController_login9_route(params) =>
      call { 
        org_shopping_controllers_UserController_login9_invoker.call(UserController_0.get.login)
      }
  
    // @LINE:207
    case org_shopping_controllers_UserController_getUserBySession10_route(params) =>
      call { 
        org_shopping_controllers_UserController_getUserBySession10_invoker.call(UserController_0.get.getUserBySession)
      }
  
    // @LINE:230
    case org_shopping_controllers_UserController_searchUsers11_route(params) =>
      call(params.fromQuery[Option[String]]("email", None), params.fromQuery[Option[String]]("nick", None)) { (email, nick) =>
        org_shopping_controllers_UserController_searchUsers11_invoker.call(UserController_0.get.searchUsers(email, nick))
      }
  
    // @LINE:252
    case org_shopping_controllers_UserController_register12_route(params) =>
      call { 
        org_shopping_controllers_UserController_register12_invoker.call(UserController_0.get.register)
      }
  
    // @LINE:279
    case org_shopping_controllers_ListController_getUserLists13_route(params) =>
      call(params.fromPath[String]("userId", None), params.fromQuery[Int]("offset", None), params.fromQuery[Int]("count", None)) { (userId, offset, count) =>
        org_shopping_controllers_ListController_getUserLists13_invoker.call(ListController_2.get.getUserLists(userId, offset, count))
      }
  
    // @LINE:282
    case controllers_Assets_at14_route(params) =>
      call(Param[String]("path", Right("/public/client/app")), params.fromPath[String]("file", None)) { (path, file) =>
        controllers_Assets_at14_invoker.call(Assets_4.at(path, file))
      }
  
    // @LINE:284
    case controllers_Assets_at15_route(params) =>
      call(Param[String]("path", Right("/public/client")), Param[String]("file", Right("public.html"))) { (path, file) =>
        controllers_Assets_at15_invoker.call(Assets_4.at(path, file))
      }
  
    // @LINE:286
    case controllers_Assets_at16_route(params) =>
      call(Param[String]("path", Right("/public/client")), Param[String]("file", Right("autologin.html"))) { (path, file) =>
        controllers_Assets_at16_invoker.call(Assets_4.at(path, file))
      }
  
    // @LINE:288
    case controllers_Assets_at17_route(params) =>
      call(Param[String]("path", Right("/public/client")), Param[String]("file", Right("index.html"))) { (path, file) =>
        controllers_Assets_at17_invoker.call(Assets_4.at(path, file))
      }
  
    // @LINE:291
    case controllers_Assets_at18_route(params) =>
      call(Param[String]("path", Right("/public/lib/swagger-ui")), params.fromPath[String]("file", None)) { (path, file) =>
        controllers_Assets_at18_invoker.call(Assets_4.at(path, file))
      }
  
    // @LINE:293
    case controllers_Assets_versioned19_route(params) =>
      call(Param[String]("path", Right("/public")), params.fromPath[Asset]("file", None)) { (path, file) =>
        controllers_Assets_versioned19_invoker.call(Assets_4.versioned(path, file))
      }
  
    // @LINE:295
    case controllers_Default_redirect20_route(params) =>
      call(Param[String]("to", Right("/docs/swagger-ui/index.html?url=/assets/swagger.json"))) { (to) =>
        controllers_Default_redirect20_invoker.call(Default_3.redirect(to))
      }
  }
}
