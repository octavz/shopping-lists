module Models exposing (..)


type alias Model =
    { userData : UserModel
    , lists : List ShopList
    , loginView : LoginViewModel
    }


type alias LoginViewModel =
    { login : String
    , password : String
    , signinAttempts : Int
    , message : Maybe String
    }


type alias UserModel =
    { login : String
    , name : String
    , key : String
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


emptyLoginModel : LoginViewModel
emptyLoginModel =
    { login = "aaa@aaa.com"
    , password = "123456"
    , signinAttempts = 0
    , message = Nothing
    }
