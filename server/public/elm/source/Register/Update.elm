module Register.Update exposing (..)

import Debug
import Register.Model exposing (..)
import Account.Model exposing (..)
import Repository exposing (..)
import Register.Messages exposing (..)

updateRegister : RegisterMsg -> RegisterModel -> ( RegisterModel, Cmd RegisterMsg )
updateRegister action model =
    case Debug.log "INFO" action of
        UpdateLogin val ->
            ( { model | login = val }, Cmd.none )

        UpdatePassword val ->
            ( { model | password = val }, Cmd.none )

        UpdateConfirm val ->
            ( { model | confirm = val }, Cmd.none )

        PostMessage msg ->
            ( { model | message = Just msg }, Cmd.none )

        OnRegister ->
            ( model, register model )

        PostRegister (Ok userModel)  ->
            ( { model | message = Nothing }, Cmd.none )

        PostRegister (Err error) ->
            ( { model | message = Just (toString error) }, Cmd.none )
