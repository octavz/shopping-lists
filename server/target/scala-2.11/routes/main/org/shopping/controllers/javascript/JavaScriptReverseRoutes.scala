
// @GENERATOR:play-routes-compiler
// @SOURCE:/home/octav/projects/shopping-list/server/conf/routes
// @DATE:Tue Oct 04 09:41:02 EEST 2016

import play.api.routing.JavaScriptReverseRoute
import play.api.mvc.{ QueryStringBindable, PathBindable, Call, JavascriptLiteral }
import play.core.routing.{ HandlerDef, ReverseRouteContext, queryString, dynamicString }


import _root_.controllers.Assets.Asset

// @LINE:5
package org.shopping.controllers.javascript {
  import ReverseRouteContext.empty

  // @LINE:5
  class ReverseMainController(_prefix: => String) {

    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:5
    def options: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "org.shopping.controllers.MainController.options",
      """
        function(path0) {
          return _wA({method:"OPTIONS", url:"""" + _prefix + { _defaultPrefix } + """" + (""" + implicitly[PathBindable[String]].javascriptUnbind + """)("path", path0)})
        }
      """
    )
  
  }

  // @LINE:7
  class ReverseUserController(_prefix: => String) {

    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:230
    def searchUsers: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "org.shopping.controllers.UserController.searchUsers",
      """
        function(email0,nick1) {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "api/users" + _qS([(""" + implicitly[QueryStringBindable[Option[String]]].javascriptUnbind + """)("email", email0), (""" + implicitly[QueryStringBindable[Option[String]]].javascriptUnbind + """)("nick", nick1)])})
        }
      """
    )
  
    // @LINE:207
    def getUserBySession: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "org.shopping.controllers.UserController.getUserBySession",
      """
        function() {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "api/user"})
        }
      """
    )
  
    // @LINE:9
    def loginGet: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "org.shopping.controllers.UserController.loginGet",
      """
        function() {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "login"})
        }
      """
    )
  
    // @LINE:12
    def loginPostForm: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "org.shopping.controllers.UserController.loginPostForm",
      """
        function() {
          return _wA({method:"POST", url:"""" + _prefix + { _defaultPrefix } + """" + "login"})
        }
      """
    )
  
    // @LINE:252
    def register: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "org.shopping.controllers.UserController.register",
      """
        function() {
          return _wA({method:"POST", url:"""" + _prefix + { _defaultPrefix } + """" + "api/register"})
        }
      """
    )
  
    // @LINE:7
    def accessToken: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "org.shopping.controllers.UserController.accessToken",
      """
        function() {
          return _wA({method:"POST", url:"""" + _prefix + { _defaultPrefix } + """" + "api/oauth2/access_token"})
        }
      """
    )
  
    // @LINE:184
    def login: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "org.shopping.controllers.UserController.login",
      """
        function() {
          return _wA({method:"POST", url:"""" + _prefix + { _defaultPrefix } + """" + "api/login"})
        }
      """
    )
  
  }

  // @LINE:43
  class ReverseListController(_prefix: => String) {

    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:137
    def addListItems: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "org.shopping.controllers.ListController.addListItems",
      """
        function(listId0) {
          return _wA({method:"POST", url:"""" + _prefix + { _defaultPrefix } + """" + "api/list/" + (""" + implicitly[PathBindable[String]].javascriptUnbind + """)("listId", encodeURIComponent(listId0)) + "/items"})
        }
      """
    )
  
    // @LINE:102
    def deleteList: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "org.shopping.controllers.ListController.deleteList",
      """
        function(listId0) {
          return _wA({method:"DELETE", url:"""" + _prefix + { _defaultPrefix } + """" + "api/list/" + (""" + implicitly[PathBindable[String]].javascriptUnbind + """)("listId", encodeURIComponent(listId0))})
        }
      """
    )
  
    // @LINE:78
    def updateList: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "org.shopping.controllers.ListController.updateList",
      """
        function(listId0) {
          return _wA({method:"PUT", url:"""" + _prefix + { _defaultPrefix } + """" + "api/list/" + (""" + implicitly[PathBindable[String]].javascriptUnbind + """)("listId", encodeURIComponent(listId0))})
        }
      """
    )
  
    // @LINE:279
    def getUserLists: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "org.shopping.controllers.ListController.getUserLists",
      """
        function(userId0,offset1,count2) {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "api/user/" + (""" + implicitly[PathBindable[String]].javascriptUnbind + """)("userId", encodeURIComponent(userId0)) + "/lists" + _qS([(""" + implicitly[QueryStringBindable[Int]].javascriptUnbind + """)("offset", offset1), (""" + implicitly[QueryStringBindable[Int]].javascriptUnbind + """)("count", count2)])})
        }
      """
    )
  
    // @LINE:164
    def getListItems: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "org.shopping.controllers.ListController.getListItems",
      """
        function(listId0) {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "api/list/" + (""" + implicitly[PathBindable[String]].javascriptUnbind + """)("listId", encodeURIComponent(listId0)) + "/items"})
        }
      """
    )
  
    // @LINE:43
    def insertList: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "org.shopping.controllers.ListController.insertList",
      """
        function() {
          return _wA({method:"POST", url:"""" + _prefix + { _defaultPrefix } + """" + "api/list"})
        }
      """
    )
  
  }


}
