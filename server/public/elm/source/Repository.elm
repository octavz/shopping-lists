module Repository exposing (login, register, suppliers)

import Http
import Json.Encode as Encode exposing (encode)
import Json.Decode as Decode exposing ((:=))
import Task
import Login.Model exposing (..)
import Register.Model exposing (..)
import Account.Model exposing (..)
import Supplier.Model exposing (..)
import Login.Messages as Login
import Register.Messages as Register
import Supplier.Messages as Supplier


baseUrl =
    "http://localhost:9000/api/"


loginUrl =
    baseUrl ++ "login"


registerUrl =
    baseUrl ++ "register"


suppliersUrl =
    baseUrl ++ "suppliers"


defaultHeaders : Maybe UserModel -> List ( String, String )
defaultHeaders optUser =
    let
        h =
            [ ( "Content-Type", "application/json" ), ( "Accept", "application/json" ) ]
    in
        case optUser of
            Just user ->
                case user.key of
                    Just k ->
                        ( "Authorization", "Bearer " ++ k ) :: h

                    Nothing ->
                        h

            Nothing ->
                h


httpPost : Maybe UserModel -> String -> Http.Body -> Task.Task Http.RawError Http.Response
httpPost optUser url body =
    Http.send Http.defaultSettings
        { verb = "POST"
        , headers = defaultHeaders optUser
        , url = url
        , body = body
        }


httpGet : Maybe UserModel -> String -> Task.Task Http.RawError Http.Response
httpGet optUser url =
    Http.send Http.defaultSettings
        { verb = "GET"
        , headers = defaultHeaders optUser
        , url = url
        , body = Http.empty
        }


loginDto : LoginModel -> String
loginDto model =
    Encode.encode 0
        (Encode.object
            [ ( "login", Encode.string model.login )
            , ( "password", Encode.string model.password )
            , ( "clientId", Encode.string "1" )
            , ( "grantType", Encode.string "password" )
            , ( "clientSecret", Encode.string "secret" )
            ]
        )


registerDto : RegisterModel -> String
registerDto model =
    Encode.encode 0
        (Encode.object
            [ ( "login", Encode.string model.login )
            , ( "password", Encode.string model.password )
            ]
        )


userDecoder : Decode.Decoder UserModel
userDecoder =
    Decode.object3 UserModel
        ("login" := Decode.string)
        ("nick" := Decode.string)
        ("accessToken" := Decode.maybe Decode.string)


supplierItemDecoder : Decode.Decoder SupplierItemModel
supplierItemDecoder =
    Decode.object3 SupplierItemModel
        ("id" := Decode.maybe Decode.string)
        ("name" := Decode.string)
        ("description" := Decode.maybe Decode.string)


suppliersDecoder : Decode.Decoder (List SupplierItemModel)
suppliersDecoder =
         Decode.list supplierItemDecoder


login : LoginModel -> Cmd Login.LoginMsg
login model =
    Task.perform Login.FetchError Login.FetchSuccess (postLogin model)


register : RegisterModel -> Cmd Register.RegisterMsg
register model =
    Task.perform Register.FetchError Register.FetchSuccess (postRegister model)


suppliers : UserModel -> Cmd Supplier.SupplierMsg
suppliers model =
    Task.perform Supplier.ServerError Supplier.SuppliersResp (getSuppliers model)


postLogin : LoginModel -> Task.Task Http.Error UserModel
postLogin model =
    let
        body =
            Http.string (loginDto model)
    in
        Http.fromJson userDecoder (httpPost Nothing loginUrl body)


postRegister : RegisterModel -> Task.Task Http.Error UserModel
postRegister model =
    let
        body =
            Http.string (registerDto model)
    in
        Http.fromJson userDecoder (httpPost Nothing registerUrl body)


getSuppliers : UserModel -> Task.Task Http.Error (List SupplierItemModel)
getSuppliers user =
    Http.fromJson suppliersDecoder <| httpGet (Just user) suppliersUrl
