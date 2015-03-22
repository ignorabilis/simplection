(ns simplection.designer.core
  (:require [reagent.core :refer [atom]]
            [jayq.core :as $]
            [simplection.templates.layout :as layout :refer [layout]]
            [cljs.core.async :refer [<! >! chan close! sliding-buffer put! alts!]]
            [simplection.canvasgraph.coordinates :as cs]
            [simplection.canvasgraph.path :as p]
            [simplection.report :as rep]
            [simplection.canvasgraph.aggregator :as agg]
            [simplection.canvasgraph.aggregates :as aggs]
            [simplection.canvasgraph.data :as g-data]
            [simplection.canvasgraph.definition :as definition]
            [simplection.canvasgraph.series :as series]
            [simplection.range :as ran]
            [simplection.canvasgraph.scale :as scale]
            [simplection.canvasgraph.intersection :as inter]
            [simplection.canvasgraph.csnormalization :as norm]
            [simplection.rendering :as rendering])
  (:require-macros [cljs.core.async.macros :as m :refer [go alt!]]))

;; TESTING!
;OLD
#_(reset! g-data/data-source [{:series "a" :category "me"     :y1 120 :y2 00}
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

(reset! g-data/data-source [{:series "a" :category "1"     :y1 40  :y2 50}
                            {:series "a" :category "2"     :y1 20  :y2 70}
                            {:series "a" :category "3"     :y1 40  :y2 60}
                            {:series "a" :category "4"     :y1 70  :y2 90}
                            {:series "a" :category "5"     :y1 20  :y2 60}
                            {:series "a" :category "6"     :y1 10  :y2 30}
                            {:series "b" :category "1"     :y1 100 :y2 120}
                            {:series "b" :category "2"     :y1 90  :y2 140}
                            {:series "b" :category "3"     :y1 70  :y2 90}
                            {:series "b" :category "4"     :y1 50  :y2 110}
                            {:series "b" :category "5"     :y1 110 :y2 90}
                            {:series "b" :category "6"     :y1 80  :y2 90}
                            {:series "c" :category "1"     :y1 150 :y2 00}
                            {:series "c" :category "2"     :y1 150  :y2 160}
                            {:series "c" :category "3"     :y1 140  :y2 160}
                            {:series "c" :category "4"     :y1 130  :y2 160}
                            {:series "c" :category "5"     :y1 120  :y2 190}
                            {:series "c" :category "6"     :y1 110  :y2 200}])

(def search-term (atom ""))

(defn get-measures[data]
  (let [filtered (filter #(not (number? (val %))) data)]
    (into [](map (fn [i] {:value (name (first i)) :displayValue (name (first i))}) filtered))))


(defn get-dimensions[data]
  (let [filtered (filter #(number? (val %)) data)]
    (into [](map (fn [i] {:value (name (first i)) :displayValue (name (first i))}) filtered))))

(def measures (atom (get-measures (first @g-data/data-source))))

(def dimensions (atom (get-dimensions (first @g-data/data-source))))

(def chart-types (atom #{"Gan", "Pie", "Bar", "Lin", "Sca", "Fun"}))
(def aggregates (atom #{"SUM" "AVG" "MIN" "MAX" "MEAN" "COUNT" "CATEGORY"}))

(def selected-chart-type (atom ""))
(def selected-rows (atom #{}))
(def selected-columns (atom #{}))
(def selected-groupings (atom #{}))

(def input-ch (chan))

(def output-ch (chan))

(go (while true
  (let [{t :type b :body} (<! input-ch)]
    (if (= t :notification)
      (layout/show-notification (:msg b) (:type b))))))

(def agg-to-str-map {"SUM" +
                     "CATEGORY" aggs/category-grouping
                     "SERIES" aggs/series-grouping
                     :default +})

(defn get-aggregate-func [agg-str]
  (if (contains? agg-to-str-map agg-str)
    (agg-to-str-map agg-str)
    (agg-to-str-map :default)))

(defn get-aggregate-rules-recursive [map]
  (if (empty? map) {}
    (assoc (get-aggregate-rules-recursive (rest map))
      (keyword (:value (first map)))
      (get-aggregate-func (:aggregate (first map)))
      )))

(defn get-aggregate-rules [] 
  (get-aggregate-rules-recursive @selected-columns))

;:aggregate-rules {:category aggs/category-grouping :series aggs/series-grouping :y1 + :y2 +}

(def graph (atom rendering/paths))

(defn refresh-definition []
    (swap! definition/default-graph-definition merge { :aggregate-rules (get-aggregate-rules)}))

(add-watch selected-columns
           :columns-watcher
           (fn [_ _ _ _] (refresh-definition)))
             