(ns simplection.handler
   (:require [compojure.handler :as handler]
             [compojure.route :as route]
             [compojure.core :refer [GET defroutes]]
             [ring.util.response :refer [resource-response response]]))


(defn index-page []
  (hiccup.page/html5
    [:head
      [:title "Hello World"]]
    [:body
      [:div {:id "content"} "Hello World"]]))

(defroutes app
  (GET "/" [] (index-page))
  (GET  "/ind" [] (resource-response "index.html" {:root "public"}))
  (route/resources "/")
  (route/not-found "Page not found"))
