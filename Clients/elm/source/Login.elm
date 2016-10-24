module Login exposing (..)

import Html exposing (label, text, input, button, div, h1)
import Html.Attributes exposing (..)
import Html.Events exposing (onInput, onClick, targetValue)
import Html.App
import Http
import Json.Encode as Encode exposing (encode)
import Json.Decode as Json exposing ((:=))
import String
import Task
import Debug
import Models exposing (..)
import Messages exposing (..)


loginDto : LoginViewModel -> String
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


loginUrl =
    "http://localhost:9000/api/login"


getErrors { login, password } =
    let
        errors =
            [ ( String.isEmpty login, "Login is required." )
            , ( String.isEmpty password, "Password is required." )
            ]
    in
        List.filterMap
            (\( erred, msg ) ->
                if erred then
                    Just msg
                else
                    Nothing
            )
            errors


errItem msg =
    div [] [ text msg ]


userDecoder : Json.Decoder UserModel
userDecoder =
    Json.object3 UserModel
        ("login" := Json.string)
        ("nick" := Json.string)
        ("accessToken" := Json.string)


fetch model =
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


login : LoginViewModel -> Cmd LoginMsg
login model =
    Task.perform FetchError FetchSuccess (fetch model)


view : LoginViewModel -> Html.Html LoginMsg
view model =
    let
        errors =
            getErrors model

        viewErrors =
            if model.signinAttempts > 0 then
                List.map errItem errors
            else
                []

        message =
            case model.message of
                Nothing ->
                    div [] []

                Just msg ->
                    div [] [ text msg ]
    in
        div [ class "container" ]
            [ div [ class "row" ]
                [ div [ class "col-xs-4 col-xs-offset-4" ]
                    [ div [ class "row top5" ]
                        [ h1 []
                            [ text "Sign In" ]
                        ]
                    , div [ class "row top5" ]
                        [ message ]
                    , div
                        [ class "row top5" ]
                        [ div [ class "col-sm-4" ]
                            [ label [ class "text-right", for "login" ]
                                [ text "Login:" ]
                            ]
                        , div [ class "col-sm-8" ]
                            [ input
                                [ name "login"
                                , type' "text"
                                , onInput UpdateLogin
                                , value model.login
                                ]
                                []
                            ]
                        ]
                    , div [ class "row top5" ]
                        [ div [ class "col-sm-4" ]
                            [ label [ class "text-right", for "pass" ]
                                [ text "Password:" ]
                            ]
                        , div [ class "col-sm-8" ]
                            [ input
                                [ name "pass"
                                , type' "password"
                                , onInput UpdatePassword
                                , value model.password
                                ]
                                []
                            ]
                        ]
                    , div [ class "row top7" ]
                        [ div [ class "col-md-2 col-md-offset-5" ]
                            [ button [ onClick Fetch, class "btn btn-default" ]
                                [ text "Login" ]
                            ]
                        ]
                    , div [ class "row errors" ]
                        viewErrors
                    ]
                ]
            ]


update : LoginMsg -> LoginViewModel -> ( LoginViewModel, Cmd LoginMsg )
update action model =
    case Debug.log "INFO" action of
        UpdateLogin val ->
            ( { model | login = val }, Cmd.none )

        UpdatePassword val ->
            ( { model | password = val }, Cmd.none )

        ShowErrors ->
            ( { model | signinAttempts = model.signinAttempts + 1 }, Cmd.none )

        PostMessage msg ->
            ( { model
                | signinAttempts = model.signinAttempts + 1
                , message = Just msg
              }
            , Cmd.none
            )

        Fetch ->
            ( model, login model )

        FetchSuccess s ->
            let
                d =
                    Debug.log "FetchSucces" s
            in
                ( model, Cmd.none )

        FetchError error ->
            ( { model | message = Just (toString error) }, Cmd.none )
