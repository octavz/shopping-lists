module Main.Update exposing (..)

import Debug
import Task
import Main.Models exposing (..)
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
import Home.Update exposing (..)


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
