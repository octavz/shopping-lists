module Main.Models exposing (..)

import List exposing (..)
import Main.Dtos exposing (..)


type Page
    = PageAccessDenied
    | PageLogin
    | PageRegister
    | PageMyAccount
    | PageSuppliers
    | PageNotFound
    | PageHome


type alias UserModel =
    { content : UpdateUserDTO
    , key : Maybe String
    }


type alias AccountModel =
    { content : UpdateUserDTO
    , confirm : Maybe String
    , message : Maybe String
    }


type alias HomeModel =
    { content : Maybe ListDTO
    , newItem : Maybe ListItemDTO
    , message : Maybe String
    }


type alias LoginModel =
    { content : LoginDTO
    , signinAttempts : Int
    , message : Maybe String
    }


type alias RegisterModel =
    { content : UpdateUserDTO
    , confirm : Maybe String
    , message : Maybe String
    }


type alias SupplierModel =
    { content : SuppliersDTO
    , current : Maybe SupplierItemDTO
    , message : Maybe String
    }


type alias Model =
    { userData : UserModel
    , loginView : LoginModel
    , registerView : RegisterModel
    , accountView : AccountModel
    , supplierView : SupplierModel
    , homeView : HomeModel
    , activePage : Page
    , sync : SyncDTO
    }


emptyUserUpdate : UpdateUserDTO
emptyUserUpdate =
    UpdateUserDTO Nothing Nothing Nothing Nothing


emptyListItem : ListItemDTO
emptyListItem =
    ListItemDTO Nothing 0 Nothing 0 Nothing 0


initUserModel : UserModel
initUserModel =
    { content = emptyUserUpdate
    , key = Nothing
    }


initLoginView : LoginModel
initLoginView =
    { content =
        { login = "aaa@aaa.com"
        , password = "123456"
        }
    , signinAttempts = 0
    , message = Nothing
    }


initRegisterView : RegisterModel
initRegisterView =
    { content = emptyUserUpdate
    , confirm = Nothing
    , message = Nothing
    }


initAccountView : AccountModel
initAccountView =
    { content = emptyUserUpdate
    , confirm = Nothing
    , message = Nothing
    }


initialModel : Model
initialModel =
    { userData = initUserModel
    , loginView = initLoginView
    , registerView = initRegisterView
    , accountView = initAccountView
    , supplierView = SupplierModel (SuppliersDTO []) Nothing Nothing
    , homeView = HomeModel Nothing Nothing Nothing
    , activePage = PageHome
    , sync = SyncDTO Nothing Nothing Nothing Nothing
    }
