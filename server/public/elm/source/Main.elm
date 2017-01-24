module Main exposing (..)

import RouteUrl
import Main.Router as Router
import Main.Models exposing (..)
import Main.View exposing (..)
import Main.Update exposing (..)


init : ( Model, Cmd msg )
init =
    ( initialModel, Cmd.none )


main =
    RouteUrl.program
        { delta2url = Router.delta2url
        , location2messages = Router.location2messages
        , init = init
        , view = view
        , update = update
        , subscriptions = \_ -> Sub.none
        }

