module Main.Messages exposing (..)

import Login.Messages as Login
import Register.Messages as Register
import Supplier.Messages as Supplier
import Main.Models exposing (..)


type Msg
    = Login Login.LoginMsg
    | Register Register.RegisterMsg
    | Supplier Supplier.SupplierMsg
    | SetActivePage Page
