module Supplier.Update exposing (..)

import Debug
import Main.Models exposing (..)
import Main.Dtos exposing (..)
import Repository exposing (..)
import Supplier.Messages exposing (..)
import Maybe exposing (withDefault)


updateSupplier : UserModel -> SupplierMsg -> SupplierModel -> ( SupplierModel, Cmd SupplierMsg )
updateSupplier userData action model =
    let
        current =
            withDefault (SupplierItemDTO Nothing "" Nothing) model.current
    in
        case Debug.log "supplier-update" action of
            UpdateName val ->
                let
                    c =
                        { current | name = val }
                in
                    ( { model | current = Just c }, Cmd.none )

            UpdateDescription val ->
                let
                    c =
                        { current | description = Just val }
                in
                    ( { model | current = Just c }, Cmd.none )

            PostMessage msg ->
                ( { model | message = Just msg }, Cmd.none )

            SuppliersReq ->
                ( model, (suppliers userData) )

            SuppliersResp (Ok val) ->
                ( { model | content = val }, Cmd.none )

            SuppliersResp (Err error) ->
                ( { model | message = Just (toString error) }, Cmd.none )

            SaveSupplierReq (Just id) -> -- update
                ( model, Cmd.none )

            SaveSupplierReq Nothing -> -- insert
                ( model, Cmd.none )

            SaveSupplierResp (Ok _) ->
                ( model, Cmd.none )

            SaveSupplierResp (Err error) ->
                ( { model | message = Just (toString error) }, Cmd.none )
