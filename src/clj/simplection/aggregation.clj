(ns simplection.aggregation)

(defn grouping
  "Group by categories"
  [& args]
  (first (vec args)))

(defn keys-by-value
  [hm v]
  (filter (comp #{v} hm) (keys hm)))

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
  ""
  [table-section aggregates]
  (zipmap
   (keys aggregates)
   (for [[k v] aggregates]
     (eval (conj (map k table-section) v)))))

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
    (keys-by-value organization-map grouping))
   organization-map))