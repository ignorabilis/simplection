(ns simplection.canvasgraph.series-orderer
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

(def keys-stacked-table (stack-keys aggregator/aggregated-table definition/series definition/aggregates))

(defn dynamic->static-series
  "Convert all the dynamic (row) series to static (column) series."
  [keys-stacked-table categories]
   (for [[k v] (aggregator/group-table keys-stacked-table definition/categories)]
    {k (apply merge v)}))

(def static-series (dynamic->static-series keys-stacked-table definition/categories))

(defn default-stack-rules
  "By default each series is not stacked."
  [static-series]
  (map vector
    (distinct
      (for [[k v] (apply merge static-series)
            [k-2 v-2] v]
        k-2))))

(def stack-rules (default-stack-rules static-series))

(def +-nil (fnil + 0 0))

(defn stack-coll [coll]
  (map #(and %1 %2)
    coll
    (reduce #(conj %1 (+-nil (last %1) %2)) [(first coll)] (rest coll))))

(defn stack-multi [hm stack-rules]
  (for [rule stack-rules]
    (stack-coll (hme/select-values-empty hm rule))))

(defn red-concat [coll]
  (reduce concat coll))

(defn stack [hm stack-rules]
  (zipmap
   (red-concat stack-rules)
   (red-concat (stack-multi hm stack-rules))))

(defn stack-table [table stack-rules]
  (for [row table
        [k v] row]
    (merge (stack v stack-rules) k)))

(def stacked-table (stack-table static-series stack-rules))
