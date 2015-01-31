(ns simplection.core
  (:require-macros [cljs.core.async.macros :refer [go alt!]])
  (:require [goog.events :as events]
            [cljs.core.async :refer [put! <! >! chan timeout]]
            [cljs-http.client :as http]))

(enable-console-print!)

(.log js/console "hello world")
