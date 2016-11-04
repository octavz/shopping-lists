module Main.Models exposing (..)

import Login.Model exposing (..)
import Register.Model exposing (..)
import Account.Model exposing(..)
import Supplier.Model exposing(..)

type Page
    = PageAccessDenied
    | PageLogin
    | PageRegister
    | PageMyAccount
    | PageSuppliers
    | PageNotFound


type alias Model =
    { userData : UserModel
    , lists : List ShopList
    , loginView : LoginModel
    , registerView : RegisterModel
    , supplierView: SupplierModel
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
        { login = "aaa@aaa.com"
        , name = "aaa"
        , key = Just "Yzg0YmVjYzgtYjA4Mi00NjM5LWJmNWYtYjAyNjVkNzk0NDQw"
        }
    , lists = []
    , loginView = emptyLoginModel
    , registerView = RegisterModel "a1@aaa.com" "123456" "123456" Nothing
    , supplierView = emptySupplierView
    , activePage = PageLogin
    }
