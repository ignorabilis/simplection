(ns simplection.canvasgraph.csnormalization
  (:require [simplection.canvasgraph.definition :as definition]
            [simplection.canvasgraph.intersection :as intersection]
            [simplection.canvasgraph.acoordinates :as acoordinates]
     #+cljs [simplection.canvasgraph.acoordinates :refer [Cartesian Polar]])
  (#+clj :require #+cljs :require-macros [simplection.core :as cr])
  #+clj (:import [simplection.canvasgraph.acoordinates Cartesian Polar]))

(def series-coordinates
  (acoordinates/normalize-coordinates
   ((cr/coordinates-resolver) (definition/get-type (definition/get-coordinate-system)))
   intersection/series-coordinates))
