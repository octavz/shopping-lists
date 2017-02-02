module Main.Router exposing (delta2url, location2messages)

import RouteHash exposing (HashUpdate)
import RouteUrl exposing (HistoryEntry(..), UrlChange)
import RouteUrl.Builder as Builder exposing (Builder)
import Navigation exposing (Location)
import Main.Models exposing (..)
import Main.Messages exposing (..)


delta2url : Model -> Model -> Maybe UrlChange
delta2url previous current =
    case Debug.log "currentPage" current.activePage of
        PageAccessDenied ->
            Nothing

        PageHome ->
            Just <| UrlChange NewEntry "/#"

        PageLogin ->
            Just <| UrlChange NewEntry "/#login"

        PageRegister ->
            Just <| UrlChange NewEntry "/#register"

        PageMyAccount ->
            Just <| UrlChange NewEntry "/#account"

        PageSuppliers ->
            Just <| UrlChange NewEntry "/#suppliers"

        PageNotFound ->
            Just <| UrlChange NewEntry "/#404"


location2messages : Location -> List Msg
location2messages location =
    case location.hash of
        "" ->
            [SetActivePage PageHome]

        "#login" ->
            [ SetActivePage PageLogin ]

        "#register" ->
            [ SetActivePage PageRegister ]

        "#account" ->
            [ SetActivePage PageMyAccount ]

        "#suppliers" ->
            [ SetActivePage PageSuppliers ]

        "#404" ->
            [ SetActivePage PageNotFound ]

        _ ->
            [ SetActivePage PageNotFound ]
