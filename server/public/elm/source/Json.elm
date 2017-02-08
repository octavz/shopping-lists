module Json exposing (..)

import Json.Encode as Encode
import Json.Decode as Decode
import Main.Models exposing (..)
import Main.Dtos exposing (..)
import List exposing (map)


maybeString : Maybe String -> Encode.Value
maybeString s =
    maybeObj s Encode.string


maybeInt : Maybe Int -> Encode.Value
maybeInt s =
    maybeObj s Encode.int


maybeObj : Maybe a -> (a -> Encode.Value) -> Encode.Value
maybeObj s enc =
    case s of
        Just v ->
            enc v

        _ ->
            Encode.null


loginEncoder : LoginDTO -> Encode.Value
loginEncoder model =
    Encode.object
        [ ( "login", Encode.string model.login )
        , ( "password", Encode.string model.password )
        , ( "clientId", Encode.string "1" )
        , ( "grantType", Encode.string "password" )
        , ( "clientSecret", Encode.string "secret" )
        ]


registerEncoder : RegisterModel -> Encode.Value
registerEncoder model =
    Encode.object
        [ ( "login", maybeString model.content.login )
        , ( "password", maybeString model.content.password )
        ]


listEncoder : ListDTO -> Encode.Value
listEncoder model =
    Encode.object
        [ ( "id", maybeString model.id )
        , ( "name", Encode.string model.name )
        , ( "description", maybeString model.description )
        , ( "created", Encode.int model.created )
        , ( "items"
          , case model.items of
                Just v ->
                    Encode.list (map listItemEncoder v)

                _ ->
                    Encode.null
          )
        , ( "status", maybeInt model.status )
        , ( "clientTag", maybeString model.clientTag )
        ]


listItemEncoder : ListItemDTO -> Encode.Value
listItemEncoder model =
    Encode.object
        [ ( "productId", maybeString model.productId )
        , ( "quantity", Encode.int model.quantity )
        , ( "description", maybeString model.description )
        , ( "status", Encode.int model.status )
        , ( "clientTag", maybeString model.clientTag )
        , ( "bought", Encode.int model.bought )
        ]


updateUserEncoder : UpdateUserDTO -> Encode.Value
updateUserEncoder model =
    Encode.object
        [ ( "login", maybeString model.login )
        , ( "password", maybeString model.password )
        , ( "id", maybeString model.id )
        , ( "nick", maybeString model.nick )
        ]


listsEncoder : ListsDTO -> Encode.Value
listsEncoder model =
    Encode.object
        [ ( "items", Encode.list (map listEncoder model.items) ) ]


productEncoder : ProductDTO -> Encode.Value
productEncoder model =
    Encode.object
        [ ( "id", maybeString model.id )
        , ( "name", Encode.string model.name )
        , ( "tags", Encode.string model.tags )
        , ( "description", maybeString model.description )
        , ( "clientTag", maybeString model.clientTag )
        ]


priceEncoder : ProductPriceDTO -> Encode.Value
priceEncoder model =
    Encode.object
        [ ( "productId", Encode.string model.productId )
        , ( "supplierId", Encode.string model.supplierId )
        , ( "price", Encode.float model.price )
        ]


syncEncoder : SyncDTO -> Encode.Value
syncEncoder model =
    Encode.object
        [ ( "userData", maybeObj model.userData updateUserEncoder )
        , ( "listsMeta", maybeObj model.listsMeta listsEncoder )
        , ( "products", maybeObj model.products (\a -> (Encode.list <| map productEncoder a)) )
        , ( "prices", maybeObj model.prices (\a -> (Encode.list <| map priceEncoder a)) )
        ]


userDecoder : Decode.Decoder UserModel
userDecoder =
    Decode.map2 UserModel
        (Decode.map4 UpdateUserDTO
            (Decode.field "login" (Decode.maybe Decode.string))
            (Decode.field "password" (Decode.maybe Decode.string))
            (Decode.field "id" (Decode.maybe Decode.string))
            (Decode.field "nick" (Decode.maybe Decode.string))
        )
        (Decode.field "accessToken" (Decode.maybe Decode.string))


supplierItemDecoder : Decode.Decoder SupplierItemDTO
supplierItemDecoder =
    Decode.map3 SupplierItemDTO
        (Decode.field "id" (Decode.maybe Decode.string))
        (Decode.field "name" Decode.string)
        (Decode.field "description" (Decode.maybe Decode.string))


suppliersDecoder : Decode.Decoder SuppliersDTO
suppliersDecoder =
    (Decode.map SuppliersDTO (Decode.list supplierItemDecoder))


listDecoder : Decode.Decoder ListDTO
listDecoder =
    Decode.map7 ListDTO
        (Decode.field "id" (Decode.maybe Decode.string))
        (Decode.field "name" Decode.string)
        (Decode.field "description" (Decode.maybe Decode.string))
        (Decode.field "created" Decode.int)
        (Decode.field "items" (Decode.maybe <| Decode.list listItemDecoder))
        (Decode.field "status" (Decode.maybe Decode.int))
        (Decode.field "clientTag" (Decode.maybe Decode.string))


listItemDecoder : Decode.Decoder ListItemDTO
listItemDecoder =
    Decode.map6 ListItemDTO
        (Decode.field "productId" (Decode.maybe Decode.string))
        (Decode.field "quantity" Decode.int)
        (Decode.field "description" (Decode.maybe Decode.string))
        (Decode.field "status" Decode.int)
        (Decode.field "clientTag" (Decode.maybe Decode.string))
        (Decode.field "bought" Decode.int)


updateUserDecoder : Decode.Decoder UpdateUserDTO
updateUserDecoder =
    Decode.map4 UpdateUserDTO
        (Decode.field "login" (Decode.maybe Decode.string))
        (Decode.field "password" (Decode.maybe Decode.string))
        (Decode.field "id" (Decode.maybe Decode.string))
        (Decode.field "nick" (Decode.maybe Decode.string))


listsDecoder : Decode.Decoder ListsDTO
listsDecoder =
    Decode.map ListsDTO (Decode.field "items" (Decode.list listDecoder))


productDecoder : Decode.Decoder ProductDTO
productDecoder =
    Decode.map5 ProductDTO
        (Decode.field "id" (Decode.maybe Decode.string))
        (Decode.field "name" Decode.string)
        (Decode.field "tags" Decode.string)
        (Decode.field "description" (Decode.maybe Decode.string))
        (Decode.field "clientTag" (Decode.maybe Decode.string))


priceDecoder : Decode.Decoder ProductPriceDTO
priceDecoder =
    Decode.map3 ProductPriceDTO
        (Decode.field "productId" Decode.string)
        (Decode.field "supplierId" Decode.string)
        (Decode.field "price" Decode.float)


syncDecoder : Decode.Decoder SyncDTO
syncDecoder =
    Decode.map4 SyncDTO
        (Decode.field "userData" (Decode.maybe updateUserDecoder))
        (Decode.field "listsMeta" (Decode.maybe listsDecoder))
        (Decode.field "products" <| Decode.maybe <| Decode.list productDecoder)
        (Decode.field "prices" <| Decode.maybe <| Decode.list priceDecoder)
