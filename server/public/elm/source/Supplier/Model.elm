module Supplier.Model exposing (..)

import List exposing (..)


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


emptySupplierView : SupplierModel



--emptySupplierView = SupplierModel [] emptySupplierItem Nothing


emptySupplierView =
    let
        elem i =
            let
                s =
                    (toString i)
            in
                SupplierItemModel (Just s) ("test " ++ s) (Just ("description " ++ s))
    in
        SupplierModel (map elem [1..5]) emptySupplierItem Nothing


emptySupplierItem : SupplierItemModel
emptySupplierItem =
    SupplierItemModel Nothing "" Nothing
