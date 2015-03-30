(ns simplection.canvasgraph.aggregator
  (:require [simplection.canvasgraph.definition :as definition]
            [simplection.canvasgraph.data :as g-data]))

(defn filter-table-columns
  "Filter the table by columns."
  [table column-names]
  (for [row table]
    (select-keys row column-names)))

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

(defn merge-table
  "Aggregates the table."
  [table aggregates]
  (for [[k v] table]
    (merge-with-multiple-aggregates (seq v) aggregates)))

(defn aggregate-table
  "Group, aggregate, filter, sort, etc. the whole table."
  [table-to-aggregate]
  (let [aggregate-rules (definition/get-aggregate-rules)]
    (merge-table
     (group-table
      (filter-table-columns table-to-aggregate (keys aggregate-rules))
      (definition/get-groupings))
     aggregate-rules)))
