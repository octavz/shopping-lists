module Views exposing (viewLogin)

import String
import Maybe exposing (..)
import Html exposing (label, text, input, button, div, h1)
import Html.Attributes exposing (..)
import Html.Events exposing (onInput, onClick, targetValue)
import Html.App
import Models exposing (..)
import Messages exposing (..)


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


viewLogin : LoginModel -> Html.Html LoginMsg
viewLogin model =
    let
        errItem msg =
            div [] [ text msg ]

        viewErrors =
            if model.signinAttempts > 0 then
                List.map errItem <| getErrors model
            else
                []

        message =
            div [] [ text <| withDefault "" model.message ]
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