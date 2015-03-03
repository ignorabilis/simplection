(ns simplection.canvasgraph.acoordinates)

(defprotocol PCoordinateSystem
  (get-coordinates-bounds [this])
  (normalize-coordinates [this table]))

(defrecord Cartesian[])
(defrecord Polar[])

(defn normalize-angle
  "Normalizes angles larger than 360 degrees."
  [angle-degrees]
  (rem (+ 360 (rem angle-degrees 360)) 360))

(defn angle-degrees->angle-radians
  "Convert angles in degrees to angles in radians"
  [angle-degrees]
  (/ (* Math/PI (- (normalize-angle angle-degrees) 90)) 180))

(defn polar-point->cartesian-point
  "Converts polar coordinates in cartesian coordinates"
  [center-x center-y [angle-degrees radius]]
  (let[angle-radians (angle-degrees->angle-radians angle-degrees)]
    [(+ center-x (* radius (Math/cos angle-radians)))
     (+ center-y (* radius (Math/sin angle-radians)))]))

(defn polar->cartesian
 "Converts a whole collection from polar coordinates to cartesian coordinates"
 [coll-polar-coordinates]
   (map #(polar-point->cartesian-point 0 0 %) coll-polar-coordinates))

(defn polar-table->cartesian-table
  [table]
  (zipmap (keys table)
    (for [[k v] table]
      (polar->cartesian v))))

(extend-protocol PCoordinateSystem

  Cartesian
  (get-coordinates-bounds
   [this]
   [0 1])

  (normalize-coordinates
   [this table]
   table)

  Polar
  (get-coordinates-bounds
   [this]
   [0 360])

  (normalize-coordinates
   [this table]
   (polar-table->cartesian-table table)))
