module Home.View exposing (viewHome)

import String
import Maybe exposing (..)
import Html exposing (..)
import Html.Attributes exposing (..)
import Html.Events exposing (onInput, onClick, targetValue)
import Home.Messages exposing (..)
import Home.Model exposing (..)

viewListItem : ShopListItem -> Html.Html HomeMsg
viewListItem model =
     li [ class "list-group-item" ]
        [ div [ class "row" ]
            [ div [ class "col-xs-4" ]
                [ text model.name ]
            , div [ class "col-xs-8 text-right" ]
                [ a [onClick (OnDelete model.id)]
                    [ i [ class "remove glyphicon glyphicon-remove-sign glyphicon-white" ]
                        []
                    ]
                ]
            ]
        ]


viewHome : HomeModel -> Html.Html HomeMsg
viewHome model =
    div [ class "container" ]
        [ div [ class "row" ]
            [ div [ class "col-md-6 col-md-offset-3" ]
                [ h3 [ class "text-center" ]
                    [ text "Create a quick list!" ]
                ]
            ]
        , div [ class "row" ]
            [ div [ class "col-md-6 col-md-offset-3" ]
                [ div [ class "row" ]
                    [ div [ class "col-md-10" ]
                        [ input [ class "form-control", placeholder "add item here", type_ "text", onInput UpdateNewItem, value model.newItem.name ]
                            []
                        ]
                    , div [ class "col-md-2" ]
                        [ button [ class "btn btn-default", type_ "button", onClick OnAdd ]
                            [ text "Add" ]
                        ]
                    ]
                ]
            ]
        , div [ class "row" ]
            [ div [ class "col-md-6 col-md-offset-3" ]
                [ ul [ class "list-group" ]
                     (List.map viewListItem model.items)
                ]
            ]
        ]


