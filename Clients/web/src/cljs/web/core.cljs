(ns web.core
    (:require 
      [reagent.core :as reagent :refer [atom]]
      [reagent.session :as session]
      [reagent-forms.core :refer [bind-fields]]
      [secretary.core :as secretary :include-macros true]
      [accountant.core :as accountant]
      [web.views.login :refer [login-template]]
      [web.views.register :refer [register-form]]
      [web.views.home :refer [home-page about-page]]
      ))

(def document (reagent/atom {:login "aaa@aaa.com" :password "123456" :confirm "123456"}))

(defn document-changed 
  [id value document]
  (prn "event:" (str document)))

(add-watch document :watcher
  (fn [key atom old-state new-state]
    (prn "-- Atom Changed --")
    (prn "key" key)
    (prn "atom" atom)
    (prn "old-state" old-state)
    (prn "new-state" new-state)))

(defn current-page []
  [:div [(session/get :current-page)]])

(defn register-template 
  [] 
  (fn 
    []
    [:div
     [:h2 "Register"]
     [bind-fields (register-form document) document document-changed]
     [:label (str @document)]]))
  
;; -------------------------
;; Routes

(secretary/defroute "/" []
  (session/put! :current-page #'home-page))

(secretary/defroute "/#login" []
  (session/put! :current-page #'login-template))

(secretary/defroute "/#register" []
  (session/put! :current-page #'register-template))

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
