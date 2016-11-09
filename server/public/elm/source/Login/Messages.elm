module Login.Messages exposing (..)

import Http exposing (..)
import Account.Model exposing (..)


type LoginMsg
    = UpdateLogin String
    | UpdatePassword String
    | PostMessage String
    | Fetch
    | FetchSuccess UserModel
    | FetchError Http.Error
    | ShowErrors
    | RegisterCmd