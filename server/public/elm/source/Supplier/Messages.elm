module Supplier.Messages exposing (..)

import Http exposing (..)
import Supplier.Model exposing (..)

type SupplierMsg
    = UpdateName String
    | UpdateDescription String
    | SuppliersReq
    | SuppliersResp (Result Http.Error (List SupplierItemModel))
    | SaveSupplierReq
    | SaveSupplierResp (Result Http.Error SupplierItemModel)
    | PostMessage String
