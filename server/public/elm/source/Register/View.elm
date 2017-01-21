module Register.View exposing (viewRegister)

import String
import Maybe exposing (..)
import Html exposing (label, text, input, button, div, h1, a, b)
import Html.Attributes exposing (..)
import Html.Events exposing (onInput, onClick, targetValue)
import Register.Model exposing (..)
import Register.Messages exposing (..)


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
                                , type_ "text"
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
                                , type_ "password"
                                , onInput UpdatePassword
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
                                , type_ "password"
                                , onInput UpdateConfirm
                                , value model.confirm
                                ]
                                []
                            ]
                        ]
                    , div [ class "row top7" ]
                        [ div [ class "col-md-2 col-md-offset-5" ]
                            [ button [ onClick OnRegister, class "btn btn-default" ]
                                [ text "Register" ]
                            ]
                        ]
                    ]
                ]
            ]
