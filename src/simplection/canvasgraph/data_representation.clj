(ns simplection.canvasgraph.data-representation
  (:require simplection.utils))

(defprotocol PDataRepresentation
  (generate-coordinates [this]))

(defrecord Category[coll coordinates-range])

(extend-protocol PDataRepresentation
  Category
  (generate-coordinates [{categories :coll coordinates-range :coordinates-range}] 
    (let [categories-count (count (distinct categories))
          categories-range [1 categories-count]
          offset-coordinates-range (simplection.utils/offset-range coordinates-range categories-count)]
      (simplection.utils/remap-values categories-range offset-coordinates-range (range 1 (inc categories-count))))))

(defrecord Numeric[coll coordinates-range])

(extend-protocol PDataRepresentation
  Numeric
  (generate-coordinates [{values :coll coordinates-range :coordinates-range}] 
    (let [values-range [(apply min values) (apply max values)]]
      (simplection.utils/remap-values values-range coordinates-range values))))