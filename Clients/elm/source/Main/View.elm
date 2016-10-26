module Main.View exposing (view)

import Html exposing (label, text, input, button, div, h1, a, b)
import Html.Attributes exposing (..)
import Html.Events exposing (onInput, onClick, targetValue)
import Html.App
import Main.Models exposing (..)
import Main.Messages exposing (..)
import Login.View exposing (..)
import Register.View exposing (..)
import Account.View exposing (..)


view : Model -> Html.Html Msg
view model =
    div []
        [ (currentView model)
        , div [] [ text (toString model) ]
        ]


currentView : Model -> Html.Html Msg
currentView model =
    case model.activePage of
        PageAccessDenied ->
            div [] [ text "Denied dude!" ]

        PageNotFound ->
            div [] [ text "404 Not found" ]

        PageLogin ->
            (Html.App.map Login (viewLogin model.loginView))

        PageRegister ->
            (Html.App.map Register (viewRegister model.registerView))

        PageMyAccount ->
            viewAccount model.userData
