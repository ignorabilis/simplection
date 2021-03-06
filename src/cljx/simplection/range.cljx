(ns simplection.range)

(defn ranges-coefficient
  "Gets the coefficient of two ranges."
  [old-min old-max new-min new-max]
  (/ (- old-max old-min) (- new-max new-min)))

(defn remap-value
  "Converts the value from the old range to a value in the new range, using the coefficient k; ignores nil."
  [[old-min old-max] [new-min new-max] old-value]
  (let[k (ranges-coefficient old-min old-max new-min new-max)]
    (and old-value (- new-max (/ (- old-max old-value) k)))))

(defn remap-values
  "Converts the values from the old range to a value in the new range, using the coefficient k."
  [[old-min old-max] [new-min new-max] coll-old-values]
  (let[k (ranges-coefficient old-min old-max new-min new-max)]
    (map #(- new-max (/ (- old-max %) k)) coll-old-values)))

(defn offset-range
  "Converts data range to a new range with an offset."
  [[original-min original-max] categories-count]
  (let [step (/ original-max (* 2 categories-count))]
    [(+ original-min step) (- original-max step)]))
