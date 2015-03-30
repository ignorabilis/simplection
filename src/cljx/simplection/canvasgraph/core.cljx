(ns simplection.canvasgraph.core
  (:require [clojure.string :as string]
            [simplection.canvasgraph.aggregator :as aggregator]
            [simplection.canvasgraph.series :as series]
            [simplection.canvasgraph.stack :as stack]
            [simplection.canvasgraph.scale :as scale]
            [simplection.canvasgraph.intersection :as intersection]
            [simplection.canvasgraph.csnormalization :as csnormalization]
            [simplection.canvasgraph.path :as path]
            [simplection.canvasgraph.area :as area]))

(defn process-graph
  [data-source]
  (area/data-paths->areas
   (path/series-coordinates->data-paths
    (csnormalization/normalize-coordinates
     (intersection/table->series-coordinates
      (scale/scale-values
       (stack/stack-series
        (series/get-static-series
         (aggregator/aggregate-table data-source)))))))))
