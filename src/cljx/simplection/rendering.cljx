(ns simplection.rendering
  (:require [clojure.string :as string]
            [simplection.canvasgraph.core :as graph-core]
            [simplection.canvasgraph.defaults :as defaults]
            [simplection.canvasgraph.definition :as definition]))

(defn generate-path
  [value style]
  [:path {:d (string/join " " (second value)) :fill (:fill style) :stroke (:stroke style) :stroke-width (:stroke-width style)}])

(defn path-geometry->svg
  "Convert path geometry instructions to svg path"
  [table]
  (map generate-path table (definition/default-style)))

(defn render-circle
  [value style]
  (let [c-args (rest value)]
    [:circle {:cx (first c-args) :cy (second c-args) :r (last c-args) :fill (:fill style) :stroke (:stroke style) :stroke-width (:stroke-width style)}]))

(defn render-path
  [value style]
  [:path {:d (str "M " (string/join " " (second value)) " L " (string/join " L " (map (partial string/join " ") (rest value))))
          :fill (:fill style) :stroke (:stroke style) :stroke-width (:stroke-width style)}])

(defn grid-geometry->svg
  "Convert grid geometry instructions to svg."
  [grid-elements]
  (for [el grid-elements]
    (if (= :path (first el))
      (render-path el (definition/default-axis-style))
      (render-circle el (definition/default-axis-style)))))

(def transformation-map {:polar "translate(450, 350) scale(320)" :cartesian "translate(50, 50) scale(640)"})

(defn render-default-graph
  []
  ((definition/get-type (definition/get-coordinate-system))
   {:cartesian defaults/cartesian-graph
    :polar defaults/polar-graph}))

(defn render-graph
  ([]
   (render-default-graph))

  ([paths grid]
    [:svg {:width "100%" :height "100%"}
     [:g {:transform ((definition/get-type (definition/get-coordinate-system)) transformation-map)}
      [:g
       grid]
      [:g
       paths]]]))

(defn render
  [data-source]
  (if (definition/is-definition-valid)
    (let [graph-elements (graph-core/process-graph data-source)]
     #_graph-elements (render-graph (path-geometry->svg (second graph-elements)) (grid-geometry->svg (first graph-elements))))
    (render-default-graph)))


