(ns simplection.core
  (:require [simplection.views.home :as home]
            [simplection.views.perspectives.ba :as ba]
            [simplection.views.perspectives.it :as it]
            [simplection.views.perspectives.design :as design]
            [reagent.core :as reagent :refer [atom]]
            [reagent.session :as session]
            [secretary.core :as secretary :include-macros true]
            [goog.events :as events]
            [goog.history.EventType :as EventType])
  (:import goog.History))

;; -------------------------
;; Views
(defn current-page []
  [(session/get :current-page)])

;; -------------------------
;; Routes
(secretary/set-config! :prefix "#")

(secretary/defroute "/" []
  (session/put! :current-page home/home-page))

(secretary/defroute "/pricing" []
  (session/put! :current-page home/pricing-page))

(secretary/defroute "/start-now/ba" []
  (session/put! :current-page ba/designer-init))

(secretary/defroute "/start-now/it" []
  (session/put! :current-page it/designer-init))

(secretary/defroute "/start-now/ux" []
  (session/put! :current-page design/designer-init))
;; -------------------------
;; History
;; must be called after routes have been defined
(defn hook-browser-navigation! []
  (doto (History.)
    (events/listen
     EventType/NAVIGATE
     (fn [event]
       (secretary/dispatch! (.-token event))))
    (.setEnabled true)))

;; -------------------------
;; Initialize app
(defn init! []
  (hook-browser-navigation!)
  (reagent/render-component [current-page] (.getElementById js/document "app")))
