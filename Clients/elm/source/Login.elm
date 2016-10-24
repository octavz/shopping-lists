module Login exposing (update, view)

import Html exposing (label, text, input, button, div, h1)
import Bootstrap.Html exposing (..)
import Html.Attributes exposing (..)
import Html.Events exposing (onInput, onClick, targetValue)
import Html.App
import Http
import Json.Encode as Encode exposing (encode)
import Json.Decode as Decode
import String
import Task exposing (Task, andThen, onError, succeed)
import Debug
import Models exposing (..)
import Messages exposing (..)


loginUrl =
    "http://localhost/login"


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


errItem msg =
    div [] [ text msg ]


field lbl typ handler =
    let
        lblWidth =
            style [ ( "width", "120px" ) ]
    in
        div [ class "row" ]
            [ div [ class "col-md-4" ] [ label [ for lbl, lblWidth ] [ text lbl ] ]
            , input [ name lbl, type' typ, handler ] []
            ]


encode model =
    Encode.encode 0
        (Encode.object
            [ ( "login", Encode.string model.login )
            , ( "password", Encode.string model.password )
            ]
        )


fetch : LoginViewModel -> Task Http.Error String
fetch model =
    (Http.fromJson Decode.string
        (Http.send Http.defaultSettings
            { verb = "POST"
            , headers = [ ( "Content-Type", "application/json" ) ]
            , url = loginUrl
            , body = Http.string (encode model)
            }
        )
    )


login : LoginViewModel -> Cmd LoginMsg
login model =
    Task.perform FetchError FetchSuccess (fetch model)


view : LoginViewModel -> Html.Html LoginMsg
view model =
    let
        errors =
            getErrors model

        viewErrors =
            if model.signinAttempts > 0 then
                List.map errItem errors
            else
                []

        message =
            case model.message of
                Nothing ->
                    div [] []

                Just msg ->
                    div [] [ text msg ]

        --clickHandler =
        --if List.length errors == 0 then
        --login model
        --else
        --ShowErrors
    in
        div
            [ class "container center-block" ]
            [ div [ class "row" ] [ h1 [] [ text "Sign In" ] ]
            , div [ class "row" ] [ message ]
            , field "Login" "text" (onInput UpdateLogin)
            , field "Password" "password" (onInput UpdatePassword)
            , div [ class "row" ] [ button [ onClick Fetch ] [ text "Login" ] ]
            , div [ class "errors" ] viewErrors
            ]


update : LoginMsg -> LoginViewModel -> ( LoginViewModel, Cmd LoginMsg )
update action model =
    case Debug.log "INFO" action of
        UpdateLogin val ->
            ( { model | login = val }, Cmd.none )

        UpdatePassword val ->
            ( { model | password = val }, Cmd.none )

        ShowErrors ->
            ( { model | signinAttempts = model.signinAttempts + 1 }, Cmd.none )

        PostMessage msg ->
            ( { model
                | signinAttempts = model.signinAttempts + 1
                , message = Just msg
              }
            , Cmd.none
            )

        Fetch ->
            ( model, (login model) )

        FetchSuccess name ->
            ( model, Cmd.none )

        FetchError error ->
            ( { model | message = Just (toString error) }, Cmd.none )
