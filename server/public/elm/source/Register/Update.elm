module Register.Update exposing (updateRegister)

import Debug
import Register.Model exposing (..)
import Register.Messages exposing (..)
import Repository exposing (..)


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

        Fetch ->
            ( model, register model )

        FetchSuccess s ->
            ( { model | message = Nothing }, Cmd.none )

        FetchError error ->
            ( { model | message = Just (toString error) }, Cmd.none )
