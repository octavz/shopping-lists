(ns web.views.home
    (:require 
      [reagent.core :as reagent :refer [atom]]
      [reagent-forms.core :refer [bind-fields]]))

(defn home-page []
  [:div [:h2 "Welcome to web"]
   [:div [:a {:href "/#login"} "go to login page"]]
   [:div [:a {:href "/#register"} "go to register page"]]
   [:div [:a {:href "/#about"} "go to about page"]] ])

(defn about-page []
  [:div [:h2 "About web"]
   [:div [:a {:href "/"} "go to the home page"]]])

