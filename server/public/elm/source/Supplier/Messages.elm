module Supplier.Messages exposing (..)

import Http exposing (..)
import Supplier.Model exposing (..)

type SupplierMsg
    = UpdateName String
    | UpdateDescription String
    | SuppliersReq
    | SuppliersResp (List SupplierItemModel)
    | SaveSupplierReq
    | SaveSupplierResp SupplierItemModel
    | ServerError Http.Error
    | PostMessage String
