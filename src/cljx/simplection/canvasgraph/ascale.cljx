(ns simplection.canvasgraph.ascale
  (:require [simplection.range :as ran]))

(defprotocol PScale
  (scale-values [this table ks cr]))

(defrecord Category[])
(defrecord Numeric[])

(extend-protocol PScale

  Category
  (scale-values [this table ks cr]
    (ran/table-range-dimensions table ks cr))

  Numeric
  (scale-values [this table ks cr]
    (ran/table-range-measures table ks cr)))
