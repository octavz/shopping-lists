module Main exposing (..)

import Html exposing (div, span, strong, text)
import Html.App
import Login as Login
import Models exposing (..)
import Messages exposing (..)


init : ( Model, Cmd msg )
init =
    ( { userData = { login = "", name = "", key = "" }
      , lists = []
      , loginView = emptyLoginModel
      }
    , Cmd.none
    )


update : Msg -> Model -> ( Model, Cmd Msg )
update msg model =
    case msg of
        Login ((FetchSuccess json) as m) ->
            let
                ( loginModel, loginCmd ) =
                    Login.update m model.loginView
            in
                ( { model | userData = json }
                , Cmd.map Login loginCmd
                )

        Login loginMsg ->
            let
                ( loginModel, loginCmd ) =
                    Login.update loginMsg model.loginView
            in
                ( { model | loginView = loginModel }, Cmd.map Login loginCmd )


view : Model -> Html.Html Msg
view model =
    div []
        [ (Html.App.map Login (Login.view model.loginView))
        , div [] [ text (toString model) ]
        ]


main =
    Html.App.program
        { init = init
        , view = view
        , update = update
        , subscriptions = \_ -> Sub.none
        }
