(ns simplection.canvasgraph.definition
  (:require [simplection.hashmap-ext :as hme]
            [simplection.canvasgraph.aggregates :as aggs]))

(def default-graph-definition (atom {:extrapolation "to be implemented"
                                     :interpolation "to be implemented"
                                     :aggregate-rules {:category aggs/category-grouping :series aggs/series-grouping :y1 + :y2 +}
                                     :stack-rules {:type :stack :data nil #_[[:y1 '(:a)]
                                                                       [:y1 '(:b)]
                                                                       [:y1 '(:c)]
                                                                       [:y2 '(:a)]
                                                                       [:y2 '(:b)]
                                                                       [:y2 '(:c)]]}
                                     :coordinate-system {:type :polar}
                                     :data-scaling [{:type :category :data [[:category]]}
                                                    {:type :numeric :data [[:y1 '(:a)]
                                                                           [:y1 '(:b)]
                                                                           [:y1 '(:c)]
                                                                           [:y2 '(:a)]
                                                                           [:y2 '(:b)]
                                                                           [:y2 '(:c)]]}]
                                     :cluster-rules "to be implemented"
                                     :intersection :cross
                                     :data-paths [{:type :straight :data [[[:category] [:y1 '(:a)]]]}
                                                  {:type :straight :data [[[:category] [:y1 '(:b)]]]}
                                                  {:type :straight :data [[[:category] [:y1 '(:c)]]]}
                                                  {:type :straight :data [[[:category] [:y2 '(:a)]]]}
                                                  {:type :straight :data [[[:category] [:y2 '(:b)]]]}
                                                  {:type :straight :data [[[:category] [:y2 '(:c)]]]}]
                                     :data-markers "to be implemented"
                                     :data-areas "to be implemented"
                                     :data-mask "to be implemented"
                                     :data-styles {[[[:DC] [:DY2 '(:a)]]] {:path {:fill "none" :stroke "green" :stroke-width 0.01}
                                                                        :aux-path {:fill "none" :stroke "green" :stroke-width 0.01}
                                                                        :area {:fill "yellow" :stroke "none" :stroke-width 0}}
                                                  [[[:DC] [:DY1 '(:a)]]] {:path {:fill "none" :stroke "green" :stroke-width 0.01}
                                                                        :aux-path {:fill "none" :stroke "green" :stroke-width 0.01}
                                                                        :area {:fill "yellow" :stroke "none" :stroke-width 0}}
                                                  [[[:DC] [:DY1 '(:b)]]] {:path {:fill "none" :stroke "green" :stroke-width 0.01}
                                                                        :aux-path {:fill "none" :stroke "green" :stroke-width 0.01}
                                                                        :area {:fill "yellow" :stroke "none" :stroke-width 0}}
                                                  [[[:DC] [:DY2 '(:b)]]] {:path {:fill "none" :stroke "green" :stroke-width 0.01}
                                                                        :aux-path {:fill "none" :stroke "green" :stroke-width 0.01}
                                                                        :area {:fill "yellow" :stroke "none" :stroke-width 0}}}}))

(defn get-stroke-width
  "Get the appropriate stroke width according to the graph type."
  [stroke-width]
  ((get-type (get-coordinate-system))
   {:cartesian (/ stroke-width 2)
    :polar stroke-width}))


(defn default-style
  []
  [{:stroke "#74ACD1" :fill "none" :stroke-width (get-stroke-width 0.01)}
   {:stroke "#87AAB0" :fill "none" :stroke-width (get-stroke-width 0.01)}
   {:stroke "#FFAF70" :fill "none" :stroke-width (get-stroke-width 0.01)}
   {:stroke "#82C695" :fill "none" :stroke-width (get-stroke-width 0.01)}
   {:stroke "#CDE8BB" :fill "none" :stroke-width (get-stroke-width 0.01)}
   {:stroke "#8DD3DF" :fill "none" :stroke-width (get-stroke-width 0.01)}
   {:stroke "#DCC2C1" :fill "none" :stroke-width (get-stroke-width 0.01)}
   {:stroke "#B3B586" :fill "none" :stroke-width (get-stroke-width 0.01)}
   {:stroke "#C1E7F0" :fill "none" :stroke-width (get-stroke-width 0.01)}
   {:stroke "#FBD0E3" :fill "none" :stroke-width (get-stroke-width 0.01)}
   {:stroke "#B4D2CA" :fill "none" :stroke-width (get-stroke-width 0.01)}
   {:stroke "#CADCF0" :fill "none" :stroke-width (get-stroke-width 0.01)}
   {:stroke "#FFD3B5" :fill "none" :stroke-width (get-stroke-width 0.01)}
   {:stroke "#FDE69A" :fill "none" :stroke-width (get-stroke-width 0.01)}
   {:stroke "#C69B78" :fill "none" :stroke-width (get-stroke-width 0.01)}])

(defn default-axis-style
  []
  {:stroke "#999999" :fill "none" :stroke-width (get-stroke-width 0.003)})

(defn get-aggregate-rules
  "Get the aggregate rules for the graph."
  []
  (get-in @default-graph-definition [:aggregate-rules]))

(defn update-aggregate-rules!
  "Update the aggregate rules for the graph."
  [k ag]
  (swap! default-graph-definition update-in [:aggregate-rules] assoc k ag))

(defn remove-aggregate-rules!
  "Remove the aggregate rules for the graph."
  [k ag]
  (swap! default-graph-definition update-in [:aggregate-rules] dissoc k ag))

(defn get-data-scaling
  "Get the data scaling rules for the graph."
  []
  (get-in @default-graph-definition [:data-scaling]))

(defn get-coordinate-system
  "Get the coordinate system type for the graph."
  []
  (get-in @default-graph-definition [:coordinate-system]))

(defn get-intersection
  "Get the intersection algorithm for the scaled data."
  []
  (get-in @default-graph-definition [:intersection]))

(defn get-data-paths
  "Get the intersection algorithm for the scaled data."
  []
  (get-in @default-graph-definition [:data-paths]))

(defn get-type
  "Get the type that corresponds to a specific record."
  [hm]
  (hm :type))

(defn get-data
  "Get the data that corresponds to a specific record."
  [hm]
  (hm :data))

(defn get-categories
  []
  (hme/keys-by-value (get-aggregate-rules) aggs/category-grouping))

(defn get-series
  []
  (hme/keys-by-value (get-aggregate-rules) aggs/series-grouping))

(defn get-groupings
  []
  (concat (get-categories) (get-series)))

(defn get-aggregates
  []
  (hme/select-keys-rest (get-aggregate-rules) (get-groupings)))
