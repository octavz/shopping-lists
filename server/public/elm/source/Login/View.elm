module Login.View exposing (viewLogin)

import String
import Maybe exposing (..)
import Html exposing (..)
import Html.Attributes exposing (..)
import Html.Events exposing (onInput, onClick, targetValue)
import Login.Model exposing (..)
import Login.Messages exposing (..)


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
            [ div [ class "mainbox col-md-6 col-md-offset-3 col-sm-8 col-sm-offset-2", id "loginbox", attribute "style" "margin-top:50px;" ]
                [ div [ class "panel panel-info" ]
                    [ div [ class "panel-heading" ]
                        [ div [ class "panel-title" ]
                            [ text "Sign In" ]
                        , div [ attribute "style" "float:right; font-size: 80%; position: relative; top:-10px" ]
                            [ a [ href "#" ]
                                [ text "Forgot password?" ]
                            ]
                        ]
                    , div [ class "panel-body", attribute "style" "padding-top:30px" ]
                        [ div [ class "alert alert-danger col-sm-12", id "login-alert", attribute "style" "display:none" ]
                            []
                        , Html.form [ class "form-horizontal", id "loginform", attribute "role" "form" ]
                            [ div [ class "input-group", attribute "style" "margin-bottom: 25px" ]
                                [ span [ class "input-group-addon" ]
                                    [ i [ class "glyphicon glyphicon-user" ]
                                        []
                                    ]
                                , input
                                    [ class "form-control"
                                    , id "login-username"
                                    , name "username"
                                    , placeholder "username or email"
                                    , type_ "text"
                                    , value model.login
                                    , onInput UpdateLogin
                                    ]
                                    []
                                ]
                            , div [ class "input-group", attribute "style" "margin-bottom: 25px" ]
                                [ span [ class "input-group-addon" ]
                                    [ i [ class "glyphicon glyphicon-lock" ]
                                        []
                                    ]
                                , input
                                    [ class "form-control"
                                    , id "login-password"
                                    , name "password"
                                    , placeholder "password"
                                    , type_ "password"
                                    , value model.password
                                    , onInput UpdatePassword
                                    ]
                                    []
                                ]
                            , div [ class "input-group" ]
                                [ div [ class "checkbox" ]
                                    [ label []
                                        [ input [ id "login-remember", name "remember", type_ "checkbox", value "1" ]
                                            []
                                        , text "Remember me                                        "
                                        ]
                                    ]
                                ]
                            , div [ class "form-group", attribute "style" "margin-top:10px" ]
                                [ div [ class "col-sm-12 controls" ]
                                    [ a [ class "btn btn-success", href "#", id "btn-login", onClick OnLogin ]
                                        [ text "Login  " ]
                                    , span [] [ text " " ]
                                    , a [ class "btn btn-primary", href "#", id "btn-fblogin" ]
                                        [ text "Login with Facebook" ]
                                    ]
                                ]
                            , div [ class "form-group" ]
                                [ div [ class "col-md-12 control" ]
                                    [ div [ attribute "style" "border-top: 1px solid#888; padding-top:15px; font-size:85%" ]
                                        [ a [ href "/#register" ] [ text "Don't have an account!                                         " ] ]
                                    ]
                                ]
                            ]
                        ]
                    ]
                ]
            ]
