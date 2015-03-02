(ns simplection.canvasgraph.coordinates
  (:require [simplection.canvasgraph.definition :as definition]
            [simplection.canvasgraph.acoordinates :as acoordinates]
     #+cljs [simplection.canvasgraph.acoordinates :refer [Cartesian Polar]])
  (#+clj :require #+cljs :require-macros [simplection.core :as cr])
  #+clj (:import [simplection.canvasgraph.acoordinates Cartesian Polar]))

(def coordinates-range
  "0-1 for linear and 0-360 for angular for the first axis."
  [(acoordinates/get-coordinates-bounds ((cr/coordinates-resolver) (definition/get-type (definition/get-coordinate-system))))
   [0 1]])
