module Repository exposing (login, register, suppliers)

import Task
import Login.Model exposing (..)
import Register.Model exposing (..)
import Account.Model exposing (..)
import Supplier.Model exposing (..)
import Login.Messages as Login
import Register.Messages as Register
import Supplier.Messages as Supplier
import HttpClient exposing (..)


login : LoginModel -> Cmd Login.LoginMsg
login model =
    Task.perform Login.ServerError Login.FetchSuccess (postLogin model)


register : RegisterModel -> Cmd Register.RegisterMsg
register model =
    Task.perform Register.ServerError Register.FetchSuccess (postRegister model)


suppliers : UserModel -> Cmd Supplier.SupplierMsg
suppliers model =
    Task.perform Supplier.ServerError Supplier.SuppliersResp (getSuppliers model)


