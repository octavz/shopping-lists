module Messages exposing (..)

import Http exposing (..)
import Models exposing (..)


type UserMsg
    = UpdateLogin String
    | UpdatePassword String
    | PostMessage String
    | Fetch
    | FetchSuccess UserModel
    | FetchError Http.Error


type LoginMsg
    = LoginView UserMsg
    | ShowErrors
    | RegisterCmd


type RegisterMsg
    = RegisterView UserMsg
    | UpdateConfirm String


type Msg
    = Login LoginMsg
    | Register RegisterMsg
    | SetActivePage Page
