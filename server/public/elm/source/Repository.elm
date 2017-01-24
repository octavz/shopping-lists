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
import Json.Decode as Decode
import Debug

baseUrl =
    "http://localhost:9000/api/"

loginUrl =
    baseUrl ++ "login"

registerUrl =
    baseUrl ++ "register"

suppliersUrl =
    baseUrl ++ "suppliers"

defaultHeaders : Maybe UserModel -> List Http.Header
defaultHeaders optUser =
    let
        h =
            [ Http.header "Content-Type" "application/json" ,Http.header "Accept" "application/json"  ]
    in
        case optUser of
            Just user ->
                case user.key of
                    Just k ->
                        (Http.header "Authorization" ("Bearer " ++ k) ) :: h

                    Nothing ->
                        h

            Nothing ->
                h

httpPost : Maybe UserModel -> String -> Http.Body -> Decode.Decoder a -> Http.Request a
httpPost optUser url body decoder =
  Http.request
    { method = "POST"
    , headers = defaultHeaders optUser
    , url = url
    , body = body
    , expect = Http.expectJson decoder
    , timeout = Nothing
    , withCredentials = False
    }

httpGet : Maybe UserModel -> String -> Decode.Decoder a -> Http.Request a
httpGet optUser url decoder =
  Http.request
    { method = "GET"
    , headers = defaultHeaders optUser
    , url = url
    , body = Http.emptyBody
    , expect = Http.expectJson decoder
    , timeout = Nothing
    , withCredentials = False
    }

login : LoginModel -> Cmd LoginMsg
login model =
  let
    body = Debug.log "body" <| Http.jsonBody (Json.loginDto model)
  in
    Http.send PostLogin (httpPost Nothing loginUrl body Json.userDecoder)

register : RegisterModel -> Cmd RegisterMsg
register model =
  let
    body = Http.jsonBody (Json.registerDto model)
  in
    Http.send PostRegister (httpPost Nothing registerUrl body Json.userDecoder)


suppliers : UserModel -> Cmd SupplierMsg
suppliers model =
    Http.send SuppliersResp (httpGet (Just model) suppliersUrl Json.suppliersDecoder)
