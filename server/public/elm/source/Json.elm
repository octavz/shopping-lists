module Json exposing (..)

import Json.Encode as Encode exposing (encode)
import Json.Decode exposing (..)
import Login.Model exposing (..)
import Register.Model exposing (..)
import Account.Model exposing (..)
import Supplier.Model exposing (..)
import Login.Messages as Login

loginDto : LoginModel -> String
loginDto model =
    Encode.encode 0
        (Encode.object
            [ ( "login", Encode.string model.login )
            , ( "password", Encode.string model.password )
            , ( "clientId", Encode.string "1" )
            , ( "grantType", Encode.string "password" )
            , ( "clientSecret", Encode.string "secret" )
            ]
        )


registerDto : RegisterModel -> String
registerDto model =
    Encode.encode 0
        (Encode.object
            [ ( "login", Encode.string model.login )
            , ( "password", Encode.string model.password )
            ]
        )


userDecoder : Decoder UserModel
userDecoder =
    object3 UserModel
        ("login" := string)
        ("nick" := string)
        ("accessToken" := maybe string)


supplierItemDecoder : Decoder SupplierItemModel
supplierItemDecoder =
    object3 SupplierItemModel
        ("id" := maybe string)
        ("name" := string)
        ("description" := maybe string)


suppliersDecoder : Decoder (List SupplierItemModel)
suppliersDecoder =
    (at ["items"] (list supplierItemDecoder))


