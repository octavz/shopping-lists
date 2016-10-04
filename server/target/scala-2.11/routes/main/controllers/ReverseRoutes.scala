
// @GENERATOR:play-routes-compiler
// @SOURCE:/home/octav/projects/shopping-list/server/conf/routes
// @DATE:Tue Oct 04 09:41:02 EEST 2016

import play.api.mvc.{ QueryStringBindable, PathBindable, Call, JavascriptLiteral }
import play.core.routing.{ HandlerDef, ReverseRouteContext, queryString, dynamicString }


import _root_.controllers.Assets.Asset

// @LINE:282
package controllers {

  // @LINE:282
  class ReverseAssets(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:282
    def at(path:String, file:String): Call = {
    
      (path: @unchecked, file: @unchecked) match {
      
        // @LINE:282
        case (path, file) if path == "/public/client/app" =>
          implicit val _rrc = new ReverseRouteContext(Map(("path", "/public/client/app")))
          Call("GET", _prefix + { _defaultPrefix } + "app/" + implicitly[PathBindable[String]].unbind("file", file))
      
        // @LINE:284
        case (path, file) if path == "/public/client" && file == "public.html" =>
          implicit val _rrc = new ReverseRouteContext(Map(("path", "/public/client"), ("file", "public.html")))
          Call("GET", _prefix + { _defaultPrefix } + "public.html")
      
        // @LINE:286
        case (path, file) if path == "/public/client" && file == "autologin.html" =>
          implicit val _rrc = new ReverseRouteContext(Map(("path", "/public/client"), ("file", "autologin.html")))
          Call("GET", _prefix + { _defaultPrefix } + "autologin.html")
      
        // @LINE:288
        case (path, file) if path == "/public/client" && file == "index.html" =>
          implicit val _rrc = new ReverseRouteContext(Map(("path", "/public/client"), ("file", "index.html")))
          Call("GET", _prefix + { _defaultPrefix } + "index.html")
      
        // @LINE:291
        case (path, file) if path == "/public/lib/swagger-ui" =>
          implicit val _rrc = new ReverseRouteContext(Map(("path", "/public/lib/swagger-ui")))
          Call("GET", _prefix + { _defaultPrefix } + "docs/swagger-ui/" + implicitly[PathBindable[String]].unbind("file", file))
      
      }
    
    }
  
    // @LINE:293
    def versioned(file:Asset): Call = {
      implicit val _rrc = new ReverseRouteContext(Map(("path", "/public")))
      Call("GET", _prefix + { _defaultPrefix } + "assets/" + implicitly[PathBindable[Asset]].unbind("file", file))
    }
  
  }

  // @LINE:295
  class ReverseDefault(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:295
    def redirect(): Call = {
      implicit val _rrc = new ReverseRouteContext(Map(("to", "/docs/swagger-ui/index.html?url=/assets/swagger.json")))
      Call("GET", _prefix + { _defaultPrefix } + "docs")
    }
  
  }


}
