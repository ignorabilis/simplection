(ns simplection.canvasgraph.scale
  (:require [simplection.hashmap-ext :as hme]
            [simplection.canvasgraph.series :as series]
            [simplection.canvasgraph.coordinates :as coordinates]
            [simplection.canvasgraph.definition :as definition]
            [simplection.canvasgraph.ascale :as ascale]
     #+cljs [simplection.canvasgraph.ascale :refer [Category Numeric]])
  (#+clj :require #+cljs :require-macros [simplection.core :as cr])
  #+clj (:import [simplection.canvasgraph.ascale Category Numeric]))

(def aggregate-keys (hme/select-keys-rest (first series/stacked-table) definition/categories))

(defn apply-scaling
  [graph-values scale coordinates-range]
  (let [scale-record ((cr/scale-resolver) (definition/get-type scale))]
      (ascale/scale-values scale-record graph-values (definition/get-data scale) coordinates-range)))

(defn scale-data
  [table]
  (let [data-scaling (definition/get-data-scaling)
        axes-points []]
    (apply-scaling
      (apply-scaling [axes-points table]
                     (first data-scaling)
                     (first coordinates/coordinates-range))
     (second data-scaling)
     (second coordinates/coordinates-range))))

(def graph-values (scale-data series/stacked-table))
(def axes-points (first graph-values))
(def scaled-table (second graph-values))
