(ns simplection.canvasgraph.apath)

(defprotocol PPathGeometry
  (generate-data-path [this table]))

(defrecord Straight[])

(extend-protocol PPathGeometry
  Straight
  (generate-data-path
    [this points]
    (let [start-point (first points)
          path ["M"]]
      (reduce #(concat %1 ["L" (first %2) (second %2)]) (concat path start-point) points))))
