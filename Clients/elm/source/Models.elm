module Models exposing (..)


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
    , activePage : Page
    }


type alias LoginModel =
    { login : String
    , password : String
    , signinAttempts : Int
    , message : Maybe String
    }


type alias UserModel =
    { login : String
    , name : String
    , key : Maybe String
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


emptyLoginModel : LoginModel
emptyLoginModel =
    { login = "aaa@aaa.com"
    , password = "123456"
    , signinAttempts = 0
    , message = Nothing
    }
