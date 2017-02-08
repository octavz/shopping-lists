module Home.Messages exposing (..)

type HomeMsg
    = UpdateNewItem String
    | OnAdd
    | OnDelete (Maybe String)

