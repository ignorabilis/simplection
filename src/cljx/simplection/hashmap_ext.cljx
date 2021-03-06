(ns simplection.hashmap-ext
  (:require [clojure.set :as hs]))

(defn select-values
  "Select all values, returns nil if no values were found."
  [hm k-vec]
  (reverse (vals (select-keys hm k-vec))))

(defn select-values-empty
  "Select all values, always returns vector (empty if no values were found)."
  [map ks]
  (reduce #(conj %1 (map %2)) [] ks))

(defn select-values-no-nils
  "Select all values from a map without nil values."
  [hm ks]
  (remove nil? (reduce #(conj %1 (hm %2)) [] ks)))

(defn get-range-values
  "Get all non nil values in a collection of maps."
  [table aggregate-keys]
  (for [row table]
    (select-values-no-nils row aggregate-keys)))

(defn keys-by-value
  "Get keys by a given value."
  [hm v]
  (filter (comp #{v} hm) (keys hm)))

(defn select-keys-rest
  "Select all the keys not specified in the seq."
  [hm k-seq]
  (into '()
  (hs/difference (set (keys hm)) k-seq)))

(defn select-keys-only
  "Select only the keys specified in the seq."
  [hm k-seq]
  (keys (select-keys hm k-seq)))
