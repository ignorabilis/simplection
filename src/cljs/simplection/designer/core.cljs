(ns simplection.designer.core
  (:require [reagent.core :refer [atom]]
            [jayq.core :as $]
            [simplection.canvasgraph.coordinates :as cs]
            [simplection.canvasgraph.path :as p]
            [simplection.report :as rep]
            [simplection.canvasgraph.aggregator :as agg]
            [simplection.canvasgraph.aggregates :as ags]
            [simplection.canvasgraph.data :as g-data]
            [simplection.canvasgraph.series :as series]
            [simplection.range :as ran]
            [simplection.canvasgraph.scale :as scale]
            [simplection.canvasgraph.intersection :as inter]
            [simplection.canvasgraph.csnormalization :as norm]
            [simplection.rendering :as rendering]))

(def search-term (atom ""))

(def measures (atom [{:displayValue "X1" :value "X1"}
                     {:displayValue "X2" :value "X2"}
                     {:displayValue "Y1" :value "Y1"}
                     {:displayValue "Y2" :value "Y2"}]))

(def dimensions (atom [{:displayValue "D1" :value "D1"}
                       {:displayValue "D2" :value "D2"}
                       {:displayValue "CD1" :value "CD1"}
                       {:displayValue "CD2" :value "CD2"}]))

(def chart-types (atom #{"Gan", "Pie", "Bar", "Lin", "Sca", "Fun"}))
(def aggregates (atom #{"SUM" "AVG" "MIN" "MAX" "MEAN" "COUNT"}))

(def selected-chart-type (atom ""))
(def selected-rows (atom #{}))
(def selected-columns (atom #{}))



;; TESTING!
(reset! g-data/data-source [{:series "a" :category "me"     :y1 120 :y2 00}
                            {:series "b" :category "me"     :y1 40  :y2 40}
                            {:series "c" :category "me"     :y1 50  :y2 80}
                            {:series "a" :category "we"     :y1 10  :y2 10}
                            {:series "b" :category "we"     :y1 90  :y2 60}
                            {:series "c" :category "we"     :y1 100 :y2 60}
                            {:series "a" :category "he"     :y1 20  :y2 60}
                            {:series "b" :category "he"     :y1 80  :y2 30}
                            {:series "c" :category "he"     :y1 90  :y2 90}
                            {:series "a" :category "she"    :y1 80  :y2 20}
                            {:series "b" :category "she"    :y1 120 :y2 00}
                            {:series "c" :category "she"    :y1 40  :y2 40}
                            {:series "a" :category "they"   :y1 50  :y2 80}
                            {:series "b" :category "they"   :y1 10  :y2 10}
                            {:series "c" :category "they"   :y1 90  :y2 60}
                            {:series "a" :category "you"    :y1 100 :y2 60}
                            {:series "b" :category "you"    :y1 20  :y2 60}
                            {:series "c" :category "you"    :y1 80  :y2 30}
                            {:series "a" :category "others" :y1 90  :y2 90}
                            {:series "b" :category "others" :y1 80  :y2 20}
                            {:series "c" :category "ninjas" :y1 100 :y2 60}
                            {:series "a" :category "ninjas" :y1 20  :y2 60}])

(def graph (atom rendering/paths))
