(ns simplection.canvasgraph.scale
  (:require [simplection.hashmap-ext :as hme]
            [simplection.canvasgraph.series :as series]
            [simplection.canvasgraph.definition :as definition]
            [simplection.canvasgraph.ascale :as ascale]
     #+cljs [simplection.canvasgraph.ascale :refer [Category Numeric]])
  (#+clj :require #+cljs :require-macros [simplection.core :as cr])
  #+clj (:import [simplection.canvasgraph.ascale Category Numeric]))

(def aggregate-keys (hme/select-keys-rest (first series/stacked-table) definition/categories))

(def coordinates-range
  "0-1 range is used then the resulting vector is scaled."
  [0 1])

(defn apply-scaling
  [table scale]
  (let [scale-record ((definition/get-type scale) (cr/scale-resolver))]
      (ascale/generate-coordinates scale-record table (definition/get-data scale) coordinates-range)))

(defn scale-data
  [table coordinates-range]
  (reduce apply-scaling table (definition/get-data-scaling)))

(def scaled-table (scale-data series/stacked-table coordinates-range))
