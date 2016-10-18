(ns web.views.main
  (:require
    [reagent.core :as reagent :refer [atom]]
    [reagent-forms.core :refer [bind-fields]]))

(def document (reagent/atom {:login "aaa@aaa.com" :password "123456" :confirm "123456"}))

(def test-items [
                 {:productId "1" :description "something 1"}
                 {:productId "2" :description "something 2"}
                 {:productId "3" :description "something 3"}
                 {:productId "4" :description "something 4"}
                 {:productId "5" :description "something 5"}
                 ])

(def main-form
  [:ul
   (for [item test-items]
     ^{:key (:productId item)}
     [:li [:div [:label (:description item)]]])])

(defn main-template
  []
  (fn
    []
    [:div
     [:h2 "Lists"]
     [bind-fields main-form document]
     [:label (str @document)]]))
