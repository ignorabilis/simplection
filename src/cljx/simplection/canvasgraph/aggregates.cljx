(ns simplection.canvasgraph.aggregates)

(defn grouping
  "When grouping just take the first element since they are all the same."
  [args]
  (first args))

(defn series-grouping
  "Group by series."
  [& args]
  (grouping args))

(defn category-grouping
  "Group by category."
  [& args]
  (grouping args))
