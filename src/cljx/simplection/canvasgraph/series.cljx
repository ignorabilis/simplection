(ns simplection.canvasgraph.series
  (:require [clojure.set :as hs]
            [simplection.canvasgraph.aggregator :as aggregator]
            [simplection.canvasgraph.definition :as definition]
            [simplection.hashmap-ext :as hme]))

(defn anything->keyword
  "Convert anything to keyword, including numbers and seqs."
  [anything]
  (keyword (str anything)))

(defn keywords
  "Convert a list of values to keywords."
  [coll]
  (map anything->keyword coll))

(defn generate-key-map
  "Creates the hash-map used for renaming keys."
  [row series aggregates]
  (into {}
        (reduce #(conj %1 [%2 [%2 (keywords (hme/select-values row series))]]) [] aggregates)))

(defn stack-keys
  "Create new vector keys for each aggregate from the existing keys and all dynamic series.
  Example - aggregated values for column :y with dynamic series 'category' and 'subcategory' -> [:y (:category :subcategory)]"
  [table-to-stack series aggregates]
  (for [row table-to-stack]
    (apply dissoc
      (hs/rename-keys row (generate-key-map row series aggregates))
           series)))

(defn get-table-stacked-keys
  [aggregated-table]
  (stack-keys aggregated-table (definition/get-series) (definition/get-aggregates)))

(defn dynamic->static-series
  "Convert all the dynamic (row) series to static (column) series."
  [aggregated-table]
   (for [[k v] (aggregator/group-table (get-table-stacked-keys aggregated-table) (definition/get-categories))]
    {k (apply merge v)}))

(defn contains-value?
  [coll element]
  (some #(= element %) coll))

(defn get-keys
  [static-series]
  (keys (first (vals (first static-series)))))

(defn get-default-series
  [static-series]
  (filter #(not (contains-value? (definition/get-categories) %)) (get-keys static-series)))

(defn get-default-categories
  [static-series]
  (filter #(contains-value? (definition/get-categories) %) (get-keys static-series)))

(def default-series (atom []))
(def default-categories (atom []))

(defn get-static-series
  [aggregated-table]
  (let [static-series (dynamic->static-series aggregated-table)]
    (do
      (reset! default-series (get-default-series static-series))
      (reset! default-categories (get-default-categories static-series))
      static-series)))
