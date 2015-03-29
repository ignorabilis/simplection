(ns simplection.views.perspectives.design
  (:require [reagent.core :refer [atom]]
            [jayq.core :as $]
            [simplection.templates.layout :as layout :refer [layout]]
            [cljs.core.async :refer [<! >! chan close! sliding-buffer put! alts!]]
            [simplection.canvasgraph.coordinates :as cs]
            [simplection.canvasgraph.path :as p]
            [simplection.report :as rep]
            [simplection.canvasgraph.aggregator :as agg]
            [simplection.canvasgraph.aggregates :as aggs]
            [simplection.canvasgraph.data :as g-data]
            [simplection.canvasgraph.definition :as definition]
            [simplection.canvasgraph.series :as series]
            [simplection.range :as ran]
            [simplection.canvasgraph.scale :as scale]
            [simplection.canvasgraph.intersection :as inter]
            [simplection.canvasgraph.csnormalization :as norm]
            [historian.core :as hist]
            [historian.keys]
            [simplection.rendering :as rendering])
  (:require-macros [cljs.core.async.macros :as m :refer [go alt!]]))

(def selected-report (atom "Sales Report 2014"))

(defn init-toolbar [] 
  [:div.row-fluid.white-border {:id "designer-toolbar"
                                    :style {:height "6%"
                                            :padding-left "10px"}}
       [:label 
        "Edit: "
        [:select {:on-change #(reset! selected-report (-> % .-target .-value))
                  }
         [:option "Sales Report 2014"]
         [:option "Sales Report 2015"]
         [:option "Sales Reports Theme"]
         [:option "Internal Reports Theme"]]]
       [:span.glyphicon.glyphicon-fast-backward.span-btn]
       [:span.glyphicon.glyphicon-fast-forward.span-btn]
       [:span.glyphicon.glyphicon-floppy-save.span-btn]
       [:span.glyphicon.glyphicon-open.span-btn]
       [:h4
        {:style {:display "inline-block"}}
        (str @selected-report " / Main Theme")]])

(defn init-color-wheel []
  (list
    [:br]
    [:div.white-border {:style {:padding "10px"}}
     [:h3 "Color Rules"]
     [:ul
      [:li [:a "Complement"]]
      [:li [:a "Analogue"]]
      [:li [:a "Monochrome"]]
      [:li [:a "Custom"]]]]
    
    [:br]
    [:div.white-border {:style {:padding "10px"}}
     [:img {:src "images/color-wheel_crosshair.png"}]
     [:img {:src "images/color-switch-tool.png" :style {:margin-left "30px"}} ]]
    
    [:br]
    [:div.white-border {:style {:padding "10px"}}
    [:div {:style {:background-color "transparent" }}
     [:div.color-block {:style {:background-color "#d2232a"}}]
     [:div.color-block {:style {:background-color "#fcaf3b"}}]
     [:div.color-block {:style {:background-color "#fef200"}}]
     [:div.color-block {:style {:background-color "#40af49"}}]
     [:div.color-block {:style {:background-color "#00adef"}}]]]
    ))

(defn init-toolbox []
  (list
    [:span.glyphicon.glyphicon-plus.span-btn]
    [:span.glyphicon.glyphicon-minus.span-btn]
    [:span.glyphicon.glyphicon-pencil.span-btn]
    [:span.glyphicon.glyphicon-search.span-btn]
    [:span.glyphicon.glyphicon-star-empty.span-btn]
    [:span.glyphicon.glyphicon-th-list.span-btn]
    [:span.glyphicon.glyphicon-trash.span-btn]
    [:span.glyphicon.glyphicon-file.span-btn]
    [:span.glyphicon.glyphicon-download-alt.span-btn]
    [:span.glyphicon.glyphicon-book.span-btn]
    [:span.glyphicon.glyphicon-screenshot.span-btn]
    
    [:span.glyphicon.glyphicon-eye-open.span-btn]
    [:span.glyphicon.glyphicon-fire.span-btn]
    [:span.glyphicon.glyphicon-magnet.span-btn]
    [:span.glyphicon.glyphicon-screenshot.span-btn]
    [:br]
    ))

(defn init-content []
  (list
    (init-toolbox)
    [:button.btn.btn-lg.btn-success.tag {:style {:padding "30px"}} "Title"]
    [:button.btn.btn-lg.btn-success.tag {:style {:padding "30px"}} "Sub"]
    [:button.btn.btn-lg.btn-success.tag {:style {:padding "30px"}} "Text 1"]
    [:button.btn.btn-lg.btn-success.tag {:style {:padding "30px"}} "Text 2"]
    [:button.btn.btn-lg.btn-success.tag {:style {:padding "30px"}} "Text 3"]
    [:br]
    [:button.btn.btn-lg.btn-primary.tag {:style {:padding "30px"}} "Logo Main"]
    [:button.btn.btn-lg.btn-primary.tag {:style {:padding "30px"}} "Logo 1"]
    [:button.btn.btn-lg.btn-primary.tag {:style {:padding "30px"}} "Logo 2"]
    [:button.btn.btn-lg.btn-primary.tag {:style {:padding "30px"}} "Logo 3"]
    [:br]
    [:button.btn.btn-lg.btn-info.tag {:style {:padding "30px"}} "SH 1"]
    [:button.btn.btn-lg.btn-info.tag {:style {:padding "30px"}} "SH 2"]
    [:button.btn.btn-lg.btn-info.tag {:style {:padding "30px"}} "SH 3"]
    [:button.btn.btn-lg.btn-info.tag {:style {:padding "30px"}} "SH 4"]
    [:button.btn.btn-lg.btn-info.tag {:style {:padding "30px"}} "SH 5"]
    [:button.btn.btn-lg.btn-info.tag {:style {:padding "30px"}} "SH 2"]
    [:button.btn.btn-lg.btn-info.tag {:style {:padding "30px"}} "SH 7"]
    [:br]
    [:button.btn.btn-lg.btn-warning.tag {:style {:padding "60px"}} "Graph 1"]
    [:button.btn.btn-lg.btn-warning.tag {:style {:padding "60px"}} "Graph 2"]
    [:br]
    [:button.btn.btn-lg.btn-danger.tag {:style {:padding "60px"}} "Graph 3"]
    [:button.btn.btn-lg.btn-danger.tag {:style {:padding "60px"}} "Graph 4"]
    ))

(defn designer-init []
  (layout
    [:div.container-fluid.row
     {:id "designer-container"
      :style {:min-height "600px"}}
     [:div.row-fluid.full-height      
      [:a.menu-nav {:href "#/start-now/ba"} " BA Perspective "]
      [:a.menu-nav {:href "#/start-now/it"} " IT Perspective "]
      (init-toolbar)
      [:div {:style {:height "91%"}}
       [:div.col-xs-3.full-height.white-border
        (init-color-wheel)]
       [:div.col-xs-9.full-height.white-border
        (init-content)]]]]))