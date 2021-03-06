(ns simplection.report
  #+clj
  (:require [clojure.string :as string]
            [hiccup.core :as hic])
  #+cljs
  (:require [clojure.string :as string]
            [hiccups.runtime :as hic-runtime])
  #+cljs
  (:require-macros [hiccups.core :as hic]))

(defn geometry->svg
  "Convert geometry instructions to svg path"
  [geo]
  (for [path geo]
    [:path {:d (string/join " " path)}]))

(defn generate-item
  [item]
  (let [geometries (:coll-geometries item)
        name (:name item)]
    [:g {:id name} (geometry->svg geometries)]))

(defrecord Report [coll-report-items])
(defrecord ReportItem [coll-geometries name])

(defprotocol PReportGeneration
  (generate-report [this]))

(extend-protocol PReportGeneration
  Report
  (generate-report [{items :coll-report-items}]
    [:svg {:width "100%" :height "100%"}
     (for [item items]
       (generate-item item))]))

(defn hiccup->svg
  [svg]
  (hic/html svg))
