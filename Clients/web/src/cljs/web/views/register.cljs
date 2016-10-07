(ns web.views.register
    (:require-macros [cljs.core.async.macros :refer [go]])
    (:require 
      [reagent.core :as reagent :refer [atom]]
      [reagent-forms.core :refer [bind-fields]]
      [cljs.core.async :refer [<!]]
      [cljs-http.client :as http :refer [post get]]
      [web.views.commons :as c]))


(defn password-valid? 
  [state]
  (let [{password :password confirm :confirm} @state]
    (and (> (count password) 0) (== password confirm))))

(defn send 
  [register-state] 
  (let [login (c/login-valid? register-state)
        pass (password-valid? register-state)]
    (if-not login
            (swap! register-state assoc :error-login "login is invalid")
            (swap! register-state dissoc :error-login))
    (if-not pass
            (swap! register-state assoc :error-password "passwords not the same")  
            (swap! register-state dissoc :error-password))
    (when (and login pass) 
          (go (let [response (<! (c/request-login register-state))]
                (do
                  ;(aset js/window.location "href" "#login")
                  (swap! register-state assoc :user-data (:body response)) ))))))

(def login-input
  [:div.row
   [:label.col-sm-2.control-label "Login:"]
   [:div.col-sm-5
    [:input.form-control {:field :text :id :login :placeholder "Login"}]]
   [:div.col-sm-5.form-group.has-error 
    [:label.control-label {:field :label :id :error-login}]]])

(def login-password 
  [:div.row
   [:label.col-sm-2.control-label "Password:"]
   [:div.col-sm-5
    [:input.form-control {:field :text :id :password :placeholder "Password"}]]
   [:div.col-sm-5.form-group.has-error 
    [:label.control-label {:field :label :id :error-password}]]])

(defn register-form 
  [document]
  [:div.container
   login-input
   login-password
   (c/row "Confirm password" :password :confirm)
   [:div.row
    [:div.col-sm-offset-2.col-sm-5
     [:div.checkbox
      [:label [:input {:field :checkbox :id :remember}] "Remember me"]]]]
   [:div.row
    [:div.col-sm-offset-2.col-sm-1 [:button.btn.btn-default {:on-click (fn [] (send document))} "Login"]]
    [:div.col-sm-1 [:reset.btn.btn-default "Reset"]] ]])



