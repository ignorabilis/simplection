(ns simplection.canvasgraph.ascale
  (:require [simplection.range :as ran]
            [simplection.canvasgraph.axis :as axis]))

(defn range-dimensions
  "Converts categories to numeric range."
  [table coordinates-range]
  (let [categories-count (count table)
          categories-range [1 categories-count]
          offset-coordinates-range (ran/offset-range coordinates-range categories-count)]
      (ran/remap-values categories-range offset-coordinates-range (range 1 (inc categories-count)))))

(defn table-range-measures
  "Converts numeric values (measures) in a table to match a new range."
  [graph-values aggregate-keys new-range]
  (let [table (second graph-values)
        axis-min-max-step (axis/get-axis-min-max-step table aggregate-keys)
        original-range [(:axis-min axis-min-max-step) (:axis-max axis-min-max-step)]
        new-step (ran/remap-value original-range new-range (:step axis-min-max-step))
        axis-steps (range (first new-range) (+ (second new-range) new-step) new-step)]
    [(conj (first graph-values) axis-steps)
     (for [row table]
      (reduce #(assoc-in %1 [%2] (ran/remap-value original-range new-range (%1 %2))) row aggregate-keys))]))

(defn table-range-dimensions
  "Convert category values (dimensions) in a table to match a new range."
  [graph-values category-keys new-range]
  (let [cat-ks (first category-keys)
        table (second graph-values)
        new-dimensions (range-dimensions table new-range)]
    [(conj (first graph-values) new-dimensions)
     (map #(apply dissoc (assoc %1 cat-ks %2) cat-ks) table new-dimensions)]))

(defprotocol PScale
  (scale-values [this table ks cr]))

(defrecord Category[])
(defrecord Numeric[])

(extend-protocol PScale

  Category
  (scale-values [this graph-values ks cr]
    (table-range-dimensions graph-values ks cr))

  Numeric
  (scale-values [this graph-values ks cr]
    (table-range-measures graph-values ks cr)))
