
package views.html.users

import play.twirl.api._
import play.twirl.api.TemplateMagic._


     object login_Scope0 {
import models._
import controllers._
import play.api.i18n._
import views.html._
import play.api.templates.PlayMagic._
import play.api.mvc._
import play.api.data._

class login extends BaseScalaTemplate[play.twirl.api.HtmlFormat.Appendable,Format[play.twirl.api.HtmlFormat.Appendable]](play.twirl.api.HtmlFormat) with play.twirl.api.Template0[play.twirl.api.HtmlFormat.Appendable] {

  /**/
  def apply():play.twirl.api.HtmlFormat.Appendable = {
    _display_ {
      {


Seq[Any](format.raw/*1.1*/("""<!doctype html>
<html lang="en" ng-app="myAppLogin">
<head>
    <meta charset="utf-8">
    <title>Shopping App Login</title>

    <link rel="stylesheet" href="/app/bootstrap/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="/app/bootstrap/css/bootstrap-theme.min.css"/>
    <link rel="stylesheet" href="/app/css/app.css"/>
</head>
<body>

<script src="/app/lib/jquery-2.1.0.min.js"></script>
<script src="/app/bootstrap/js/bootstrap.js"></script>
<script src="/app/lib/angular/angular.js"></script>
<script src="/app/lib/angular/angular-resource.js"></script>
<script src="/app/lib/underscore/underscore.js"></script>
<script src="/app/lib/underscore/angular-underscore.js"></script>

<script src="/app/js/appLogin.js"></script>

<div class="container">
    <div class="col-sm-8 col-sm-offset-2">

        <div class="Login">
            <h4>Login</h4>

            <form name="loginForm" novalidate action="/login" method="POST">

                <div class="form-group"
                     ng-class=""""),format.raw/*31.32*/("""{"""),format.raw/*31.33*/(""" """),format.raw/*31.34*/("""'has-error' : loginForm.email.$invalid && !loginForm.email.$pristine """),format.raw/*31.103*/("""}"""),format.raw/*31.104*/("""">
                    <label for="username">Email</label>
                    <input type="email" name="username" id="username" class="form-control" ng-model="user.Email" required/>
                    <p ng-show="loginForm.email.$invalid && !loginForm.email.$pristine" class="help-block">Invalid
                        Email.</p>
                </div>

                <div class="form-group"
                     ng-class=""""),format.raw/*39.32*/("""{"""),format.raw/*39.33*/(""" """),format.raw/*39.34*/("""'has-error' : loginForm.password.$invalid && !loginForm.password.$pristine """),format.raw/*39.109*/("""}"""),format.raw/*39.110*/("""">
                    <label for="password">Password</label>
                    <input type="password" id="password" name="password" class="form-control" ng-model="user.Password" required
                           ng-minlength="6" id="PasswordID"/>

                    <p ng-show="loginForm.password.$error.minlength" class="help-block">Password is too short.</p>
                </div>
                <input type="hidden" value="1" name="client_id"/>
                <input type="hidden" value="password" name="grant_type"/>
                <input type="hidden" value="secret" name="client_secret"/>
                <input id="login" name="login" type="submit" class="btn btn-primary" ng-disabled="loginForm.$invalid" value="Sign In"/>
                <a href="/index.html#/register">
                    <input type="button" value="Register" class="btn btn-primary"/>
                </a>
            </form>

        </div>

    </div>
</div>

</body>
</html>
"""))
      }
    }
  }

  def render(): play.twirl.api.HtmlFormat.Appendable = apply()

  def f:(() => play.twirl.api.HtmlFormat.Appendable) = () => apply()

  def ref: this.type = this

}


}

/**/
object login extends login_Scope0.login
              /*
                  -- GENERATED --
                  DATE: Tue Oct 04 09:41:02 EEST 2016
                  SOURCE: /home/octav/projects/shopping-list/server/app/views/users/login.scala.html
                  HASH: fd3b6694ca2c0a6aafada45db6c19d84f336ab68
                  MATRIX: 615->0|1654->1011|1683->1012|1712->1013|1810->1082|1840->1083|2296->1511|2325->1512|2354->1513|2458->1588|2488->1589
                  LINES: 25->1|55->31|55->31|55->31|55->31|55->31|63->39|63->39|63->39|63->39|63->39
                  -- GENERATED --
              */
          