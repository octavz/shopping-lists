module Home.Model exposing (..)

type alias ShopListItem =
    { id : String
    , name : String
    , quantity : Int
    }

type alias HomeModel =
  {
    newItem: ShopListItem
    , items: List ShopListItem
    , message: String
  }

emptyShopListItem: ShopListItem
emptyShopListItem = ShopListItem "" "" 0
