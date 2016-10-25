module Main exposing (..)

import Html exposing (div, span, strong, text)
import Html.App
import Update exposing (..)
import Models exposing (..)
import Messages exposing (..)
import RouteUrl
import Router as Router
import Views as Views


init : ( Model, Cmd msg )
init =
    ( initialModel, Cmd.none )


update : Msg -> Model -> ( Model, Cmd Msg )
update msg model =
    case msg of
        Login ((LoginView (FetchSuccess user)) as m) ->
            let
                ( subModel, subCmd ) =
                    updateLogin m model.loginView
            in
                ( { model
                    | userData = user
                    , loginView = subModel
                    , activePage = PageMyAccount
                  }
                , Cmd.map Login subCmd
                )

        Register ((RegisterView (FetchSuccess user)) as m) ->
            let
                ( subModel, subCmd ) =
                    updateRegister m model.registerView
            in
                ( { model
                    | userData = user
                    , registerView = subModel
                    , activePage = PageMyAccount
                  }
                , Cmd.map Register subCmd
                )

        Login RegisterCmd ->
            ( { model | activePage = setActivePage model PageRegister }, Cmd.none )

        Login subMsg ->
            let
                ( subModel, subCmd ) =
                    updateLogin subMsg model.loginView
            in
                ( { model | loginView = subModel }, Cmd.map Login subCmd )

        Register subMsg ->
            let
                ( subModel, subCmd ) =
                    updateRegister subMsg model.registerView
            in
                ( { model | registerView = subModel }, Cmd.map Register subCmd )

        SetActivePage page ->
            ( { model | activePage = setActivePage model page }, Cmd.none )


setActivePage : Model -> Page -> Page
setActivePage model page =
    case Debug.log "authKey" model.userData.key of
        Just _ ->
            page

        _ ->
            PageLogin


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
            (Html.App.map Login (Views.viewLogin model.loginView))

        PageRegister ->
            (Html.App.map Register (Views.viewRegister model.registerView))

        PageMyAccount ->
            Views.viewAccount model.userData



--_ ->
--div [] [ text "not implemented yet" ]


main =
    RouteUrl.program
        { delta2url = Router.delta2url
        , location2messages = Router.location2messages
        , init = init
        , view = view
        , update = update
        , subscriptions = \_ -> Sub.none
        }
