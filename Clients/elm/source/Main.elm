module Main exposing (..)

import Html exposing (div, span, strong, text)
import Html.App
import Login as Login
import Models exposing (..)
import Messages exposing (..)
import RouteUrl
import Router as Router
import Views as Views


init : ( Model, Cmd msg )
init =
    ( { userData = { login = "", name = "", key = Just "" }
      , lists = []
      , loginView = emptyLoginModel
      , activePage = PageLogin
      }
    , Cmd.none
    )


update : Msg -> Model -> ( Model, Cmd Msg )
update msg model =
    case msg of
        Login ((FetchSuccess user) as m) ->
            let
                ( loginModel, loginCmd ) =
                    Login.update m model.loginView
            in
                ( { model
                    | userData = user
                    , loginView = loginModel
                    , activePage = PageMyAccount
                  }
                , Cmd.map Login loginCmd
                )

        Login loginMsg ->
            let
                ( loginModel, loginCmd ) =
                    Login.update loginMsg model.loginView
            in
                ( { model | loginView = loginModel }, Cmd.map Login loginCmd )

        SetActivePage page ->
            ( { model | activePage = setActivePage model page }, Cmd.none )


setActivePage : Model -> Page -> Page
setActivePage model page =
    case model.userData.key of
        Just _ ->
            page

        _ ->
            PageAccessDenied


view : Model -> Html.Html Msg
view model =
    div []
        [ (Html.App.map Login (Views.viewLogin model.loginView))
        , div [] [ text (toString model) ]
        ]


main =
    RouteUrl.program
        { delta2url = Router.delta2url
        , location2messages = Router.location2messages
        , init = init
        , view = view
        , update = update
        , subscriptions = \_ -> Sub.none
        }
