module Json exposing (..)

import Json.Encode as Encode exposing (encode)
import Json.Decode exposing (..)
import Login.Model exposing (..)
import Register.Model exposing (..)
import Account.Model exposing (..)
import Supplier.Model exposing (..)


loginDto : LoginModel -> Value
loginDto model =
        Encode.object
            [ ( "login", Encode.string model.login )
            , ( "password", Encode.string model.password )
            , ( "clientId", Encode.string "1" )
            , ( "grantType", Encode.string "password" )
            , ( "clientSecret", Encode.string "secret" )
            ]


registerDto : RegisterModel -> Value
registerDto model =
        Encode.object
            [ ( "login", Encode.string model.login )
            , ( "password", Encode.string model.password )
            ]



userDecoder : Decoder UserModel
userDecoder =
    map3 UserModel
        (field "login" string)
        (field "nick" string)
        (field "accessToken" (maybe string))


supplierItemDecoder : Decoder SupplierItemModel
supplierItemDecoder =
    map3 SupplierItemModel
        (field "id" (maybe string))
        (field "name" string)
        (field "description" (maybe string))


suppliersDecoder : Decoder (List SupplierItemModel)
suppliersDecoder =
    (at [ "items" ] (list supplierItemDecoder))
