module Login.Model exposing (..)


type alias LoginModel =
    { login : String
    , password : String
    , signinAttempts : Int
    , message : Maybe String
    }


emptyLoginModel : LoginModel
emptyLoginModel =
    { login = "aaa@aaa.com"
    , password = "123456"
    , signinAttempts = 0
    , message = Nothing
    }
