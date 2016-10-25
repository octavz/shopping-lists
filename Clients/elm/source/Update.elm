module Update exposing (updateLogin, updateRegister)

import Debug
import Models exposing (..)
import Messages exposing (..)
import Repository exposing (..)


updateLogin : LoginMsg -> LoginModel -> ( LoginModel, Cmd LoginMsg )
updateLogin action model =
    case Debug.log "INFO" action of
        LoginView (UpdateLogin val) ->
            ( { model | login = val }, Cmd.none )

        LoginView (UpdatePassword val) ->
            ( { model | password = val }, Cmd.none )

        ShowErrors ->
            ( { model | signinAttempts = model.signinAttempts + 1 }, Cmd.none )

        LoginView (PostMessage msg) ->
            ( { model
                | signinAttempts = model.signinAttempts + 1
                , message = Just msg
              }
            , Cmd.none
            )

        LoginView Fetch ->
            ( model, login model )

        LoginView (FetchSuccess s) ->
            ( { model | message = Nothing }, Cmd.none )

        LoginView (FetchError error) ->
            ( { model | message = Just (toString error) }, Cmd.none )

        RegisterCmd ->
            ( model, Cmd.none )


updateRegister : RegisterMsg -> RegisterModel -> ( RegisterModel, Cmd RegisterMsg )
updateRegister action model =
    case Debug.log "INFO" action of
        RegisterView (UpdateLogin val) ->
            ( { model | login = val }, Cmd.none )

        RegisterView (UpdatePassword val) ->
            ( { model | password = val }, Cmd.none )

        UpdateConfirm val ->
            ( { model | confirm = val }, Cmd.none )

        RegisterView (PostMessage msg) ->
            ( { model | message = Just msg }, Cmd.none )

        RegisterView Fetch ->
            ( model, register model )

        RegisterView (FetchSuccess s) ->
            ( { model | message = Nothing }, Cmd.none )

        RegisterView (FetchError error) ->
            ( { model | message = Just (toString error) }, Cmd.none )
