(ns web.core
    (:require 
      [reagent.core :as reagent :refer [atom]]
      [reagent.session :as session]
      [reagent-forms.core :refer [bind-fields]]
      [secretary.core :as secretary :include-macros true]
      [accountant.core :as accountant]
      [web.views.login :refer [login-template]]
      [web.views.register :refer [register-template]]
      [web.views.home :refer [home-page about-page]]
      ))

(defn current-page []
  [:div [(session/get :current-page)]])

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
