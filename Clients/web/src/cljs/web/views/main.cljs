(ns web.views.main
  (:require
    [reagent.core :as reagent :refer [atom]]
    [reagent-forms.core :refer [bind-fields]]))

(def test-items [
                 {:productId "1" :description "something 1"}
                 {:productId "2" :description "something 2"}
                 {:productId "3" :description "something 3"}
                 {:productId "4" :description "something 4"}
                 {:productId "5" :description "something 5"}
                 ])

(defn main-form
  [document]
  [:ul
   (for [item test-items]
     ^{:key (:productId item)}
     [:li [:div [:label (:description item)]]])])


