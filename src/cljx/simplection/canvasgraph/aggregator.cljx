(ns simplection.canvasgraph.aggregator
  (:require clojure.set))

(def table-to-organize (atom []))

(defn grouping
  "When grouping just take the first element since they are all the same."
  [args]
  (first args))

(defn series-grouping
  "Group by series"
  [& args]
  (grouping args))

(defn category-grouping
  "Group by category"
  [& args]
  (grouping args))

(defn keys-by-value
  "Get keys by a given value."
  [hm v]
  (filter (comp #{v} hm) (keys hm)))

(defn select-keys-rest
  "Select all the keys not specified in the seq."
  [hm k-seq]
  (into '()
  (clojure.set/difference (set (keys hm)) k-seq)))

(def organize-rules {:DC category-grouping :DO series-grouping :DY1 + :DY2 +})
(def categories (keys-by-value organize-rules category-grouping))
(def series (keys-by-value organize-rules series-grouping))
(def groupings (concat categories series))
(def aggregates (select-keys-rest organize-rules groupings))

(defn filter-table-columns
  "Filter the table by columns."
  [table column-names]
  (for [row table]
    (select-keys row column-names)))

(defn filter-table-rows
  "Filter the incoming data"
  [table filtering-fn]
  [])

(defn group-table
  "Group table by a chosen column combination."
  [table column-names]
  (group-by #(select-keys % column-names) table))

(defn merge-with-multiple-aggregates
  "Merges maps by using a map which specifies each column merge option."
  [table-section aggregates]
  (zipmap
   (keys aggregates)
   (for [[k v] aggregates]
     (apply (comp v) (map k table-section)))))

(defn aggregate-table
  "Aggregates the table."
  [table aggregates]
  (for [[k v] table]
    (merge-with-multiple-aggregates (seq v) aggregates)))

(defn organize-table
  "Group, aggregate, filter, sort, etc. the whole table"
  [table-to-organize organization-map]
  (aggregate-table
   (group-table
    (filter-table-columns table-to-organize (keys organization-map))
    groupings)
   organization-map))
