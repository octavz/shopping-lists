module Main.Models exposing (..)

import Login.Model exposing (..)
import Register.Model exposing (..)
import Account.Model exposing (..)
import Supplier.Model exposing (..)
import List exposing (..)


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
    , supplierView : SupplierModel
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



--emptySupplierView = SupplierModel [] emptySupplierItem Nothing


emptySupplierItem : SupplierItemModel
emptySupplierItem =
    SupplierItemModel Nothing "" Nothing


emptySupplierView : SupplierModel
emptySupplierView =
    let
        elem i =
            let
                s =
                    (toString i)
            in
                SupplierItemModel (Just s) ("test " ++ s) (Just ("description " ++ s))
    in
        SupplierModel (map elem (List.range 1 5)) emptySupplierItem Nothing


emptyLoginView : LoginModel
emptyLoginView =
    { login = "aaa@aaa.com"
    , password = "123456"
    , signinAttempts = 0
    , message = Nothing
    }


initialModel : Model
initialModel =
    { userData =
        { login = "aaa@aaa.com"
        , name = "aaa"
        , key = Just "Yzg0YmVjYzgtYjA4Mi00NjM5LWJmNWYtYjAyNjVkNzk0NDQw"
        }
    , lists = []
    , loginView = emptyLoginView
    , registerView = RegisterModel "a1@aaa.com" "123456" "123456" Nothing
    , supplierView = SupplierModel [] emptySupplierItem Nothing
    , activePage = PageLogin
    }
