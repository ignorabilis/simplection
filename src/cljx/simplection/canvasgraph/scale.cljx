(ns simplection.canvasgraph.scale
  (:require [simplection.hashmap-ext :as hme]
            [simplection.canvasgraph.series :as series]
            [simplection.canvasgraph.coordinates :as coordinates]
            [simplection.canvasgraph.definition :as definition]
            [simplection.canvasgraph.ascale :as ascale]
     #+cljs [simplection.canvasgraph.ascale :refer [Category Numeric]])
  (#+clj :require #+cljs :require-macros [simplection.core :as cr])
  #+clj (:import [simplection.canvasgraph.ascale Category Numeric]))

(defn apply-scaling
  [graph-values scale coordinates-range]
  (let [scale-record ((cr/scale-resolver) (definition/get-type scale))]
      (ascale/scale-values scale-record graph-values (definition/get-data scale) coordinates-range)))

(defn scale-values
  [table]
  (let [data-scaling (definition/get-data-scaling)
        coordinates-range (coordinates/get-coordinates-range)
        axes-points []]
    (apply-scaling
      (apply-scaling [axes-points table]
                     (first data-scaling)
                     (first coordinates-range))
     (second data-scaling)
     (second coordinates-range))))
