
// @GENERATOR:play-routes-compiler
// @SOURCE:/home/octav/projects/shopping-list/server/conf/routes
// @DATE:Tue Oct 04 09:41:02 EEST 2016

import play.api.mvc.{ QueryStringBindable, PathBindable, Call, JavascriptLiteral }
import play.core.routing.{ HandlerDef, ReverseRouteContext, queryString, dynamicString }


import _root_.controllers.Assets.Asset

// @LINE:5
package org.shopping.controllers {

  // @LINE:5
  class ReverseMainController(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:5
    def options(path:String): Call = {
      import ReverseRouteContext.empty
      Call("OPTIONS", _prefix + { _defaultPrefix } + implicitly[PathBindable[String]].unbind("path", path))
    }
  
  }

  // @LINE:7
  class ReverseUserController(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:230
    def searchUsers(email:Option[String], nick:Option[String]): Call = {
      import ReverseRouteContext.empty
      Call("GET", _prefix + { _defaultPrefix } + "api/users" + queryString(List(Some(implicitly[QueryStringBindable[Option[String]]].unbind("email", email)), Some(implicitly[QueryStringBindable[Option[String]]].unbind("nick", nick)))))
    }
  
    // @LINE:207
    def getUserBySession(): Call = {
      import ReverseRouteContext.empty
      Call("GET", _prefix + { _defaultPrefix } + "api/user")
    }
  
    // @LINE:9
    def loginGet(): Call = {
      import ReverseRouteContext.empty
      Call("GET", _prefix + { _defaultPrefix } + "login")
    }
  
    // @LINE:12
    def loginPostForm(): Call = {
      import ReverseRouteContext.empty
      Call("POST", _prefix + { _defaultPrefix } + "login")
    }
  
    // @LINE:252
    def register(): Call = {
      import ReverseRouteContext.empty
      Call("POST", _prefix + { _defaultPrefix } + "api/register")
    }
  
    // @LINE:7
    def accessToken(): Call = {
      import ReverseRouteContext.empty
      Call("POST", _prefix + { _defaultPrefix } + "api/oauth2/access_token")
    }
  
    // @LINE:184
    def login(): Call = {
      import ReverseRouteContext.empty
      Call("POST", _prefix + { _defaultPrefix } + "api/login")
    }
  
  }

  // @LINE:43
  class ReverseListController(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:137
    def addListItems(listId:String): Call = {
      import ReverseRouteContext.empty
      Call("POST", _prefix + { _defaultPrefix } + "api/list/" + implicitly[PathBindable[String]].unbind("listId", dynamicString(listId)) + "/items")
    }
  
    // @LINE:102
    def deleteList(listId:String): Call = {
      import ReverseRouteContext.empty
      Call("DELETE", _prefix + { _defaultPrefix } + "api/list/" + implicitly[PathBindable[String]].unbind("listId", dynamicString(listId)))
    }
  
    // @LINE:78
    def updateList(listId:String): Call = {
      import ReverseRouteContext.empty
      Call("PUT", _prefix + { _defaultPrefix } + "api/list/" + implicitly[PathBindable[String]].unbind("listId", dynamicString(listId)))
    }
  
    // @LINE:279
    def getUserLists(userId:String, offset:Int, count:Int): Call = {
      import ReverseRouteContext.empty
      Call("GET", _prefix + { _defaultPrefix } + "api/user/" + implicitly[PathBindable[String]].unbind("userId", dynamicString(userId)) + "/lists" + queryString(List(Some(implicitly[QueryStringBindable[Int]].unbind("offset", offset)), Some(implicitly[QueryStringBindable[Int]].unbind("count", count)))))
    }
  
    // @LINE:164
    def getListItems(listId:String): Call = {
      import ReverseRouteContext.empty
      Call("GET", _prefix + { _defaultPrefix } + "api/list/" + implicitly[PathBindable[String]].unbind("listId", dynamicString(listId)) + "/items")
    }
  
    // @LINE:43
    def insertList(): Call = {
      import ReverseRouteContext.empty
      Call("POST", _prefix + { _defaultPrefix } + "api/list")
    }
  
  }


}
