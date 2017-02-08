module Main.Messages exposing (..)

import Login.Messages as Login
import Register.Messages as Register
import Supplier.Messages as Supplier
import Account.Messages as Account
import Home.Messages as Home
import Main.Dtos exposing (..)
import Main.Models exposing (..)
import Time exposing (..)
import Http

type Msg
    = Login Login.LoginMsg
    | Register Register.RegisterMsg
    | Supplier Supplier.SupplierMsg
    | Account Account.AccountMsg
    | SetActivePage Page
    | Home Home.HomeMsg
    | Sync (Result Http.Error SyncDTO)
    | SyncData Time
