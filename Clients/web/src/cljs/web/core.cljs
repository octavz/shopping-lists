(ns web.core
    (:require [reagent.core :as reagent :refer [atom]]
              [reagent.session :as session]
              [reagent-forms.core :refer [bind-fields]]
              [secretary.core :as secretary :include-macros true]
              [accountant.core :as accountant]))

;; -------------------------
;; Views

(def doc-state (reagent/atom {}))

(defn home-page []
  [:div [:h2 "Welcome to web"]
   [:div [:a {:href "/#login"} "go to login page"]]
   [:div [:a {:href "/#about"} "go to about page"]] ])

(defn row [label input id]
  [:div.row
    [:label.col-sm-2.control-label (str label ":")]
    [:div.col-sm-5 [:input.form-control {:field input :id id :placeholder label}]]])

(defn login-valid? [] 
  (let [{login :login} @doc-state]
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
    [:div.col-sm-1 [:reset.btn.btn-default "Reset"]] ]])

(defn login-page []
  (fn []
    [:div
     [:h2 "Login"]
     [bind-fields login-form doc-state]
     [:label (str @doc-state)]]))

(defn about-page []
  [:div [:h2 "About web"]
   [:div [:a {:href "/"} "go to the home page"]]])

(defn current-page []
  [:div [(session/get :current-page)]])

;; -------------------------
;; Routes

(secretary/defroute "/" []
  (session/put! :current-page #'home-page))

(secretary/defroute "/#login" []
  (session/put! :current-page #'login-page))

(secretary/defroute "/#about" []
  (session/put! :current-page #'about-page))
;; -------------------------
;; Initialize app

(defn mount-root []
  (reagent/render [current-page] (.getElementById js/document "app")))

(defn init! []
  (accountant/configure-navigation!
    {:nav-handler
     (fn [path]
       (secretary/dispatch! path))
     :path-exists?
     (fn [path]
       (secretary/locate-route path))})
  (accountant/dispatch-current!)
  (mount-root))
