(ns simplection.canvasgraph.data-paths)

(defprotocol PDataPathGeometry
  (generate-data-path [this]))

(defrecord Straight[coll])

(extend-protocol PDataPathGeometry
  Straight
  (generate-data-path [{points :coll}] 
    (vec 
      (for [point points]
        (vec
          (flatten
            (let [start-point (first point)
                  x (first start-point)
                  y (rest start-point)
                  path (list "M" x y)]
              (concat path
                      (for [y (rest point)]
                        (list "L" y))))))))))