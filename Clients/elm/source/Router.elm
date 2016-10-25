module Router exposing (delta2url, location2messages)

import RouteHash exposing (HashUpdate)
import RouteUrl exposing (HistoryEntry(..), UrlChange)
import RouteUrl.Builder as Builder exposing (Builder)
import Navigation exposing (Location)
import Models exposing (..)
import Messages exposing (..)


delta2url : Model -> Model -> Maybe UrlChange
delta2url previous current =
    case Debug.log "currentPage" current.activePage of
        PageAccessDenied ->
            Nothing

        PageLogin ->
            Just <| UrlChange NewEntry "/#login"

        PageRegister ->
            Just <| UrlChange NewEntry "/#register"

        PageMyAccount ->
            Just <| UrlChange NewEntry "/#my-account"

        PageNotFound ->
            Just <| UrlChange NewEntry "/#404"


location2messages : Location -> List Msg
location2messages location =
    case location.hash of
        "" ->
            []

        "#login" ->
            [ SetActivePage PageLogin ]

        "#register" ->
            [ SetActivePage PageRegister ]

        "#my-account" ->
            [ SetActivePage PageMyAccount ]

        "#404" ->
            [ SetActivePage PageNotFound ]

        _ ->
            [ SetActivePage PageNotFound ]
