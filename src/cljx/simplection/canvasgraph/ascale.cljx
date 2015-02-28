(ns simplection.canvasgraph.ascale
  (:require [simplection.range :as ran]
            [simplection.hashmap-ext :as hme]
            [simplection.canvasgraph.series :as series]
            [simplection.canvasgraph.definition :as definition]))

(def aggregate-keys (hme/select-keys-rest (first series/stacked-table) definition/categories))

(defprotocol PScale
  (generate-coordinates [this table cr]))

(defrecord Category[])

(extend-protocol PScale
  Category
  (generate-coordinates [this table cr]
    (ran/table-range-dimensions table definition/categories cr)))

(defrecord Numeric[])

(extend-protocol PScale
  Numeric
  (generate-coordinates [this table cr]
    (ran/table-range-measures table aggregate-keys cr)))
