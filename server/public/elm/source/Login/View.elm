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


view : LoginModel -> Html.Html LoginMsg
view model =
  div [ class "container", attribute "style" "margin-top:30px" ]
      [ div [ class "col-md-4 col-md-offset-4" ]
          [ div [ class "panel panel-default" ]
              [ div [ class "panel-heading" ]
                  [ h3 [ class "panel-title" ]
                      [ strong []
                          [ text "Sign in " ]
                      ]
                  , div [ attribute "style" "float:right; font-size: 80%; position: relative; top:-10px" ]
                      [ a [ href "#" ]
                          [ text "Forgot password?" ]
                      ]
                  ]
              , div [ class "panel-body" ]
                  [ form [ attribute "role" "form" ]
                      [ div [ class "alert alert-danger" ]
                          [ a [ class "close", attribute "data-dismiss" "alert", href "#" ]
                              [ text "×" ]
                          , text "Incorrect Username or Password!            "
                          ]
                      , div [ class "input-group", attribute "style" "margin-bottom: 12px" ]
                          [ span [ class "input-group-addon" ]
                              [ i [ class "glyphicon glyphicon-user" ]
                                  []
                              ]
                          , input [ class "form-control", id "login-username", name "username", placeholder "username or email", type_ "text", value "", onInput UpdateLogin ]
                              []
                          ]
                      , div [ class "input-group", attribute "style" "margin-bottom: 12px" ]
                          [ span [ class "input-group-addon" ]
                              [ i [ class "glyphicon glyphicon-lock" ]
                                  []
                              ]
                          , input [ class "form-control", id "login-password", name "password", placeholder "password", type_ "password", onInput UpdatePassword ]
                              []
                          ]
                      , div [ class "input-group" ]
                          [ div [ class "checkbox", attribute "style" "margin-top: 0px;" ]
                              [ label []
                                  [ input [ id "login-remember", name "remember", type_ "checkbox", value "1" ]
                                      []
                                  , text "Remember me                                        "
                                  ]
                              ]
                          ]
                      , button [ onClick OnLogin, class "btn btn-success", type_ "submit" ]
                          [ text "Sign in" ]
                      , hr [ attribute "style" "margin-top:10px;margin-bottom:10px;" ]
                          []
                      , div [ class "form-group" ]
                          [ div [ attribute "style" "font-size:85%" ]
                              [ a [ onClick RegisterCmd, href "/#register" ]
                                  [ text "Don't have an account! Click to register" ]
                              ]
                          ]
                      ]
                  ]
              ]
          ]
      ]

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
                    , div [ class "row top7" ]
                        [ div [ class "col-md-2 col-md-offset-5" ]
                            [ button [ onClick OnLogin, class "btn btn-default" ]
                                [ text "Login" ]
                            ]
                        ]
                    , div [ class "row top7" ]
                        [ div [ class "col-sm-6 col-sm-offset-3" ]
                            [ a [ onClick RegisterCmd, href "/#register" ]
                                [ text "Click to register" ]
                            ]
                        ]
                    , div [ class "row errors" ]
                        viewErrors
                    ]
                ]
            ]
