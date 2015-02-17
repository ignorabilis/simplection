(ns simplection.server.core
  (:require [compojure.route :as route]
            [compojure.handler :as handler]
            [compojure.core :refer [defroutes GET]]
            [org.httpkit.server :refer [run-server with-channel send! on-close]]
            [ring.middleware.cors :refer [wrap-cors]]
            [ring.util.response :refer [resource-response]]))

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
               (or (System/getenv "PORT") "3450"))]
    (run-server application {:port port :join? false})))

(future (loop []
          (doseq [client @clients]
             (send! (key client) (str {:happiness (rand 100)})
                   false))
          (Thread/sleep 1000)
          (recur)))
