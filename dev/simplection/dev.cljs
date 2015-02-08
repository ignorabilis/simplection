(ns simplection.dev
    (:require [simplection.core :as core]
              [figwheel.client :as fw]))

(fw/start {
  :on-jsload (fn []
               ;; (stop-and-start-my app)
               )})
(core/init!)
