module Main.Models exposing (..)

import Login.Model exposing (..)
import Register.Model exposing (..)
import Account.Model exposing(..)


type Page
    = PageAccessDenied
    | PageLogin
    | PageRegister
    | PageMyAccount
    | PageNotFound


type alias Model =
    { userData : UserModel
    , lists : List ShopList
    , loginView : LoginModel
    , registerView : RegisterModel
    , activePage : Page
    }

type alias ShopList =
    { id : String
    , name : String
    , created : Int
    , items : List ShopListItem
    }


type alias ShopListItem =
    { id : String
    , name : String
    , quantity : Int
    }


initialModel : Model
initialModel =
    { userData =
        { login = ""
        , name = ""
        , key = Nothing
        }
    , lists = []
    , loginView = emptyLoginModel
    , registerView = RegisterModel "a1@aaa.com" "123456" "123456" Nothing
    , activePage = PageLogin
    }
