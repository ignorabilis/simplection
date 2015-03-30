(ns simplection.canvasgraph.aarea)

(defprotocol PAreaGeometry
  (generate-auxiliary-paths [this table])
  (generate-area [this table]))

(defrecord None [])
(defrecord Shape [])
(defrecord ShapeArea [])
(defrecord AreaToAxis [])

(extend-protocol PAreaGeometry
  None

  (generate-auxiliary-paths
   [this series-data]
    series-data)

  (generate-area
   [this series-data]
   series-data)

  Shape

  (generate-auxiliary-paths
   [this series-data]
    series-data)

  (generate-area
   [this series-data]
   (assoc
     series-data
     :path
     (conj (vec (:path series-data)) "Z")))

  ShapeArea

  (generate-auxiliary-paths
   [this series-data]
    series-data)

  (generate-area
   [this series-data]
   (assoc
     (assoc
       series-data
       :path
       (conj (vec (:path series-data)) "Z"))
     :area
     (conj (vec (:path series-data)) "Z")))

  AreaToAxis

  (generate-auxiliary-paths
   [this series-data]
   (let [path (vec (:path series-data))
         x0 (second path)
         y0 (nth path 2)
         x1 (second path)
         y1 0
         x2 (nth path (- (count path) 2))
         y2 0
         x3 (nth path (- (count path) 2))
         y3 (last path)]
    (assoc
       series-data
       :aux-path
       ["M" x3 y3 "L" x3 y3 "L" x2 y2 "L" x1 y1 "L" x0 y0])))

  (generate-area
   [this series-data]
   (let [path (vec (:path series-data))
         aux-path (vec (:aux-path series-data))]
     (assoc
       series-data
       :area
       (concat path (drop 6 aux-path) ["Z"])))))
