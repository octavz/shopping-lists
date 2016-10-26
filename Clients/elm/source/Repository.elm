module Repository exposing (login, register)

import Http
import Json.Encode as Encode exposing (encode)
import Json.Decode as Decode exposing ((:=))
import Task
import Login.Messages as Login
import Login.Model exposing (..)
import Register.Messages as Register
import Register.Model exposing (..)
import Account.Model exposing (..)


baseUrl =
    "http://localhost:9000/api/"


loginUrl =
    baseUrl ++ "login"


registerUrl =
    baseUrl ++ "register"


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


login : LoginModel -> Cmd Login.LoginMsg
login model =
    Task.perform Login.FetchError Login.FetchSuccess (fetchLogin model)


register : RegisterModel -> Cmd Register.RegisterMsg
register model =
    Task.perform Register.FetchError Register.FetchSuccess (fetchRegister model)


fetchLogin : LoginModel -> Task.Task Http.Error UserModel
fetchLogin model =
    let
        future =
            Http.send Http.defaultSettings
                { verb = "POST"
                , headers =
                    [ ( "Content-Type", "application/json" )
                    , ( "Accept", "application/json" )
                    ]
                , url = loginUrl
                , body = Http.string (loginDto model)
                }
    in
        (Http.fromJson userDecoder future)


fetchRegister : RegisterModel -> Task.Task Http.Error UserModel
fetchRegister model =
    let
        future =
            Http.send Http.defaultSettings
                { verb = "POST"
                , headers =
                    [ ( "Content-Type", "application/json" )
                    , ( "Accept", "application/json" )
                    ]
                , url = registerUrl
                , body = Http.string (registerDto model)
                }
    in
        (Http.fromJson userDecoder future)
