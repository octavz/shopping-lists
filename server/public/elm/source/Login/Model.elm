module Login.Model exposing (..)


type alias LoginModel =
    { login : String
    , password : String
    , signinAttempts : Int
    , message : Maybe String
    }
