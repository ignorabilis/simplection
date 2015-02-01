(ns simplection.core
  (:use [compojure.core :only (defroutes GET)]
        ring.util.response
        org.httpkit.server)
  (:require [compojure.route :as route]
            [compojure.handler :as handler]
            [ring.middleware.cors :refer [wrap-cors]]
            [cheshire.core :refer :all]))

(def clients (atom {}))

(defn ws
  [req]
  (with-channel req con
                (swap! clients assoc con true)
                (println con " connected")
                (on-close con (fn [status]
                                (swap! clients dissoc con)
                                (println con " disconnected. status: " status)))))

(defroutes routes
  (GET  "/" [] (resource-response "index.html" {:root "public"}))
  (GET "/ws" [] ws)
  (route/resources "/")
  (route/not-found "Page not found"))

(def application (-> (handler/site routes)
                     (wrap-cors
                      :access-control-allow-origin #"http://simplection.com.*$")))

(defn -main [& args]
  (let [port (Integer/parseInt
               (or (System/getenv "PORT") "4321"))]
    (run-server application {:port port :join? false})))


(future (loop []
          (doseq [client @clients]
             (send! (key client) (generate-string
                                 {:happiness (rand 10)})
                   false))
          (Thread/sleep 1000)
          (recur)))
