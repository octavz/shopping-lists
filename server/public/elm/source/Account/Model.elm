module Account.Model exposing (..)


type alias UserModel =
    { login : String
    , name : String
    , key : Maybe String
    }
