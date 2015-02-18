(ns app
  (:require [garden.def :refer [defstylesheet defstyles]]
            [garden.units :refer [px]]
            [garden.color :as color]))

(def subtle-theme-color (color/rgba 100, 50, 50, 0.1))
(def container-border-color (color/rgb 255, 255, 255))
(def draggable-border-color (color/rgb 128, 128, 128))

(def height
  [:html :body :.full-height
   {:height "100%"}])

(def height-2
  [:#designer-container.container-fluid
   {:height "85%"}])

(def container
  [:#designer-container.container-fluid
   [:div
    {:background-color subtle-theme-color
     :border [["solid" (px 1) container-border-color]]}]])

(def draggable-chart-item
  [:.draggable-chart-item
   {:border [["solid" (px 1) draggable-border-color]]
    :border-radius (px 4)
    :background-color subtle-theme-color
    :display "inline-block"
    :padding [[(px 2) (px 10)]]}])

(defstyles app
  height
  height-2
  container
  draggable-chart-item)
