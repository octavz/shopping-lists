module Home.View exposing (viewHome)

import String exposing (..)
import Maybe exposing (..)
import Html exposing (..)
import Html.Attributes exposing (..)
import Html.Events exposing (onInput, onClick, targetValue)
import Home.Messages exposing (..)
import Main.Models exposing (..)
import Main.Dtos exposing (..)


viewListItem : ListItemDTO -> Html.Html HomeMsg
viewListItem model =
    li [ class "list-group-item" ]
        [ div [ class "row" ]
            [ div [ class "col-xs-4" ]
                [ text <| withDefault "" model.description ]
            , div [ class "col-xs-8 text-right" ]
                [ a [ attribute "role" "button", onClick (OnDelete model.productId) ]
                    [ i [ class "remove glyphicon glyphicon-remove-sign glyphicon-white" ]
                        []
                    ]
                ]
            ]
        ]




viewHome : HomeModel -> Html.Html HomeMsg
viewHome model =
    let
        newItem = withDefault emptyListItem model.newItem
        items =
            case model.content of
                Just lst ->
                    withDefault [] lst.items

                Nothing ->
                    []
    in
        div [ class "container" ]
            [ div [ class "row" ]
                [ div [ class "col-md-6 col-md-offset-3" ]
                    [ h3 [ class "text-center" ]
                        [ text "Create a quick list!" ]
                    ]
                ]
            , div [ class "row" ]
                [ div
                    [ classList
                        [ ( "col-md-6 col-md-offset-3", True )
                        , ( "hide", (model.message == Nothing) )
                        ]
                    ]
                    [ div [ class "alert alert-danger" ] [ text <| withDefault "" model.message ] ]
                , div [ class "col-md-6 col-md-offset-3" ]
                    [ div [ class "row" ]
                        [ div [ class "col-md-10" ]
                            [ input
                                [ class "form-control"
                                , placeholder "add item here"
                                , type_ "text"
                                , onInput UpdateNewItem
                                , value <| withDefault "" newItem.description
                                ]
                                []
                            ]
                        , div [ class "col-md-2" ]
                            [ button
                                [ class "btn btn-default"
                                , attribute "role" "button"
                                , type_ "button"
                                , onClick OnAdd
                                ]
                                [ text "Add" ]
                            ]
                        ]
                    ]
                ]
            , div [ class "row" ]
                [ div [ class "col-md-6 col-md-offset-3" ]
                    [ ul [ class "list-group" ]
                        (List.map viewListItem items)
                    ]
                ]
            ]
