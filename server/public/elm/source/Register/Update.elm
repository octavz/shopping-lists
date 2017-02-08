module Register.Update exposing (..)

import Debug
import Main.Models exposing (..)
import Repository exposing (..)
import Register.Messages exposing (..)


updateRegister : RegisterMsg -> RegisterModel -> ( RegisterModel, Cmd RegisterMsg )
updateRegister action model =
    case Debug.log "INFO" action of
        UpdateLogin val ->
            let
                c =
                    model.content
            in
                ( { model | content = { c | login = Just val } }, Cmd.none )

        UpdatePassword val ->
            let
                c =
                    model.content
            in
              ( { model | content = {c| password = Just val} }, Cmd.none )

        UpdateConfirm val ->
            ( { model | confirm = Just val }, Cmd.none )

        PostMessage msg ->
            ( { model | message = Just msg }, Cmd.none )

        OnRegister ->
            ( model, register model )

        PostRegister (Ok userModel) ->
            ( { model | message = Nothing }, Cmd.none )

        PostRegister (Err error) ->
            ( { model | message = Just (toString error) }, Cmd.none )
