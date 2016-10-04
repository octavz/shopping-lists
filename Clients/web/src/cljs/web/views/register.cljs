(ns web.views.register
    (:require 
      [reagent.core :as reagent :refer [atom]]
      [reagent-forms.core :refer [bind-fields]]
      [web.views.commons :refer [row]]))

(def register-state (atom {}))

(defn login-valid? [] 
  (let [{login :login} @register-state]
    (> (count login) 0)))

(defn password-valid? []
  (let [{password :password confirm :confirm} @register-state]
    (if (not= password confirm) 
          (swap! register-state assoc :error "passwords not the same")
          (js/alert "pass ok")
        )))

(defn on-login [] 
  (if (and (password-valid?) (login-valid?))
    (js/alert "OK1")
    (js/alert "Login is empty")))

(defn login-input []
  (let [{err :error} @register-state]
    [:div.row
     [:label.col-sm-2.control-label "Login:"]
     [:div.col-sm-5
      [:input.form-control {:field :text :id :login :placeholder "login"}]]
     [:div.col-sm-5 [:label err]]
     ])
  )

(def register-form
  [:div.container
   (login-input)
   (row "Password" :password :password)
   (row "Confirm password" :password :confirm)
   [:div.row
    [:div.col-sm-offset-2.col-sm-5
     [:div.checkbox
      [:label [:input {:field :checkbox :id :remember}] "Remember me"]]]]
   [:div.row
    [:div.col-sm-offset-2.col-sm-1 [:button.btn.btn-default {:on-click on-login} "Login"]]
    [:div.col-sm-1 [:reset.btn.btn-default "Reset"]] ]])

(defn register-template [] (fn []
    [:div
     [:h2 "Register"]
     [bind-fields register-form register-state]
     [:label (str @register-state)]]))

