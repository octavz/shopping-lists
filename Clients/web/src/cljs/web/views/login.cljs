(ns web.views.login
    (:require 
      [reagent.core :as reagent :refer [atom]]
      [reagent-forms.core :refer [bind-fields]]
      [web.views.commons :refer [row]]))

(def login-state (atom {}))

(defn login-valid? [] 
  (let [{login :login} @login-state]
    (> (count login) 0)))

(defn on-login [] 
  (if (login-valid?)
    (js/alert "OK")
    (js/alert "Login is empty")))

(def login-form
  [:div.container
   (row "Login"  :text :login)
   (row "Password" :password :password)
   [:div.row
    [:div.col-sm-offset-2.col-sm-5
     [:div.checkbox
      [:label [:input {:field :checkbox :id :remember}] "Remember me"]]]]
   [:div.row
    [:div.col-sm-offset-2.col-sm-1 [:button.btn.btn-default {:on-click on-login} "Login"]]
    [:div.col-sm-1 [:reset.btn.btn-default "Reset"]] ]
   [:div.row
    [:div.col-sm-offset-2.col-sm-5
      [:a {:href "/#register"} "go to register page"]]]])

(defn login-template []
  (fn []
    [:div
     [:h2 "Login"]
     [bind-fields login-form login-state]
     [:label (str @login-state)]]))

