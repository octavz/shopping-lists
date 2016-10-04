
// @GENERATOR:play-routes-compiler
// @SOURCE:/home/octav/projects/shopping-list/server/conf/routes
// @DATE:Tue Oct 04 09:41:02 EEST 2016

package org.shopping.controllers;

import router.RoutesPrefix;

public class routes {
  
  public static final org.shopping.controllers.ReverseMainController MainController = new org.shopping.controllers.ReverseMainController(RoutesPrefix.byNamePrefix());
  public static final org.shopping.controllers.ReverseUserController UserController = new org.shopping.controllers.ReverseUserController(RoutesPrefix.byNamePrefix());
  public static final org.shopping.controllers.ReverseListController ListController = new org.shopping.controllers.ReverseListController(RoutesPrefix.byNamePrefix());

  public static class javascript {
    
    public static final org.shopping.controllers.javascript.ReverseMainController MainController = new org.shopping.controllers.javascript.ReverseMainController(RoutesPrefix.byNamePrefix());
    public static final org.shopping.controllers.javascript.ReverseUserController UserController = new org.shopping.controllers.javascript.ReverseUserController(RoutesPrefix.byNamePrefix());
    public static final org.shopping.controllers.javascript.ReverseListController ListController = new org.shopping.controllers.javascript.ReverseListController(RoutesPrefix.byNamePrefix());
  }

}
