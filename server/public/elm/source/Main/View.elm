module Main.View exposing (view)

import Html exposing (..)
import Html.Attributes exposing (..)
import Html.Events exposing (onInput, onClick, targetValue)
import Main.Models exposing (..)
import Main.Messages exposing (..)
import Login.View exposing (..)
import Register.View exposing (..)
import Account.View exposing (..)
import Supplier.View exposing (..)
import Json.Encode as Json
import Debug


nav : Model -> Html Msg
nav model =
    let
        isLogin =
            model.activePage == PageLogin

        isRegister =
            model.activePage == PageRegister

        isMyAccount =
            model.activePage == PageMyAccount
    in
        ul [ class "nav nav-pills" ]
            [ li [ property "role" (Json.string "presentation"), classList [ ( "active", isLogin ) ] ] [ a [ href "#login" ] [ text "Login" ] ]
            , li [ property "role" (Json.string "presentation"), classList [ ( "active", isRegister ) ] ] [ a [ href "#register" ] [ text "Register" ] ]
            , li [ property "role" (Json.string "presentation"), classList [ ( "active", isMyAccount ) ] ] [ a [ href "#account" ] [ text "Account" ] ]
            , li [ property "role" (Json.string "presentation"), classList [ ( "active", isMyAccount ) ] ] [ a [ href "#suppliers" ] [ text "Suppliers" ] ]
            ]


view : Model -> Html.Html Msg
view model =
    div []
        [ nav model
        , (currentView model)
        , div [] [ text (toString model) ]
        ]


currentView : Model -> Html Msg
currentView model =
    case Debug.log "page" model.activePage of
        PageAccessDenied ->
            div [] [ text "Denied dude!" ]

        PageNotFound ->
            div [] [ text "404 Not found" ]

        PageLogin ->
            (Html.map Login (viewLogin model.loginView))

        PageRegister ->
            (Html.map Register (viewRegister model.registerView))

        PageMyAccount ->
            viewAccount model.userData

        PageSuppliers ->
            (Html.map Supplier (viewSupplier model.supplierView))
