module Account.View exposing (viewAccount)

import Account.Messages exposing (..)
import Html exposing (label, text, input, button, div, h1, a, b, p, span,i)
import Html.Attributes exposing (..)
import Html.Events exposing (onInput, onClick, targetValue)
import Main.Models exposing (..)
import Maybe exposing (..)

viewAccount1 : UserModel -> Html.Html a
viewAccount1 model =
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
                        , text <| withDefault "" model.content.login
                        ]
                    ]
                , div
                    [ class "row top5" ]
                    [ div [ class "col-sm-4" ]
                        [ b [] [ text "Name: " ]
                        , text <| withDefault "" model.content.nick
                        ]
                    ]
                ]
            ]
        ]

viewAccount : AccountModel -> Html.Html AccountMsg
viewAccount model =
    div [ class "container" ]
        [ div [ class "mainbox col-md-6 col-md-offset-3 col-sm-8 col-sm-offset-2", id "signupbox", attribute "style" "margin-top:50px" ]
            [ div [ class "panel panel-info" ]
                [ div [ class "panel-heading" ]
                    [ div [ class "panel-title" ]
                        [ text "Account" ]
                    ]
                , div [ class "panel-body" ]
                    [ Html.form [ action "", class "form-horizontal", id "signupform", attribute "role" "form" ]
                        [ div [ class "alert alert-danger", id "signupalert", attribute "style" "display:none" ]
                            [ p []
                                [ text <| withDefault "" model.message ]
                            , span []
                                []
                            ]
                        , div [ class "form-group" ]
                            [ label [ class "col-md-3 control-label", for "email" ]
                                [ text "Login" ]
                            , div [ class "col-md-9" ]
                                [ input
                                    [ class "form-control"
                                    , name "email"
                                    , placeholder "Login"
                                    , type_ "text"
                                    , value <| withDefault "" model.content.login
                                    , onInput UpdateLogin
                                    ]
                                    []
                                ]
                            ]
                        , div [ class "form-group" ]
                            [ label [ class "col-md-3 control-label", for "email" ]
                                [ text "Name" ]
                            , div [ class "col-md-9" ]
                                [ input
                                    [ class "form-control"
                                    , name "email"
                                    , placeholder "Email Address"
                                    , type_ "text"
                                    , value <| withDefault "" model.content.nick
                                    , onInput UpdateName
                                    ]
                                    []
                                ]
                            ]
                        , div [ class "form-group" ]
                            [ label [ class "col-md-3 control-label", for "password" ]
                                [ text "Password" ]
                            , div [ class "col-md-9" ]
                                [ input
                                    [ class "form-control"
                                    , name "passwd"
                                    , placeholder "Password"
                                    , type_ "password"
                                    , value <| withDefault "" model.content.password
                                    , onInput UpdatePassword
                                    ]
                                    []
                                ]
                            ]
                        , div [ class "form-group" ]
                            [ label [ class "col-md-3 control-label", for "password" ]
                                [ text "Password again" ]
                            , div [ class "col-md-9" ]
                                [ input
                                    [ class "form-control"
                                    , name "passwd"
                                    , placeholder "Password"
                                    , type_ "password"
                                    , value <| withDefault "" model.confirm
                                    , onInput UpdateConfirm
                                    ]
                                    []
                                ]
                            ]
                        , div [ class "form-group" ]
                            [ div [ class "col-md-offset-3 col-md-9" ]
                                [ button
                                    [ class "btn btn-info"
                                    , id "btn-signup"
                                    , type_ "button"
                                    , onClick OnAccount
                                    ]
                                    [ i [ class "icon-hand-right" ]
                                        []
                                    , text "OK                            "
                                    ]
                                ]
                            ]
                        ]
                    ]
                ]
            ]
        ]
