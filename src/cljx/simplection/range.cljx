(ns simplection.range)

(defn select-values [map ks]
  (remove nil? (reduce #(conj %1 (map %2)) [] ks)))

(defn get-range-values
  "Get all the relevant values in a map."
  [table aggregate-keys]
  (for [row table]
    (select-values row aggregate-keys)))

(defn get-min-max
  "Get min and max as a vector."
  [table aggregate-keys]
  (let [data-range (flatten (get-range-values table aggregate-keys))
        data-min (apply min data-range)
        data-max (apply max data-range)]
    [data-min data-max]))

(defn ranges-coefficient
  "Gets the coefficient of two ranges."
  [old-min old-max new-min new-max]
  (/ (- old-max old-min) (- new-max new-min)))

(defn remap-values
  "Converts the value from the old range to a value in the new range, using the coefficient k."
  [old-range new-range coll-old-values]
  (let[[old-min old-max] old-range
       [new-min new-max] new-range
       k (ranges-coefficient old-min old-max new-min new-max)]
    (map #(- new-max (/ (- old-max %) k)) coll-old-values)))

(defn offset-range
  "Offsets values evenly from the original range"
  [coll-original-range categories-count]
  (let [[original-min original-max] coll-original-range
        step (/ original-max (* 2 categories-count))]
    [(+ original-min step) (- original-max step)]))
