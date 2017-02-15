module Login.Update exposing (updateLogin)

import Debug
import Main.Models exposing (..)
import Repository exposing (..)
import Login.Messages exposing (..)
import Http


updateLogin : LoginMsg -> LoginModel -> ( LoginModel, Cmd LoginMsg )
updateLogin action model =
    let
        inner =
            model.content
    in
        case Debug.log "login-update" action of
            UpdateLogin val ->
                ( { model | content = { inner | login = val } }, Cmd.none )

            UpdatePassword val ->
                ( { model | content = { inner | password = val } }, Cmd.none )

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
