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
    {:background-color subtle-theme-color}]])

(def draggable-chart-item
  [:.draggable-chart-item 
   {:border [["solid" (px 1) draggable-border-color]]
    :border-radius (px 4)
    :background-color subtle-theme-color
    :display "inline-block"
    :padding [[(px 2) (px 10)]]}])

(def white-border 
  [:.white-border
   {:border "solid 1px white"}])

(def white-border-top
  [:.white-border-top
   {:border-top "solid 1px white"}])

(def menu-nav
  [:.menu-nav
   {:margin "10px"}])

(def span-btn
  [:.span-btn
   {:margin "2px 10px"
    :font-size "20px"
    :border "solid 2px transparent"
    :padding "8px"}])

(def span-btn-hover
  [:.span-btn:hover
   {:border "solid 2px white"}])

(def highlighted 
  [:.highlighted
   {:border-color "hsla(0,33.333332%,29.411764%,0.2) !important"}])

(def tag 
  [:.tag
   {:margin "10px"}])

(def color-block
  [:.color-block
   {:border "solid 1px white"
    :width "20%"
    :padding-top "20%"
    :display "inline-block"}])

(def icon-img
  [:.icon-img
   {:width "26px"
    :height "26px"
    :font-size "20px"
    :margin-right "10px"}])

(def compound-btn
  [:.compound-btn {:border "solid 2px transparent"
                   :padding "10px"
                   }])

(def compound-btn-hover
  [:.compound-btn:hover { :border"solid 2px white"}])

(def transparent
  [:.transparent { :background-color "transparent"}])

(def right-spaced
  [:.right-spaced {:padding-right "40px"
                   :min-width "150px"
                   :display "inline-block"}])

(def large-font
  [:.large-font {:font-size "20px"}])

(defstyles app
  height
  height-2
  container
  draggable-chart-item
  white-border
  white-border-top
  highlighted
  menu-nav
  span-btn-hover
  span-btn
  tag
  color-block
  icon-img
  compound-btn
  compound-btn-hover
  transparent
  right-spaced
  large-font
)