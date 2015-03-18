(ns simplection.rendering
  (:require [clojure.string :as string]
            [simplection.canvasgraph.path :as path]
            [simplection.canvasgraph.definition :as definition]))

(defn generate-path
  [value style]
  [:path {:d (string/join " " (second value)) :fill (:fill style) :stroke (:stroke style) :stroke-width (:stroke-width style)}])

(defn geometry->svg
  "Convert geometry instructions to svg path"
  [table]
  (map generate-path table definition/default-style))

(defn generate-graph
  [paths]
  [:svg {:width "100%" :height "100%"}
   [:g {:transform " translate(450, 300) scale(400)"}
    [:g
     paths]]])

(def paths (generate-graph (geometry->svg path/data-paths)))
