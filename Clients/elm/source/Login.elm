module Login exposing (..)

import Debug

import Models exposing (..)
import Messages exposing (..)
import Repository exposing (..)


update : LoginMsg -> LoginModel -> ( LoginModel, Cmd LoginMsg )
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
            ( model, login model )

        FetchSuccess s ->
            ( { model | message = Nothing }, Cmd.none )

        FetchError error ->
            ( { model | message = Just (toString error) }, Cmd.none )
