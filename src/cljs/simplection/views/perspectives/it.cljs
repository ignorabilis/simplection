(ns simplection.designer.perspectives.it
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

(defn designer-init []
  (layout
    [:div.container-fluid.row
     {:id "designer-container"
      :style {:min-height "600px"}}
     [:div.row-fluid.full-height      
      [:a.menu-nav {:href "#/start-now/ba"} " BA Perspective "]
      [:a.menu-nav {:href "#/start-now/ux"} " Design Perspective "]
      ]]))