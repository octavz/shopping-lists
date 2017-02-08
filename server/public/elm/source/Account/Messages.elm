module Account.Messages exposing (..)

import Main.Models exposing (..)

type AccountMsg
    = UpdateLogin String
    | UpdateName String
    | UpdatePassword String
    | UpdateConfirm String
    | PostMessage String
    | OnAccount
