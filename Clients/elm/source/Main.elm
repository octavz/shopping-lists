module Main exposing (..)

import Html exposing (div, span, strong, text)
import Html.App
import Login as Login
import Models exposing (..)
import Messages exposing (..)


init : ( Model, Cmd msg )
init =
    ( { userData = emptyLoginModel, lists = [] }, Cmd.none )


update : Msg -> Model -> ( Model, Cmd Msg )
update msg model =
    case msg of
        Login loginMsg ->
            let
                ( loginModel, loginCmd ) =
                    Login.update loginMsg model.userData
            in
                ( { model | userData = loginModel }, Cmd.map Login loginCmd )


view : Model -> Html.Html Msg
view model =
    div []
        [ Html.App.map Login (Login.view model.userData) ]


main =
    Html.App.program
        { init = init
        , view = view
        , update = update
        , subscriptions = \_ -> Sub.none
        }
