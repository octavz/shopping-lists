module Home.Update exposing (updateHome)

import Debug
import Main.Models exposing (..)
import Home.Messages exposing (..)
import Time exposing (Time)
import Http
import Main.Dtos exposing (ListDTO, ListItemDTO)
import Maybe exposing (withDefault)


items : HomeModel -> List ListItemDTO
items model =
    case model.content of
        Just lst ->
            withDefault [] lst.items

        _ ->
            []


newItem : HomeModel -> ListItemDTO
newItem model =
    withDefault (ListItemDTO Nothing 0 Nothing 0 Nothing 0) model.newItem


updateHome : HomeMsg -> HomeModel -> ( HomeModel, Cmd HomeMsg )
updateHome action model =
    let
        newId : Maybe String
        newId =
            Just <| toString <| List.length (items model) + 1

        c =
            withDefault (ListDTO (Just "1") "main" Nothing 0 (Just []) Nothing Nothing) model.content
    in
        case Debug.log "home-update" action of
            UpdateNewItem val ->
                ( { model | newItem = Just (ListItemDTO newId 0 (Just val) 0 Nothing 0), message = Just "" }, Cmd.none )

            OnAdd ->
                if (newItem model).description /= Nothing then
                    ( { model
                        | content = Just { c | items = Just <| (newItem model) :: (items model) }
                        , newItem = Nothing
                        , message = Just ""
                      }
                    , Cmd.none
                    )
                else
                    ( { model | message = Just "Item must have a text" }, Cmd.none )

            OnDelete val ->
                let
                    filtered : List ListItemDTO
                    filtered =
                        List.filter (\x -> x.productId /= val) (items model)
                in
                    ( { model
                        | content = Just { c | items = Just filtered }
                        , message = Just ""
                      }
                    , Cmd.none
                    )
