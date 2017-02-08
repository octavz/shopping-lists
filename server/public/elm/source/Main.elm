module Main exposing (..)

import RouteUrl
import Main.Router as Router
import Main.Models exposing (..)
import Main.Messages exposing (..)
import Main.View exposing (..)
import Main.Update exposing (..)
import Time exposing (..)


init : ( Model, Cmd msg )
init =
    ( initialModel, Cmd.none )


subscriptions : Model -> Sub Msg
subscriptions model =
    Time.every (10 * second) SyncData


main =
    RouteUrl.program
        { delta2url = Router.delta2url
        , location2messages = Router.location2messages
        , init = init
        , view = view
        , update = update
        , subscriptions = subscriptions
        }
