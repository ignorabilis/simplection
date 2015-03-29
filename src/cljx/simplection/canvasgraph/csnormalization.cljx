(ns simplection.canvasgraph.csnormalization
  (:require [simplection.canvasgraph.definition :as definition]
            [simplection.canvasgraph.intersection :as intersection]
            [simplection.canvasgraph.scale :as scale]
            [simplection.canvasgraph.acoordinates :as acoordinates]
     #+cljs [simplection.canvasgraph.acoordinates :refer [Cartesian Polar]])
  (#+clj :require #+cljs :require-macros [simplection.core :as cr])
  #+clj (:import [simplection.canvasgraph.acoordinates Cartesian Polar]))

(defn normalize-grid-coordinates
  [grid-coordinates]
  (let [coordinate-system ((cr/coordinates-resolver) (definition/get-type (definition/get-coordinate-system)))]
    (acoordinates/normalize-grid-coordinates coordinate-system grid-coordinates)))

(defn normalize-series-coordinates
  [series-coordinates]
  (acoordinates/normalize-table-coordinates
   ((cr/coordinates-resolver) (definition/get-type (definition/get-coordinate-system)))
   series-coordinates))

(defn normalize-coordinates
  [series-coordinates]
  (vector
   (normalize-grid-coordinates (first series-coordinates))
   (normalize-series-coordinates (second series-coordinates))))
