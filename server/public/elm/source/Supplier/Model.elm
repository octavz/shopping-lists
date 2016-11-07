module Supplier.Model exposing (..)


type alias SupplierItemModel =
    { id : Maybe String
    , name : String
    , description : Maybe String
    }


type alias SupplierModel =
    { items : List SupplierItemModel
    , current : SupplierItemModel
    , message : Maybe String
    }
