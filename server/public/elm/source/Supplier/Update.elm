module Supplier.Update exposing (updateSupplier)

import Debug
import Supplier.Model exposing (..)
import Account.Model exposing (..)
import Repository exposing (..)
import Supplier.Messages exposing (..)


updateSupplier : UserModel -> SupplierMsg -> SupplierModel -> ( SupplierModel, Cmd SupplierMsg )
updateSupplier userData action model =
    let
        current =
            model.current
    in
        case Debug.log "INFO" action of
            UpdateName val ->
                let
                    c =
                        { current | name = val }
                in
                    ( { model | current = c }, Cmd.none )

            UpdateDescription val ->
                let
                    c =
                        { current | description = Just val }
                in
                    ( { model | current = c }, Cmd.none )

            PostMessage msg ->
                ( { model | message = Just msg }, Cmd.none )

            SuppliersReq ->
                ( model, (suppliers userData) )

            SuppliersResp val ->
                ( { model | items = val }, Cmd.none )

            SaveSupplierReq -> (model, Cmd.none)
            SaveSupplierResp _ -> (model, Cmd.none)

            ServerError error ->
                ( { model | message = Just (toString error) }, Cmd.none )
