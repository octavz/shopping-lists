module Main.Dtos exposing (..)


type alias LoginDTO =
    { login : String
    , password : String
    }


type alias UpdateUserDTO =
    { login : Maybe String
    , password : Maybe String
    , id : Maybe String
    , nick : Maybe String
    }


type alias ListItemDTO =
    { productId : Maybe String
    , quantity : Int
    , description : Maybe String
    , status : Int
    , clientTag : Maybe String
    , bought : Int
    }


type alias ListDTO =
    { id : Maybe String
    , name : String
    , description : Maybe String
    , created : Int
    , items : Maybe (List ListItemDTO)
    , status : Maybe Int
    , clientTag : Maybe String
    }


type alias ListsDTO =
    { items : List ListDTO
    }


type alias ProductDTO =
    { id : Maybe String
    , name : String
    , tags : String
    , description : Maybe String
    , clientTag : Maybe String
    }


type alias ProductPriceDTO =
    { productId : String
    , supplierId : String
    , price : Float
    }


type alias SyncDTO =
    { userData : Maybe UpdateUserDTO
    , listsMeta : Maybe ListsDTO
    , products : Maybe (List ProductDTO)
    , prices : Maybe (List ProductPriceDTO)
    }


type alias SupplierItemDTO =
    { id : Maybe String
    , name : String
    , description : Maybe String
    }

type alias SuppliersDTO =
    { items : List SupplierItemDTO
    }
