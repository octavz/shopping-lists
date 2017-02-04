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
import Home.View exposing (..)
import Json.Encode as Json
import Debug
import String exposing (..)


nav1 : Model -> Html Msg
nav1 model =
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


view_ : Model -> Html.Html Msg
view_ model =
    div []
        [ nav1 model
        , (currentView model)
        , div [] [ text (toString model) ]
        ]


isLoggedIn : Model -> Bool
isLoggedIn model =
    model.userData.key /= Nothing


viewNav : Model -> Html.Html Msg
viewNav model =
    nav [ class "navbar navbar-default", attribute "role" "navigation" ]
        [ div [ class "navbar-header" ]
            [ button
                [ class "navbar-toggle"
                , attribute "data-target" "#example-navbar-collapse"
                , attribute "data-toggle" "collapse"
                , type_ "button"
                ]
                [ span [ class "sr-only" ]
                    [ text "Toggle navigation" ]
                , span [ class "icon-bar" ]
                    []
                , span [ class "icon-bar" ]
                    []
                , span [ class "icon-bar" ]
                    []
                ]
            , a [ class "navbar-brand", href "#" ]
                [ text "Shopping lists" ]
            ]
        , div [ class "collapse navbar-collapse", id "example-navbar-collapse" ]
            [ ul [ class "nav navbar-nav navbar-left" ]
                [ li []
                    [ a [ href "#" ]
                        [ text "Home" ]
                    ]
                ]
            , ul [ class "nav navbar-nav navbar-right" ]
                [ userDropDown model ]
            ]
        ]


userDropDown : Model -> Html.Html Msg
userDropDown model =
    if (isLoggedIn model) then
        li [ class "dropdown" ]
            [ a [ class "dropdown-toggle", attribute "data-toggle" "dropdown", href "#" ]
                [ text model.userData.name
                , b [ class "caret" ]
                    []
                ]
            , ul [ class "dropdown-menu" ]
                [ li []
                    [ a [ href "#" ]
                        [ text "Account" ]
                    ]
                , li []
                    [ a [ href "#" ]
                        [ text "Log Out" ]
                    ]
                , li [ class "divider" ]
                    []
                , li []
                    [ a [ href "#" ]
                        [ text "Help" ]
                    ]
                ]
            ]
    else
        li []
            [ a [ href "/#login" ]
                [ text "Log In" ]
            ]


view : Model -> Html.Html Msg
view model =
    div [ class "container" ]
        [ (viewNav model)
        , (currentView model)
        ]


currentView : Model -> Html Msg
currentView model =
    case Debug.log "page" model.activePage of
        PageHome ->
            (Html.map Home (viewHome model.homeView))

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
