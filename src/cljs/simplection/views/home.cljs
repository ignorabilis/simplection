(ns simplection.views.home
    (:require [simplection.templates.layout :refer [layout]]))

(defn home-page []
  (layout
   [:div
    [:h2 "Welcome to Simplection"]
    [:div.main-header
     [:div.jumbotron.gradient
      [:div
       [:h1 "It all makes sense now"]
        [:p "Discover your data. Structure the future. Capitalize on both."]]]]]))

(defn pricing-page []
  (layout
   [:div
    [:h1 "Buy Simplection"]
    [:h3 "Or Upgrade Existing Plan"]]))

(defn start-now []
  (layout
   [:div
    [:h2 "Header here"]
    [:h2 "Image here"]]))
