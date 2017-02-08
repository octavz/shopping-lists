module Login.Messages exposing (..)

import Http exposing (..)
import Main.Models exposing (..)


type LoginMsg
    = UpdateLogin String
    | UpdatePassword String
    | PostMessage String
    | OnLogin
    | PostLogin (Result Http.Error UserModel)
    | ShowErrors
    | RegisterCmd
