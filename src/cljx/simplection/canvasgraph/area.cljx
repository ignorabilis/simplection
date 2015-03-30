(ns simplection.canvasgraph.area
  (:require [simplection.canvasgraph.aarea :as aarea]
            [simplection.canvasgraph.definition :as definition]
     #+cljs [simplection.canvasgraph.aarea :refer [None Shape ShapeArea]])
  (#+clj :require #+cljs :require-macros [simplection.core :as cr])
  #+clj (:import [simplection.canvasgraph.aarea None Shape ShapeArea]))

(defn generate-areas
  [data-paths]
       (zipmap (keys data-paths)
               (let [area-record ((cr/area-resolver) (definition/get-areas))]
                 (for [[k v] data-paths]
                   (aarea/generate-area area-record
                                        (aarea/generate-auxiliary-paths area-record v))))))

(defn data-paths->areas
  [paths]
  (let [grid (first paths)
        data-paths (second paths)]
      (vector
       grid
       (generate-areas data-paths))))
