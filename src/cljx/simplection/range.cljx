(ns simplection.range
  (:require [simplection.hashmap-ext :as hme]))

(defn get-min-max
  "Get min and max as a vector."
  [table aggregate-keys]
  (let [data-range (flatten (hme/get-range-values table aggregate-keys))
        data-min (apply min data-range)
        data-max (apply max data-range)]
    [data-min data-max]))

(defn ranges-coefficient
  "Gets the coefficient of two ranges."
  [old-min old-max new-min new-max]
  (/ (- old-max old-min) (- new-max new-min)))

(defn remap-value
  "Converts the value from the old range to a value in the new range, using the coefficient k; ignores nil."
  [[old-min old-max] [new-min new-max] old-value]
  (let[k (ranges-coefficient old-min old-max new-min new-max)]
    (and old-value (- new-max (/ (- old-max old-value) k)))))

(defn remap-values
  "Converts the values from the old range to a value in the new range, using the coefficient k."
  [[old-min old-max] [new-min new-max] coll-old-values]
  (let[k (ranges-coefficient old-min old-max new-min new-max)]
    (map #(- new-max (/ (- old-max %) k)) coll-old-values)))

(defn offset-range
  "Converts data range to a new range with an offset."
  [[original-min original-max] categories-count]
  (let [step (/ original-max (* 2 categories-count))]
    [(+ original-min step) (- original-max step)]))

(defn range-dimensions
  "Converts categories to numeric range."
  [table coordinates-range]
  (let [categories-count (count table)
          categories-range [1 categories-count]
          offset-coordinates-range (offset-range coordinates-range categories-count)]
      (remap-values categories-range offset-coordinates-range (range 1 (inc categories-count)))))

(defn table-range-measures
  "Converts numeric values (measures) in a table to match a new range."
  [table aggregate-keys new-range]
  (let [original-range (get-min-max table aggregate-keys)]
    (for [row table]
      (reduce #(assoc-in %1 [%2] (remap-value original-range new-range (%1 %2))) row aggregate-keys))))

(defn table-range-dimensions
  "Convert category values (dimensions) in a table to match a new range."
  [table category-keys new-range]
  (let [cat-ks (first category-keys)]
    (map #(apply dissoc (assoc %1 cat-ks %2) cat-ks) table (range-dimensions table new-range))))
