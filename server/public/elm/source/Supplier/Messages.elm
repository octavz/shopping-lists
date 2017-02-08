module Supplier.Messages exposing (..)

import Http exposing (..)
import Main.Dtos exposing (..)

type SupplierMsg
    = UpdateName String
    | UpdateDescription String
    | SuppliersReq
    | SuppliersResp (Result Http.Error SuppliersDTO)
    | SaveSupplierReq (Maybe String)
    | SaveSupplierResp (Result Http.Error SupplierItemDTO)
    | PostMessage String
