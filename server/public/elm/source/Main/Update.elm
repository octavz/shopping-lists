module Main.Update exposing (..)

import Debug
import Task
import Main.Models exposing (..)
import Main.Dtos exposing (..)
import Repository exposing (..)
import Main.Models exposing (..)
import Register.Update exposing (..)
import Supplier.Update exposing (..)
import Login.Update exposing (..)
import Main.Messages exposing (..)
import Login.Messages exposing (..)
import Register.Messages exposing (..)
import Supplier.Messages exposing (..)
import Home.Messages exposing (..)
import Account.Update exposing (..)
import Account.Messages exposing (..)
import Home.Update exposing (..)
import Maybe.Extra exposing (..)


update : Msg -> Model -> ( Model, Cmd Msg )
update msg model =
    case Debug.log "main-update" msg of
        Login ((PostLogin (Ok user)) as m) ->
            let
                ( subModel, subCmd ) =
                    updateLogin m model.loginView
            in
                ( { model
                    | userData = Debug.log "user" user
                    , loginView = subModel
                    , accountView = AccountModel user.content Nothing Nothing
                    , activePage = PageMyAccount
                  }
                , Cmd.map Login subCmd
                  --Task.perform identity (Task.succeed (SetActivePage PageSuppliers))
                )

        {- Register ((Register.FetchSuccess user) as m) ->
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
        -}
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

        Account OnAccount ->
            let
                inner =
                    model.sync
            in
                if isNothing model.accountView.message then
                    ( { model | sync = (accountToSync model.accountView inner) }, Cmd.none )
                else
                    ( model, Cmd.none )

        Account subMsg ->
            let
                ( subModel, subCmd ) =
                    updateAccount subMsg model.accountView
            in
                ( { model | accountView = subModel }, Cmd.map Account subCmd )

        SetActivePage page ->
            let
                cmdNewPage =
                    case page of
                        PageSuppliers ->
                            Task.perform identity (Task.succeed (Supplier SuppliersReq))

                        _ ->
                            Cmd.none
            in
                ( { model | activePage = setActivePage model page }, cmdNewPage )

        Supplier subMsg ->
            let
                ( subModel, subCmd ) =
                    updateSupplier model.userData subMsg model.supplierView
            in
                ( { model | supplierView = subModel }, Cmd.map Supplier subCmd )

        Home subMsg ->
            let
                ( subModel, subCmd ) =
                    updateHome subMsg model.homeView
            in
                ( { model | homeView = subModel }, Cmd.map Home subCmd )

        SyncData _ ->
            performSync model

        Sync val ->
            ( model, Cmd.none )



--this saves the current account to sync store


accountToSync : AccountModel -> SyncDTO -> SyncDTO
accountToSync account sync =
    let
        mergeAccount : UpdateUserDTO -> UpdateUserDTO -> UpdateUserDTO
        mergeAccount first second =
            { nick = or second.nick first.nick
            , password = or second.password first.password
            , login = or second.login first.login
            , id = or second.id first.id
            }
    in
        case sync.userData of
            Just u ->
                { sync | userData =  Just (mergeAccount u account.content)  }

            _ ->
                { sync | userData = Just account.content }


performSync : Model -> ( Model, Cmd Msg )
performSync model =
    case model.userData.key of
        Nothing ->
            ( model, Cmd.none )

        _ ->
            ( model, Cmd.none )


setActivePage : Model -> Page -> Page
setActivePage model page =
    case Debug.log "authKey" model.userData.key of
        Just _ ->
            page

        _ ->
            case page of
                PageRegister ->
                    page

                PageHome ->
                    page

                _ ->
                    PageLogin
