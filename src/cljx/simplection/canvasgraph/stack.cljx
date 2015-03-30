(ns simplection.canvasgraph.stack
  (:require [simplection.hashmap-ext :as hme]
            [simplection.canvasgraph.series :as series]))

(defn get-default-stack-rules
  [static-series]
  (map vector (series/get-default-series static-series)))

(def +-nil (fnil + 0 0))

(defn stack-coll [coll]
  (map #(and %1 %2)
    coll
    (reduce #(conj %1 (+-nil (last %1) %2)) [(first coll)] (rest coll))))

(defn stack-multi [hm stack-rules]
  (for [rule stack-rules]
    (stack-coll (hme/select-values-empty hm rule))))

(defn reduce-concat [coll]
  (reduce concat coll))

(defn stack [hm stack-rules]
  (zipmap
   (reduce-concat stack-rules)
   (reduce-concat (stack-multi hm stack-rules))))

(defn stack-table [table stack-rules]
  (for [row table
        [k v] row]
    (merge (stack v stack-rules) k)))

(defn stack-series
  [static-series]
  (let [stack-rules (get-default-stack-rules static-series)]
    (stack-table static-series stack-rules)))
