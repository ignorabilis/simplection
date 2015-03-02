(ns simplection.canvasgraph.acoordinates)

(defprotocol PCoordinateSystem
  (get-coordinates-bounds [this]))

(defrecord Cartesian[])
(defrecord Polar[])

(extend-protocol PCoordinateSystem

  Cartesian
  (get-coordinates-bounds [this]
                          [0 1])

  Polar
  (get-coordinates-bounds [this]
                          [0 360]))
