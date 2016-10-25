module Views exposing (viewLogin, viewRegister, viewAccount)

import String
import Maybe exposing (..)
import Html exposing (label, text, input, button, div, h1, a, b)
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
                                , onInput (\a -> LoginView (UpdateLogin a))
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
                                , onInput (LoginView << UpdatePassword)
                                , value model.password
                                ]
                                []
                            ]
                        ]
                    , div [ class "row top7" ]
                        [ div [ class "col-md-2 col-md-offset-5" ]
                            [ button [ onClick (LoginView Fetch), class "btn btn-default" ]
                                [ text "Login" ]
                            ]
                        ]
                    , div [ class "row top7" ]
                        [ div [ class "col-md-2 col-md-offset-5" ]
                            [ a [ onClick RegisterCmd, href "/#register" ]
                                [ text "Click to register" ]
                            ]
                        ]
                    , div [ class "row errors" ]
                        viewErrors
                    ]
                ]
            ]


viewRegister : RegisterModel -> Html.Html RegisterMsg
viewRegister model =
    let
        message =
            div [] [ text <| withDefault "" model.message ]
    in
        div [ class "container" ]
            [ div [ class "row" ]
                [ div [ class "col-xs-4 col-xs-offset-4" ]
                    [ div [ class "row top5" ]
                        [ h1 []
                            [ text "Register" ]
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
                                , onInput (RegisterView << UpdateLogin)
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
                                , onInput (RegisterView << UpdatePassword)
                                , value model.password
                                ]
                                []
                            ]
                        ]
                    , div [ class "row top5" ]
                        [ div [ class "col-sm-4" ]
                            [ label [ class "text-right", for "pass" ]
                                [ text "Confirm:" ]
                            ]
                        , div [ class "col-sm-8" ]
                            [ input
                                [ name "pass"
                                , type' "password"
                                , onInput UpdateConfirm
                                , value model.confirm
                                ]
                                []
                            ]
                        ]
                    , div [ class "row top7" ]
                        [ div [ class "col-md-2 col-md-offset-5" ]
                            [ button [ onClick (RegisterView Fetch), class "btn btn-default" ]
                                [ text "Register" ]
                            ]
                        ]
                    ]
                ]
            ]


viewAccount : UserModel -> Html.Html a
viewAccount model =
    div [ class "container" ]
        [ div [ class "row" ]
            [ div [ class "col-md-4 col-xs-offset-4" ]
                [ div [ class "row top5" ]
                    [ h1 []
                        [ text "My Account" ]
                    ]
                , div
                    [ class "row top5" ]
                    [ div [ class "col-sm-4" ]
                        [ b [] [ text "Login: " ]
                        , text model.login
                        ]
                    ]
                , div
                    [ class "row top5" ]
                    [ div [ class "col-sm-4" ]
                        [ b [] [ text "Name: " ]
                        , text model.name
                        ]
                    ]
                ]
            ]
        ]
