(ns simplection.data-aggregation)

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

(merge-with-multiple-aggregates [{:DO "a" :DC "main" :DY1 120 :DY2 70}{:DO "a" :DC "gene" :DY1 40  :DY2 0}] {:DO distinct? :DC distinct? :DY1 + :DY2 +})

(aggregate-table {{:DO "a"}
                  [{:DO "a" :DC "main" :DY1 120 :DY2 70}
                   {:DO "a" :DC "gene" :DY1 40  :DY2 0}]
                  {:DO "b"}
                  [{:DO "b" :DC "gene" :DY1 40  :DY2 0}]}{:DO grouping :DC grouping :DY1 + :DY2 +})

(organize-table [{:DO "a" :DC "main" :DY1 120 :DY2 0}
                 {:DO "b" :DC "gene" :DY1 40  :DY2 40}
                 {:DO "b" :DC "main" :DY1 50  :DY2 80}
                 {:DO "b" :DC "gene" :DY1 10  :DY2 10}
                 {:DO "a" :DC "main" :DY1 90  :DY2 60}
                 {:DO "a" :DC "main" :DY1 100 :DY2 60}
                 {:DO "b" :DC "gene" :DY1 20  :DY2 60}
                 {:DO "a" :DC "main" :DY1 80  :DY2 30}
                 {:DO "a" :DC "main" :DY1 90  :DY2 90}
                 {:DO "a" :DC "main" :DY1 80  :DY2 20}] {:DY1 +})

(filter-table-columns [{:DO "a" :DC "main" :DY1 120 :DY2 0}
                       {:DO "b" :DC "gene" :DY1 40  :DY2 40}
                       {:DO "b" :DC "main" :DY1 50  :DY2 80}
                       {:DO "b" :DC "gene" :DY1 10  :DY2 10}
                       {:DO "a" :DC "main" :DY1 90  :DY2 60}
                       {:DO "a" :DC "main" :DY1 100 :DY2 60}
                       {:DO "b" :DC "gene" :DY1 20  :DY2 60}
                       {:DO "a" :DC "main" :DY1 80  :DY2 30}
                       {:DO "a" :DC "main" :DY1 90  :DY2 90}
                       {:DO "a" :DC "main" :DY1 80  :DY2 20}] (keys {:DO grouping :DY2 +}))

(group-table '({:DY2 0, :DO "a"}
                     {:DY2 40, :DO "b"}
                     {:DY2 80, :DO "b"}
                     {:DY2 10, :DO "b"}
                     {:DY2 60, :DO "a"}
                     {:DY2 60, :DO "a"}
                     {:DY2 60, :DO "b"}
                     {:DY2 30, :DO "a"} {:DY2 90, :DO "a"} {:DY2 20, :DO "a"}) '(:DO :DY2))

(aggregate-table {{:DO "a"}
                  [{:DY2 0, :DO "a"}
                   {:DY2 60, :DO "a"}
                   {:DY2 60, :DO "a"}
                   {:DY2 30, :DO "a"}
                   {:DY2 90, :DO "a"}
                   {:DY2 20, :DO "a"}],
                  {:DO "b"} [{:DY2 40, :DO "b"} {:DY2 80, :DO "b"} {:DY2 10, :DO "b"} {:DY2 60, :DO "b"}]}
                 { :DY2 + :DO grouping})
