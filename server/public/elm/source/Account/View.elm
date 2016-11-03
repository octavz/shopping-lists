module Account.View exposing (viewAccount)

import Html exposing (label, text, input, button, div, h1, a, b)
import Html.Attributes exposing (..)
import Html.Events exposing (onInput, onClick, targetValue)
import Html.App
import Account.Model exposing (..)


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
