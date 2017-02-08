module Repository exposing (login, register, suppliers)

import Http
import Main.Models exposing (..)
import Json
import Login.Messages exposing (..)
import Main.Messages exposing (..)
import Register.Messages exposing (..)
import Supplier.Messages exposing (..)
import Account.Messages exposing (..)
import Json.Decode as Decode
import Main.Dtos exposing (..)
import Debug


baseUrl =
    "http://localhost:9000/api/"


loginUrl =
    baseUrl ++ "login"


registerUrl =
    baseUrl ++ "register"

accountUrl =
    baseUrl ++ "user"

suppliersUrl =
    baseUrl ++ "suppliers"

syncUrl =
    baseUrl ++ "data"

defaultHeaders : Maybe UserModel -> List Http.Header
defaultHeaders optUser =
    let
        h =
            [ Http.header "Content-Type" "application/json", Http.header "Accept" "application/json" ]
    in
        case optUser of
            Just user ->
                case user.key of
                    Just k ->
                        (Http.header "Authorization" ("Bearer " ++ k)) :: h

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

httpPut : Maybe UserModel -> String -> Http.Body -> Decode.Decoder a -> Http.Request a
httpPut optUser url body decoder =
    Http.request
        { method = "PUT"
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


sync : UserModel -> SyncDTO -> Cmd Msg
sync user sync =
    let
        body =
            Debug.log "body" <| Http.jsonBody (Json.syncEncoder sync)
    in
        Http.send Sync (httpPut (Just user) syncUrl body Json.syncDecoder)

login : LoginDTO -> Cmd LoginMsg
login model =
    let
        body =
            Debug.log "body" <| Http.jsonBody (Json.loginEncoder model)
    in
        Http.send PostLogin (httpPost Nothing loginUrl body Json.userDecoder)


register : RegisterModel -> Cmd RegisterMsg
register model =
    let
        body =
            Http.jsonBody (Json.registerEncoder model)
    in
        Http.send PostRegister (httpPost Nothing registerUrl body Json.userDecoder)


--saveAccount : AccountModel -> Cmd AccountMsg
--saveAccount model =
--    let
--        body =
--            Http.jsonBody (Json.accountEncoder model)
--    in
--        Http.send PostAccount (httpPut Nothing accountUrl body Json.userDecoder)


suppliers : UserModel -> Cmd SupplierMsg
suppliers model =
    Http.send SuppliersResp (httpGet (Just model) suppliersUrl Json.suppliersDecoder)
