(ns simplection.rendering
  (:require [clojure.string :as string]
            [simplection.canvasgraph.core :as graph-core]
            [simplection.canvasgraph.defaults :as defaults]
            [simplection.canvasgraph.definition :as definition]))

(defn map-every-nth [f coll n]
  (map-indexed #(if (zero? (mod (inc %1) n)) (f %2) %2) coll))

(defn generate-geometries
  [value area-style aux-path-style path-style]
  [:g
   [:path {:d (string/join " " (map-every-nth #(- %) (:area (second value)) 3))
           :fill (:fill area-style) :stroke (:stroke area-style) :fill-opacity (:fill-opacity area-style)}]

   [:path {:d (string/join " " (map-every-nth #(- %) (:aux-path (second value)) 3))
           :fill (:fill aux-path-style) :stroke (:stroke aux-path-style) :stroke-width (:stroke-width aux-path-style)}]

   [:path {:d (string/join " " (map-every-nth #(- %) (:path (second value)) 3))
           :fill (:fill path-style) :stroke (:stroke path-style) :stroke-width (:stroke-width path-style)}]])

(defn area-geometry->svg
  "Convert path geometry instructions to svg path"
  [table]
  (map generate-area table (definition/default-area-style)))

(defn aux-paths-geometry->svg
  "Convert path geometry instructions to svg path"
  [table]
  (map generate-aux-path table (definition/default-aux-path-style)))

(defn paths-geometry->svg
  "Convert path geometry instructions to svg path"
  [table]
  (map generate-path table (definition/default-path-style)))

(defn geometries->svg
  "Convert path geometry instructions to svg path"
  [table]
  (map generate-geometries table (definition/default-area-style) (definition/default-aux-path-style) (definition/default-path-style)))

(defn render-circle
  [value style]
  (let [c-args (rest value)]
    [:circle {:cx (first c-args) :cy (second c-args) :r (last c-args) :fill (:fill style) :stroke (:stroke style) :stroke-width (:stroke-width style)}]))

(defn render-path
  [value style]
  (let [points (rest value)
        inverted-points (map #(vector (first %) (- (second %))) points)]
  [:path {:d (str "M " (string/join " " (first inverted-points)) " L " (string/join " L " (map (partial string/join " ") inverted-points)))
          :fill (:fill style) :stroke (:stroke style) :stroke-width (:stroke-width style)}]))

(defn grid-geometry->svg
  "Convert grid geometry instructions to svg."
  [grid-elements]
  (for [el grid-elements]
    (if (= :path (first el))
      (render-path el (definition/default-axis-style))
      (render-circle el (definition/default-axis-style)))))

(def transformation-map {:polar "translate(450, 350) scale(320)" :cartesian "translate(50, 700) scale(640)"})

(defn render-default-graph
  []
  ((definition/get-type (definition/get-coordinate-system))
   {:cartesian defaults/cartesian-graph
    :polar defaults/polar-graph}))

(defn render-graph
  ([]
   (render-default-graph))

  ([grid geometries]
    [:svg {:width "100%" :height "100%"}
     [:g {:transform ((definition/get-type (definition/get-coordinate-system)) transformation-map)}
      [:g
       grid]
      [:g
       geometries]]]))

(defn render
  [data-source]
  (if (definition/is-definition-valid)
    (let [graph-elements (graph-core/process-graph data-source)]
     #_graph-elements (render-graph
                       (grid-geometry->svg (first graph-elements))
                       (geometries->svg (second graph-elements))))
    (render-default-graph)))


