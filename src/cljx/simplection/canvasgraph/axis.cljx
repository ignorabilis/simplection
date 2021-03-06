(ns simplection.canvasgraph.axis
  (:require [simplection.hashmap-ext :as hme]))

(def step-coll [1 2 5 10 20 50])

(defn get-min-max
  "Get min and max as a vector."
  [table aggregate-keys]
  (let [data-range (flatten (hme/get-range-values table aggregate-keys))
        data-min (apply min data-range)
        data-max (apply max data-range)]
    [data-min data-max]))

(defn get-new-min
  [s-min]
  (if (neg? s-min) s-min 0))

(defn get-first-digit
  [number]
  (Math/floor
   (/
    (Math/abs number)
    (Math/pow 10
              (Math/floor
               (Math/log10
                (Math/abs number)))))))

(defn get-first-second-digit
  [number]
  (Math/floor
   (/
    (* 10 (Math/abs number))
    (Math/pow 10
              (Math/floor
               (Math/log10
                (Math/abs number)))))))

(defn get-base
  [number]
  (Math/floor
   (Math/log10
    (Math/abs number))))

(defn get-max-step-count
  [size]
  14)

(defn get-possible-step-counts
  [diff-first-digits]
  (map #(+ 2 (Math/floor (/ diff-first-digits %))) step-coll))

(defn get-step-count
  [size possible-step-counts diff-first-digits]
  (let [max-step-count (get-max-step-count size)]
    (first (filter #(>= max-step-count %) possible-step-counts))))

(defn indices [pred coll]
  (keep-indexed #(when (pred %2) %1) coll))

(defn get-pow-step
  [step-count possible-step-counts]
  (let [pow-step-nth (first (indices #(= step-count %) possible-step-counts))]
    (nth step-coll pow-step-nth)))

(defn get-step
  [pow-step diff-base]
  (* pow-step
     (Math/pow 10 (- diff-base 1))))

(defn get-first-digits
  "Get all meaningful digits.
  Example 1: s-min = 11534, s-min = 11302, difference = 232; the first digits will be 115.
  Example 2: s-min = 11534, s-min = 302, difference = 11232; the first digits will be 1."
  [s-max diff-base]
  (* 10
     (Math/floor
      (/ s-max
         (Math/pow 10 diff-base)))))

(defn get-first-digits-plus-one
  "Get all meaningful digits plus one.
  Example 1: s-min = 11534, s-min = 11302, difference = 232; the first digits will be 1153.
  Example 2: s-min = 11534, s-min = 302, difference = 11232; the first digits will be 11."
  [s-max diff-base]
  (Math/floor
   (/ s-max
      (Math/pow 10 (- diff-base 1)))))

(defn get-steps-needed-to-outrange-max
  [s-max diff-base first-digits pow-step]
  (let [max-to-outrange (+ 1 (get-first-digits-plus-one s-max diff-base))
        value-to-outrange (- max-to-outrange first-digits)]
    (Math/ceil (/ value-to-outrange pow-step))))

(defn get-axis-max
  [first-digits pow-step pow-steps-needed diff-base]
  (* (Math/pow 10 (- diff-base 1))
     (+ first-digits
        (* pow-step pow-steps-needed))))

(defn get-axis-min
  [new-min step-count step axis-max]
  (if (zero? new-min) new-min (- axis-max (* step step-count))))

(defn get-axis-min-max-step
  "Get the axis min, max and step values."
  [table aggregate-keys]
  (let [[s-min s-max] (get-min-max table aggregate-keys)

        new-min (get-new-min s-min)
        difference (- s-max s-min)
        diff-first-digits (get-first-second-digit difference)
        diff-base (get-base difference)
        possible-step-counts (get-possible-step-counts diff-first-digits)
        step-count (get-step-count 0 possible-step-counts diff-first-digits)
        pow-step (get-pow-step step-count possible-step-counts)
        first-digits (get-first-digits s-max diff-base)
        pow-steps-needed (get-steps-needed-to-outrange-max s-max diff-base first-digits pow-step)

        step (get-step pow-step diff-base)
        axis-max (get-axis-max first-digits pow-step pow-steps-needed diff-base)
        axis-min (get-axis-min new-min step-count step axis-max)]
    {:axis-min axis-min :axis-max axis-max :step step}))
