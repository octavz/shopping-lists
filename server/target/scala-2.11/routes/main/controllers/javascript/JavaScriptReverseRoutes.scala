
// @GENERATOR:play-routes-compiler
// @SOURCE:/home/octav/projects/shopping-list/server/conf/routes
// @DATE:Tue Oct 04 09:41:02 EEST 2016

import play.api.routing.JavaScriptReverseRoute
import play.api.mvc.{ QueryStringBindable, PathBindable, Call, JavascriptLiteral }
import play.core.routing.{ HandlerDef, ReverseRouteContext, queryString, dynamicString }


import _root_.controllers.Assets.Asset

// @LINE:282
package controllers.javascript {
  import ReverseRouteContext.empty

  // @LINE:282
  class ReverseAssets(_prefix: => String) {

    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:282
    def at: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.Assets.at",
      """
        function(path0,file1) {
        
          if (path0 == """ + implicitly[JavascriptLiteral[String]].to("/public/client/app") + """) {
            return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "app/" + (""" + implicitly[PathBindable[String]].javascriptUnbind + """)("file", file1)})
          }
        
          if (path0 == """ + implicitly[JavascriptLiteral[String]].to("/public/client") + """ && file1 == """ + implicitly[JavascriptLiteral[String]].to("public.html") + """) {
            return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "public.html"})
          }
        
          if (path0 == """ + implicitly[JavascriptLiteral[String]].to("/public/client") + """ && file1 == """ + implicitly[JavascriptLiteral[String]].to("autologin.html") + """) {
            return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "autologin.html"})
          }
        
          if (path0 == """ + implicitly[JavascriptLiteral[String]].to("/public/client") + """ && file1 == """ + implicitly[JavascriptLiteral[String]].to("index.html") + """) {
            return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "index.html"})
          }
        
          if (path0 == """ + implicitly[JavascriptLiteral[String]].to("/public/lib/swagger-ui") + """) {
            return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "docs/swagger-ui/" + (""" + implicitly[PathBindable[String]].javascriptUnbind + """)("file", file1)})
          }
        
        }
      """
    )
  
    // @LINE:293
    def versioned: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.Assets.versioned",
      """
        function(file1) {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "assets/" + (""" + implicitly[PathBindable[Asset]].javascriptUnbind + """)("file", file1)})
        }
      """
    )
  
  }

  // @LINE:295
  class ReverseDefault(_prefix: => String) {

    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:295
    def redirect: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.Default.redirect",
      """
        function() {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "docs"})
        }
      """
    )
  
  }


}
