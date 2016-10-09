(ns web.views.register
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require
    [reagent.core :as reagent :refer [atom]]
    [reagent.session :as session]
    [reagent-forms.core :refer [bind-fields]]
    [cljs.core.async :refer [<!]]
    [web.views.commons :as c]
    [web.views.main :refer [main-template]]
    ))

(def document (reagent/atom {:login "aaa@aaa.com" :password "123456" :confirm "123456"}))

(defn document-changed
  [id value document]
  (prn "event:" (str document)))

(defn password-valid?
  [state]
  (let [{password :password confirm :confirm} @state]
    (and (> (count password) 0) (== password confirm))))

(defn send
  []
  (let [login (c/login-valid? document)
        pass (password-valid? document)]
    (if-not login
      (swap! document assoc :error-login "login is invalid")
      (swap! document dissoc :error-login))
    (if-not pass
      (swap! document assoc :error-password "passwords not the same")
      (swap! document dissoc :error-password))
    (when (and login pass)
      (go (let [response (<! (c/request-login document))]
            (do
              ;(aset js/window.location "href" "#login")
              (session/put! :user-data (:body response))
              (c/redirect :main)
              ))))))

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

(def container
  [:div.container
   login-input
   login-password
   (c/row "Confirm password" :password :confirm)
   [:div.row
    [:div.col-sm-offset-2.col-sm-5
     [:div.checkbox
      [:label [:input {:field :checkbox :id :remember}] "Remember me"]]]]
   [:div.row
    [:div.col-sm-offset-2.col-sm-1 [:button.btn.btn-default {:on-click send} "Login"]]
    [:div.col-sm-1 [:reset.btn.btn-default "Reset"]]]])

(defn register-template
  []
  (reagent/create-class
    {:get-initial-state (fn
                          []
                          (when (c/logged-in?)
                            (c/redirect :main)))
     :render            (fn
                          []
                          [:div
                           [:h2 "Register"]
                           [bind-fields container document]
                           [:label (str @document)]])}))



