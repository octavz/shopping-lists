module Main.Messages exposing (..)

import Login.Messages as Login
import Register.Messages as Register
import Main.Models exposing (..)


type Msg
    = Login Login.LoginMsg
    | Register Register.RegisterMsg
    | SetActivePage Page
