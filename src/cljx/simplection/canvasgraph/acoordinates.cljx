(ns simplection.canvasgraph.acoordinates)

(defprotocol PCoordinateSystem
  (generate-coordinates [this]))

(defrecord Cartesian[])
(defrecord Polar[])

(extend-protocol PCoordinateSystem

  Cartesian
  (generate-coordinates [this])

Polar
  (generate-coordinates [this]))
