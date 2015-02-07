(ns simplection.datasources.csv)

(use 'clojure.string)

(defn to-csv [text delimiter]
  (let [[keys & data] (map #(map trim %)
                           (map #(split % (re-pattern delimiter))
                                (split text #"\n|\r|\n\r")))
        keys (map keyword keys)]
    (map #(apply sorted-map (interleave keys %)) data)))

(defn from-csv 
  ([text] (to-csv text ","))
  ([text delimiter] (to-csv text delimiter)))