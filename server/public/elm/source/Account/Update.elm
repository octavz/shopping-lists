module Account.Update exposing (..)

import Debug
import Main.Models exposing (..)
import Repository exposing (..)
import Account.Messages exposing (..)
import String exposing (isEmpty)
import Maybe exposing (withDefault)


notEmpty : Maybe String -> Bool
notEmpty val =
    not <| isEmpty <| withDefault "" val


updateAccount : AccountMsg -> AccountModel -> ( AccountModel, Cmd AccountMsg )
updateAccount action model =
    let
        c =
            model.content
    in
        case Debug.log "INFO" action of
            UpdateLogin val ->
                ( { model | content = { c | login = Just val }, message = Nothing }, Cmd.none )

            UpdateName val ->
                ( { model | content = { c | nick = Just val }, message = Nothing }, Cmd.none )

            UpdatePassword val ->
                ( { model | content = { c | password = Just val }, message = Nothing }, Cmd.none )

            UpdateConfirm val ->
                ( { model | confirm = Just val, message = Nothing }, Cmd.none )

            PostMessage msg ->
                ( { model | message = Just msg }, Cmd.none )

            OnAccount ->
                if model.content.password /= model.confirm && (notEmpty model.content.login) && (notEmpty model.content.nick) then
                    ( { model | message = Just "Password is not confirmed." }, Cmd.none )
                else
                    ( { model | message = Nothing }, Cmd.none )
