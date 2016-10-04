
// @GENERATOR:play-routes-compiler
// @SOURCE:/home/octav/projects/shopping-list/server/conf/routes
// @DATE:Tue Oct 04 09:41:02 EEST 2016


package router {
  object RoutesPrefix {
    private var _prefix: String = "/"
    def setPrefix(p: String): Unit = {
      _prefix = p
    }
    def prefix: String = _prefix
    val byNamePrefix: Function0[String] = { () => prefix }
  }
}
