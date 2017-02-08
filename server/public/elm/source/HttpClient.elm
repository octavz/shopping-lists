module HttpClient exposing (..)

import Http
import Main.Models exposing (..)
import Json exposing (..)
import Task


{-baseUrl =
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
    Http.fromJson suppliersDecoder <| httpGet (Just user) suppliersUrl-}
