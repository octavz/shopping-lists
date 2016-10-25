module Repository exposing (login)

import Http
import Json.Encode as Encode exposing (encode)
import Json.Decode as Json exposing ((:=))
import Task
import Models exposing (..)
import Messages exposing (..)


baseUrl =
    "http://localhost:9000/api/"


loginUrl =
    baseUrl ++ "login"


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


userDecoder : Json.Decoder UserModel
userDecoder =
    Json.object3 UserModel
        ("login" := Json.string)
        ("nick" := Json.string)
        ("accessToken" := Json.maybe Json.string)


login : LoginModel -> Cmd LoginMsg
login model =
    Task.perform FetchError FetchSuccess (fetchLogin model)


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
