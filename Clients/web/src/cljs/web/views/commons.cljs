(ns web.views.commons
  (:require
    [cljs.core.async :refer [<!]]
    [reagent.session :as session]
    [cljs-http.client :as http :refer [post get]]))

(def base-url "http://localhost:9000/api")

(def action
  {:login    (str base-url "/login")
   :register (str base-url "/register")})

(def path
  {:root     "/"
   :login    "/#login"
   :register "/#register"
   :main     "/#main"})

(defn json-request
  [data]
  {:headers     {"accept" "application/json"}
   :json-params data})

(defn row [label input id]
  [:div.row
   [:label.col-sm-2.control-label (str label ":")]
   [:div.col-sm-5 [:input.form-control {:field input :id id :placeholder label}]]])

(defn request-login [state]
  (let [{login :login password :password} @state]
    (http/post
      (:login action)
      (json-request {:userName login :password password :clientId "1" :grantType "password" :clientSecret "secret"}))))

(defn login-valid?
  [state]
  (let [{login :login} @state]
    (> (count login) 0)))

(defn redirect
  [url]
  (set! (.-location js/document) (path url)))

(defn logged-in?
  []
  (not (nil? (session/get :user-data))))
