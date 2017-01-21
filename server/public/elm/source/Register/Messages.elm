module Register.Messages exposing (..)

import Http exposing (..)
import Account.Model exposing (..)

type RegisterMsg
    = UpdateLogin String
    | UpdatePassword String
    | UpdateConfirm String
    | PostMessage String
    | OnRegister
    | PostRegister (Result Http.Error UserModel)
