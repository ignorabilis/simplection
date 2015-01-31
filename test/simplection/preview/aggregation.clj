(ns simplection.preview.aggregation
  (:require [simplection.aggregation :refer :all]))

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
