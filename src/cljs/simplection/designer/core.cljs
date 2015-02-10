(ns simplection.designer.core
  (:require [reagent.core :refer [atom]]
            [jayq.core :as $]))

(def search-term (atom ""))
(def measures (atom ["X1", "X2", "Y1", "Y2"]))
(def dimensions (atom ["D1", "D2", "CD1", "CD2"]))
(def chart-types (atom ["Gant", "Pie", "Bar", "Line", "Scatter", "Funnel"]))
	