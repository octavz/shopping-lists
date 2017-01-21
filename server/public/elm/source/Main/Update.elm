module Main.Update exposing (update)

import Debug
import Cmd.Extra as Extra

import Main.Models exposing (..)
import Main.Messages exposing (..)
import Repository exposing (..)
import Main.Models exposing (..)
import Login.Update exposing (..)
import Supplier.Update exposing (..)
import Login.Messages as Login
import Register.Update exposing (..)
import Register.Messages as Register
import Supplier.Messages as Supplier

update : Msg -> Model -> ( Model, Cmd Msg )
update msg model =
    case Debug.log "main" msg of
        Login ((Login.FetchSuccess user) as m) ->
            let
                ( subModel, subCmd ) =
                    updateLogin m model.loginView
            in
                ( { model
                    | userData = user
                    , loginView = subModel
                  }
                , Extra.message (SetActivePage PageSuppliers)
                )

        Register ((Register.FetchSuccess user) as m) ->
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

        Login (Login.RegisterCmd) ->
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

        SetActivePage page ->
            let cmdNewPage = case page of
              PageSuppliers -> Extra.message (Supplier Supplier.SuppliersReq)
              _ -> Cmd.none
            in
              ( { model | activePage = setActivePage model page }, cmdNewPage )

        Supplier subMsg ->
            let
                ( subModel, subCmd ) =
                    updateSupplier model.userData subMsg model.supplierView
            in
                ( { model | supplierView = subModel }, Cmd.map Supplier subCmd )


setActivePage : Model -> Page -> Page
setActivePage model page =
    case Debug.log "authKey" model.userData.key of
        Just _ ->
            page

        _ ->
            case page of
                PageRegister ->
                    page

                _ ->
                    PageLogin
