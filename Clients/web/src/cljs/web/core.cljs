(ns web.core
  (:require
    [reagent.core :as reagent :refer [atom]]
    [reagent.session :as session]
    [reagent-forms.core :refer [bind-fields]]
    [secretary.core :as secretary :include-macros true]
    [accountant.core :as accountant]
    [web.views.login :refer [login-template]]
    [web.views.commons :as c]
    [web.views.main :refer [main-template]]
    [web.views.register :refer [register-template]]
    [web.views.home :refer [home-page about-page]]))

(defn current-page []
  [:div [(session/get :current-page)]])


;(add-watch document :watcher
;           (fn [key atom old-state new-state]
;             (when (and
;                     (not (contains? old-state :current-page))
;                     (contains? new-state :current-page))
;               (let [page (new-state :current-page)]
;                 (prn "Paged changed")
;                 (cond
;                   (:current-page :main) (session/put! :current-page #'main-template))))
;             ))
;; -------------------------
;; Routes

(secretary/defroute (c/path :root) []
                    (session/put! :current-page #'home-page))

(secretary/defroute (c/path :login) []
                    (session/put! :current-page #'login-template))

(secretary/defroute (c/path :main) []
                    (session/put! :current-page #'main-template))

(secretary/defroute (c/path :register) []
                    (if (c/logged-in?)
                      (c/redirect :main)
                      (session/put! :current-page #'register-template)
                      )
                    )

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
