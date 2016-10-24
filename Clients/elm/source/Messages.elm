module Messages exposing (..)

import Http exposing (..)


type LoginMsg
    = UpdateLogin String
    | UpdatePassword String
    | PostMessage String
    | ShowErrors
    | Fetch
    | FetchSuccess String
    | FetchError Http.Error


type Msg
    =  Login LoginMsg
