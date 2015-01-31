(ns simplection.test.aggregation
  (:require [midje.sweet :refer :all]
            [simplection.aggregation :refer :all]))

(def canvas-graph-definition-old {:aggregates [{:aggregate "category-group" :data [0]}
                                           {:aggregate "series-group" :data [1]}
                                           {:aggregate "+" :data [4]}
                                           {:aggregate "average" :data [5]}]})

(def canvas-graph-definition-new {:DO "category-group" :DC "series-group" :DY1 + :DY2 +})

(def test-table [{:DO "a"   :DC "main"   :DSC "main-a"   :DSSC "main-a-1"   :DX1 10    :DX2 90    :DY1 120   :DY2 70}
                 {:DO "b"   :DC "gene"   :DSC "gene-b"   :DSSC "gene-b-1"   :DX1 50    :DX2 100   :DY1 40    :DY2 80}
                 {:DO "c"   :DC "gene"   :DSC "gene-c"   :DSSC "gene-c-1"   :DX1 20    :DX2 30    :DY1 60    :DY2 20}
                 {:DO "a"   :DC "gene"   :DSC "gene-d"   :DSSC "gene-d-1"   :DX1 40    :DX2 20    :DY1 40    :DY2 0}
                 {:DO "c"   :DC "main"   :DSC "main-b"   :DSSC "main-b-1"   :DX1 60    :DX2 80    :DY1 110   :DY2 50}
                 {:DO "a"   :DC "main"   :DSC "main-c"   :DSSC "main-c-1"   :DX1 100   :DX2 60    :DY1 40    :DY2 20}
                 {:DO "a"   :DC "gene"   :DSC "gene-d"   :DSSC "gene-d-1"   :DX1 10    :DX2 40    :DY1 110   :DY2 80}
                 {:DO "b"   :DC "gene"   :DSC "gene-e"   :DSSC "gene-e-1"   :DX1 70    :DX2 120   :DY1 10    :DY2 20}
                 {:DO "c"   :DC "gene"   :DSC "gene-b"   :DSSC "gene-b-1"   :DX1 20    :DX2 10    :DY1 0     :DY2 90}])

(facts "Filtering should filter only the columns needed by key"
       (filter-table-columns [{}] [:DO]) => [{}]

       (filter-table-columns [{:DO "a"}] [:DO]) => [{:DO "a"}]

       (filter-table-columns [{:DO "a" :DC "main" :DY1 120 :DY2 70}] [:DO]) => [{:DO "a"}]

       (filter-table-columns [{:DO "a" :DC "main" :DY1 120 :DY2 70}
                      {:DO "b" :DC "gene" :DY1 40  :DY2 80}] [:DO]) => [{:DO "a"}
                                                                       {:DO "b"}]

       (filter-table-columns [{:DO "a" :DC "main" :DY1 120 :DY2 70}
                      {:DO "b" :DC "gene" :DY1 40  :DY2 80}] [:DO :DY2]) => [{:DO "a" :DY2 70}
                                                                             {:DO "b" :DY2 80}])

(facts "Grouped table"
       (group-table [{}] [:DO]) => {{} [{}]}

       (group-table [{:DO "a"}] [:DO]) => {{:DO "a"} [{:DO "a"}]}

       (group-table [{:DO "a" :DC "main" :DY1 120 :DY2 70}] [:DO]) => {{:DO "a"} [{:DO "a" :DC "main" :DY1 120 :DY2 70}]}

       (group-table [{:DO "a" :DC "main" :DY1 120 :DY2 70}
                     {:DO "b" :DC "gene" :DY1 40  :DY2 80}] [:DO]) => {{:DO "a"} [{:DO "a" :DC "main" :DY1 120 :DY2 70}]
                                                                       {:DO "b"} [{:DO "b" :DC "gene" :DY1 40  :DY2 80}]}

       (group-table [{:DO "a" :DC "main" :DY1 120 :DY2 70}
                     {:DO "b" :DC "gene" :DY1 40  :DY2 80}
                     {:DO "c" :DC "gene" :DY1 60  :DY2 20}] [:DO]) => {{:DO "a"} [{:DO "a" :DC "main" :DY1 120 :DY2 70}]
                                                                       {:DO "b"} [{:DO "b" :DC "gene" :DY1 40  :DY2 80}]
                                                                       {:DO "c"} [{:DO "c" :DC "gene" :DY1 60  :DY2 20}]}

       (group-table [{:DO "a" :DC "main" :DY1 120 :DY2 70}
                     {:DO "b" :DC "gene" :DY1 40  :DY2 80}
                     {:DO "a" :DC "gene" :DY1 40  :DY2 0}] [:DO]) => {{:DO "a"} [{:DO "a" :DC "main" :DY1 120 :DY2 70}
                                                                                 {:DO "a" :DC "gene" :DY1 40  :DY2 0}]
                                                                      {:DO "b"} [{:DO "b" :DC "gene" :DY1 40  :DY2 80}]}

       (group-table [{:DO "a" :DC "main" :DY1 120 :DY2 70}
                     {:DO "b" :DC "gene" :DY1 40  :DY2 80}
                     {:DO "a" :DC "gene" :DY1 40  :DY2 0}] [:DO :DC]) => {{:DO "a" :DC "main"} [{:DO "a" :DC "main" :DY1 120 :DY2 70}]
                                                                          {:DO "a" :DC "gene"} [{:DO "a" :DC "gene" :DY1 40  :DY2 0}]
                                                                          {:DO "b" :DC "gene"} [{:DO "b" :DC "gene" :DY1 40  :DY2 80}]})

(facts "Merge table"
       (merge-with-multiple-aggregates '({:DO "a" :DC "main" :DY1 120 :DY2 70}
                                         {:DO "a" :DC "gene" :DY1 40  :DY2 0})
                                       {:DO distinct? :DC distinct? :DY1 + :DY2 +}) => {:DY2 70, :DY1 160, :DC true, :DO false})

(facts "Aggregated table"
       (aggregate-table [{}] [distinct?]) => [{}])
