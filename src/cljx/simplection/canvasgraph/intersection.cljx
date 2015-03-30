(ns simplection.canvasgraph.intersection
  (:require [simplection.canvasgraph.definition :as definition]
            [simplection.canvasgraph.acoordinates :as acoordinates]
            [simplection.canvasgraph.scale :as scale]
            [simplection.canvasgraph.series :as series]
            [simplection.canvasgraph.acoordinates :as acoordinates]
     #+cljs [simplection.canvasgraph.acoordinates :refer [Cartesian Polar]])
  (#+clj :require #+cljs :require-macros [simplection.core :as cr])
  #+clj (:import [simplection.canvasgraph.acoordinates Cartesian Polar]))

(defn cross
  ([] '(()))
  ([xs & more]
    (mapcat #(map (partial cons %)
                  (apply cross more))
            xs)))

(def intersection-fns {:cross cross :interleave interleave})

(defn intersect
  []
  (let [intersect-fn (intersection-fns (definition/get-intersection))
        s-1 (map vector @series/default-categories) ; to do - remove hardcoded values
        s-2 @series/default-series] ; to do - remove hardcoded values
    (intersect-fn s-1 s-2)))

(defn in?
  "True if a collection contains element."
  [seq element]
  (some #(= element %) seq))

(defn transpose-table [table]
  (let [ks (keys (first table))]
       (into {} (for [k ks] [k (map #(% k) table)]))))

(defn intersect-values
  [table]
  (let [transposed-table (transpose-table table)
        intersection-rules (intersect)]
    (into {}
      (for [rule intersection-rules]
         [rule
          (filter #(not (in? % nil))
                  (map vector (transposed-table (first rule)) (transposed-table (last rule))))]))))

(defn table->series-coordinates
  [scaled-values]
  (let [coordinate-system ((cr/coordinates-resolver) (definition/get-type (definition/get-coordinate-system)))]
  (vector
    (acoordinates/generate-grid coordinate-system (first scaled-values))
    (intersect-values (second scaled-values)))))
