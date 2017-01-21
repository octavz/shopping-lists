module Repository exposing (login, register, suppliers)

import Http
import Login.Model exposing (..)
import Register.Model exposing (..)
import Account.Model exposing (..)
import Supplier.Model exposing (..)
import Json
import Login.Messages exposing (..)
import Register.Messages exposing (..)
import Supplier.Messages exposing (..)

baseUrl =
    "http://localhost:9000/api/"

loginUrl =
    baseUrl ++ "login"

registerUrl =
    baseUrl ++ "register"

suppliersUrl =
    baseUrl ++ "suppliers"

login : LoginModel -> Cmd LoginMsg
login model =
  let
    body = Http.jsonBody (Json.loginDto model)
  in
    Http.send PostLogin (Http.post loginUrl body Json.userDecoder)

register : RegisterModel -> Cmd RegisterMsg
register model =
  let
    body = Http.jsonBody (Json.registerDto model)
  in
    Http.send PostRegister (Http.post registerUrl body Json.userDecoder)


suppliers : UserModel -> Cmd SupplierMsg
suppliers model =
    Http.send SuppliersResp (Http.get suppliersUrl Json.suppliersDecoder)
