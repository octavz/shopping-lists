module Supplier.View exposing (viewSupplier)

import String
import Maybe exposing (..)
import Html exposing (label, text, input, button, div, h1, a, b)
import Html.Attributes exposing (..)
import Html.Events exposing (onInput, onClick, targetValue)
import Html.App
import Supplier.Messages exposing (..)
import Supplier.Model exposing (..)


viewSupplier : SupplierModel -> Html.Html SupplierMsg
viewSupplier model =
    let
        message =
            div [] [ text <| withDefault "" model.message ]
    in
        div [ class "container" ]
            [ viewItem model.current
            , text "table"
            , div [ class "row top5" ] [ message ]
            ]


viewItem : SupplierItemModel -> Html.Html SupplierMsg
viewItem model =
    div [ class "container" ]
        [ div [ class "row" ]
            [ div [ class "col-xs-4 col-xs-offset-4" ]
                [ div [ class "row top5" ]
                    [ h1 []
                        [ text "Suppliers" ]
                    ]
                , div
                    [ class "row top5" ]
                    [ div [ class "col-sm-4" ]
                        [ label [ class "text-right", for "login" ]
                            [ text "Name:" ]
                        ]
                    , div [ class "col-sm-8" ]
                        [ input
                            [ name "name"
                            , type' "text"
                            , onInput UpdateName
                            , value model.name
                            ]
                            []
                        ]
                    ]
                , div [ class "row top5" ]
                    [ div [ class "col-sm-4" ]
                        [ label [ class "text-right", for "pass" ]
                            [ text "Description:" ]
                        ]
                    , div [ class "col-sm-8" ]
                        [ input
                            [ name "description"
                            , type' "text"
                            , onInput UpdateDescription
                            , value <| withDefault "" model.description
                            ]
                            []
                        ]
                    ]
                , div [ class "row top7" ]
                    [ div [ class "col-md-2 col-md-offset-5" ]
                        [ button [ onClick SaveSupplierReq, class "btn btn-default" ]
                            [ text "Save" ]
                        ]
                    ]
                ]
            ]
        ]
