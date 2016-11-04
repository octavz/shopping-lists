module Register.Model exposing (..)


type alias RegisterModel =
    { login : String
    , password : String
    , confirm : String
    , message : Maybe String
    }
