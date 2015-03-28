(ns simplection.templates.layout
  (:require [jayq.core :as $]))

(defn header []
  [:div.navbar.row
   [:div
    [:a {:href "#/"} "Simplection"]
    " "
    [:a {:href "#/pricing"} "Pricing"]
    " "
    [:a {:href "#/start-now/ba"} "Start Now"]]])

(defn footer []
  [:div.navbar.row
   [:div
    [:a {:href "#/"} "Simplection"]
    " "
    [:a {:href "#/data-mining"} "Data Mining"]
    " "
    [:a {:href "#/reporting"} "Reporting"]]])

(defn layout [content]
  [:div.full-height.container-fluid
   [header] 
   content 
   [footer]])

(defn show-notification [message t]
  (let [sel (case t
              :error "#error-message-div"
              :success "#success-message-div"
              nil)]
  (-> ($/$ sel)
      ($/fade-in 500)
      ($/fade-out 3500)
      ($/html message))))