(ns simplection.designer.core
  (:require [reagent.core :refer [atom]]))

(def search-term (atom ""))
(def measures (atom ["X1", "X2", "Y1", "Y2"]))
(def dimensions (atom ["D1", "D2", "CD1", "CD2"]))