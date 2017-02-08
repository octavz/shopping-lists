module Login.Update exposing (updateLogin)

import Debug
import Main.Models exposing (..)
import Repository exposing (..)
import Login.Messages exposing (..)
import Http


updateLogin : LoginMsg -> LoginModel -> ( LoginModel, Cmd LoginMsg )
updateLogin action model =
    case Debug.log "login-update" action of
        UpdateLogin val ->
            let
                c =
                    model.content
            in
                ( { model | content = { c | login = val } }, Cmd.none )

        UpdatePassword val ->
            let
                c =
                    model.content
            in
                ( { model | content = { c | password = val } }, Cmd.none )

        ShowErrors ->
            ( { model | signinAttempts = model.signinAttempts + 1 }, Cmd.none )

        PostMessage msg ->
            ( { model
                | signinAttempts = model.signinAttempts + 1
                , message = Just msg
              }
            , Cmd.none
            )

        OnLogin ->
            ( model, login model.content )

        PostLogin (Ok userModel) ->
            ( { model | message = Nothing }, Cmd.none )

        PostLogin (Err error) ->
            ( { model | message = Just (toString error) }, Cmd.none )

        RegisterCmd ->
            ( model, Cmd.none )
