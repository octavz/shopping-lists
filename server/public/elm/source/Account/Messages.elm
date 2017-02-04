module Account.Messages exposing (..)

import Http exposing (..)
import Account.Model exposing (..)

type AccountMsg
    = UpdateLogin String
    | UpdatePassword String
    | UpdateConfirm String
    | PostMessage String
    | OnAccount
    | PostAccount (Result Http.Error UserModel)
