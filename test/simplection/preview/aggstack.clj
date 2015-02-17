(ns simplection.preview.aggstack
  (:require [simplection.canvasgraph.aggregator :refer :all]
            [simplection.canvasgraph.series-orderer :refer :all]))

;; Aggregate values for each group
;; a main 560 260
;; b gene 70 110
;; b main 50 80
(reset! table-to-organize [{:DO "a" :DC "main" :DY1 120 :DY2 0}
                           {:DO "b" :DC "gene" :DY1 40  :DY2 40}
                           {:DO "b" :DC "main" :DY1 50  :DY2 80}
                           {:DO "b" :DC "gene" :DY1 10  :DY2 10}
                           {:DO "a" :DC "main" :DY1 90  :DY2 60}
                           {:DO "a" :DC "main" :DY1 100 :DY2 60}
                           {:DO "b" :DC "gene" :DY1 20  :DY2 60}
                           {:DO "a" :DC "main" :DY1 80  :DY2 30}
                           {:DO "a" :DC "main" :DY1 90  :DY2 90}
                           {:DO "a" :DC "main" :DY1 80  :DY2 20}])

#_(def stack-rules '([[:DY1 (:b)][:DY1 (:a)]][[:DY1 (:b)][:DY2 (:a)]]))

(stack-table static-series stack-rules)
