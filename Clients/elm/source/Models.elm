module Models exposing (..)


type alias Model =
    { userData : LoginViewModel
    , lists : List ShopList
    }


type alias LoginViewModel =
    { login : String
    , password : String
    , signinAttempts : Int
    , message : Maybe String
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
    { login = ""
    , password = ""
    , signinAttempts = 0
    , message = Nothing
    }
