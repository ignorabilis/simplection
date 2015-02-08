(ns simplection.views.designer
  (:require [simplection.templates.layout :refer [layout]]))

(defn init-left-menu-measurements []
  [:div.row {:id "designer-left-menu-measurements"
             :style {:height "40%"}}
   "Measurements"])

(defn init-left-menu-dimensions []
  [:div.row {:id "designer-left-menu-dimensions"
             :style {:height "40%"}}
   "Dimensions"])

(defn init-left-menu-it []
  [:div.row {:id "designer-left-menu-it"
             :style {:height "20%"}}
   "IT"])

(defn init-left-menu []
  [:div.col-xs-2.full-height {:id "designer-left-menu"}
   (init-left-menu-measurements)
   (init-left-menu-dimensions)
   (init-left-menu-it)])

(defn init-center-columns-container []
  [:div.row {:id "designer-center-columns-container"
             :style {:height "20%"}}
   "Columns"])

(defn init-center-rows-container []
  [:div.col-xs-2.full-height {:id "designer-center-rows-container" } "Rows"])

(defn init-center-plot-area []
  [:div.col-xs-10.full-height {:id "designer-center-plot-area"} "Plot Area"])

(defn init-center []
  [:div.col-xs-8.full-height {:id "designer-center"}
   (init-center-columns-container)
   [:div.row {:style {:height "80%"}}
    (init-center-rows-container)
    (init-center-plot-area)]])

(defn init-right-menu-graph-types []
  [:div.row {:id "designer-right-menu-graph-types"
             :style {:height "60%"}}
   "Graph Types"])

(defn init-right-menu-settings []
  [:div.row {:id "designer-right-menu-settings"
             :style {:height "40%"}}
   "Settings"])

(defn init-right-menu []
  [:div.col-xs-2.full-height {:id "designer-right-menu"} 
   (init-right-menu-graph-types)
   (init-right-menu-settings)])

(defn designer-init []
  (layout
    [:div.container-fluid {:id "designer-container"}
     [:div.row-fluid.full-height
      (init-left-menu)
      (init-center)
      (init-right-menu)]]))
