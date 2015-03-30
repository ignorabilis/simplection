(ns simplection.canvasgraph.path
  (:require [simplection.canvasgraph.apath :as apath]
            [simplection.canvasgraph.definition :as definition]
     #+cljs [simplection.canvasgraph.apath :refer [Straight]])
  (#+clj :require #+cljs :require-macros [simplection.core :as cr])
  #+clj (:import [simplection.canvasgraph.apath Straight]))

(defn generate-paths
  [table]
  (zipmap (keys table)
          (let [data-path-record ((cr/path-resolver) (definition/get-data-paths))]
            (for [[k v] table]
              (apath/generate-data-path data-path-record v)))))

(defn series-coordinates->data-paths
  [normalized-coordinates]
  (vector
   (first normalized-coordinates)
   (generate-paths (second normalized-coordinates))))
