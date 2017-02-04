module Account.Model exposing (..)


type alias UserModel =
    { login : String
    , name : String
    , key : Maybe String
    }

type alias AccountModel =
    { login : String
    , name : String
    , message: Maybe String
    }
