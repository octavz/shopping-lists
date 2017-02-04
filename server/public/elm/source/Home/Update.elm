module Home.Update exposing (updateHome)

import Debug
import Home.Model exposing (..)
import Home.Messages exposing (..)
import Time exposing (Time)
import Http


updateHome : HomeMsg -> HomeModel -> ( HomeModel, Cmd HomeMsg )
updateHome action model =
    let
        newId =
            case List.head model.items of
                Just item ->
                    (Result.withDefault 0 (String.toInt item.id)) + 1

                Nothing ->
                    0
    in
        case Debug.log "home-update" action of
            UpdateNewItem val ->
                ( { model | newItem = ShopListItem (toString newId) val 0, message = "" }, Cmd.none )

            OnAdd ->
                if model.newItem.name /= "" then
                  ( { model | items = model.newItem :: model.items, newItem = emptyShopListItem, message = "" }, Cmd.none )
                else
                  ( { model | message = "Item must have a text"}, Cmd.none )

            OnDelete val ->
                ( { model | items = List.filter (\x -> x.id /= val) model.items, message = "" }, Cmd.none )
