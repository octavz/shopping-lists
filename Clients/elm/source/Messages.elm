module Messages exposing (..)

import Http exposing (..)
import Models exposing (..)


type LoginMsg
    = UpdateLogin String
    | UpdatePassword String
    | PostMessage String
    | ShowErrors
    | Fetch
    | FetchSuccess UserModel
    | FetchError Http.Error


type Msg
    = Login LoginMsg
