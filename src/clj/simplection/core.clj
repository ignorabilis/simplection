(ns simplection.core
  (:require [clojure.string :as string]
            [simplection.canvasgraph.coordinate-system :as cs]))

(defn record-mapper
  [hm record-class]
  (let [class-map (bean record-class)
        full-name (class-map :canonicalName)
        r-name (class-map :simpleName)
        r-ns (string/replace full-name (str "." r-name) "")]
    (conj hm [(keyword r-name) `(~(symbol r-ns (str "->" r-name)))])))

(defn record-factory
  [proto]
  (reduce record-mapper {} (extenders proto)))

(defmacro cs-resolver
  []
  (record-factory cs/PCoordinateSystem))
