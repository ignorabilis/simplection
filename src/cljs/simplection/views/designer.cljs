(ns simplection.views.designer
  (:require [simplection.templates.layout :as layout :refer [layout]]
            [simplection.designer.core :as controller]))

(defn init-left-menu-search []
  [:div.row {:id "designer-left-menu-search"
             :style {:height "5%"}}
   [:input.col-xs-12 {:type "text"
                      :value @controller/search-term
                      :on-change #(reset! controller/search-term (-> % .-target .-value))
                      :placeholder "Filter Measures / Dimensions"
                      :style {:height "100%"}
                      :on-key-down #(case (.-which %)
                                      27 (reset! controller/search-term "")
                                      nil)}]])

(defn case-insensitive-search [query collection]
  (if (empty? query) collection
    (let [q (.toLowerCase query)]
      (filter #(not= -1 (.indexOf (.toLowerCase (% :displayValue)) q))
              collection))))

(defn init-left-menu-measures []
  (let [query @controller/search-term
        measures @controller/measures]
    [:div.row {:id "designer-left-menu-measures"
               :style {:height "40%"}}
     [:h4 "Measures"]
     [:ul
      (for [m (case-insensitive-search query measures)]
        [:li [:a {:href "#"
                  :data-dimension true
                  :draggable true
                  :on-click #(.preventDefault %)
                  :on-drag-start #(let [dt (.-dataTransfer %)]
                                    (do (.setData dt "type" "measure")
                                      (.setData dt "value" (:value m))
                                      (.setData dt "displayValue" (:displayValue m))))}
              (:displayValue m)]])]]))

(defn init-left-menu-dimensions []
  (let [query @controller/search-term
        dimensions @controller/dimensions]
    [:div.row {:id "designer-left-menu-dimensions"
               :style {:height "35%"}}
     [:h4 "Dimensions"]
     [:ul
      (for [m (case-insensitive-search query dimensions)]
         [:li [:a {:href "#"
                  :draggable true
                  :on-click #(.preventDefault %)
                  :on-drag-start #(let [dt (.-dataTransfer %)]
                                    (do (.setData dt "type" "dimension")
                                      (.setData dt "value" (:value m))
                                      (.setData dt "displayValue" (:displayValue m))))}
              (:displayValue m)]])]]))

(defn init-left-menu-it []
  [:div.row {:id "designer-left-menu-it"
             :style {:height "20%"}}
   [:h4 {:style {:height "20%"
                 :margin "0"}}
    "IT" ]
   [:textarea {:id "designer-left-menu-it-textarea"
               :placeholder "File your reqest to IT here"
               :style {:height "80%"
                       :resize "none"
                       :width "100%"}
               :on-key-up #(case (.-which %)
                             27 (set! (-> % .-target .-value) "")
                             13 (do (set! (-> % .-target .-value) "")
                                     (layout/show-notification "Your request has been sent." :success)
                                     );TODO: send data to back-end
                             nil)}]])

(defn init-left-menu []
  [:div.col-xs-2.full-height {:id "designer-left-menu"}
   (init-left-menu-search)
   (init-left-menu-measures)
   (init-left-menu-dimensions)
   (init-left-menu-it)])

(defn init-center-columns-container []
  [:div.row {:id "designer-center-columns-container"
             :style {:height "20%"}
             :on-drag-enter #(do 
                               (.preventDefault %)
                               (set! (.-effectAllowed (.-dataTransfer %)) "copy")
                               (.add (-> % .-target .-classList) "highlighted"))
             :on-drag-leave #(do
                               (.preventDefault %)
                               (.remove (-> % .-target .-classList) "highlighted"))
             :on-drop #(let [dt (.-dataTransfer %)
                             t (.getData dt "type")
                             val (.getData dt "value")
                             dVal (.getData dt "displayValue")]
                         (do
                           (swap! controller/selected-columns conj {:value val :displayValue dVal})
                           (.remove (-> % .-target .-classList) "highlighted")))
             :on-drag-over #(.preventDefault %) }
   [:div 
    [:div "Columns"]
    (for [m @controller/selected-columns]
      [:div
       {:style {:display "inline-block"
                :margin-left "5px"
                :padding "5px"}}
       [:span (:displayValue m)]
       [:a {:href "#"
            :on-click #(do
                         (swap! controller/selected-columns clojure.set/difference #{m})
                         (.preventDefault %))
            :style {:float "right"
                    :margin-left "30px"
                    :font-weight "bold"}}
        "X"]])]])

(defn init-center-rows-container []
  [:div.col-xs-2.full-height 
   {:id "designer-center-rows-container"
    :on-drag-enter #(do 
                      (.preventDefault %)
                      (set! (.-effectAllowed (.-dataTransfer %)) "copy")
                      (.add (-> % .-target .-classList) "highlighted"))
    :on-drag-leave #(do
                      (.preventDefault %)
                      (.remove (-> % .-target .-classList) "highlighted"))
    :on-drop #(let [dt (.-dataTransfer %)
                    t (.getData dt "type")
                    val (.getData dt "value")
                             dVal (.getData dt "displayValue")]
                (do
                  (swap! controller/selected-rows conj {:value val :displayValue dVal})
                  (.remove (-> % .-target .-classList) "highlighted")))
    :on-drag-over #(.preventDefault %) }
   [:div 
    [:div "Rows"]
    (for [m @controller/selected-rows]
      [:div (:displayValue m)
       [:a {:href "#"
            :on-click #(do
                         (swap! controller/selected-rows clojure.set/difference #{m})
                         (.preventDefault %))
            :style {:float "right"
                    :font-weight "bold"}}
        "X"]])]])

(defn init-center-plot-area []
  [:div.col-xs-10.full-height {:id "designer-center-plot-area"} @controller/graph])

(defn init-center []
  [:div.col-xs-8.full-height {:id "designer-center"}
   (init-center-columns-container)
   [:div.row {:style {:height "80%"}}
    (init-center-rows-container)
    (init-center-plot-area)]])

(defn init-right-menu-chart-types []
  [:div.row {:id "designer-right-menu--types"
             :style {:height "60%"}}
   [:h4 (str "Chart Type: " @controller/selected-chart-type)]
   (let [c-types @controller/chart-types]
     [:div.container-fluid {:style {:padding "0"}}
      (for [mod '(0 1 2)]
        [:div.col-md-4
         (for [t (take-nth 3 (drop mod c-types))]
           [:button {:value t
                     :class "draggable-chart-item"
                     :on-click #(reset! controller/selected-chart-type t)
                     :style {:width "70px"
                             :margin "4px 0 0 0"}}
            t])])])])

(defn init-right-menu-settings []
  [:div.row {:id "designer-right-menu-settings"
             :style {:height "40%"}}
   "Settings"])

(defn init-right-menu []
  [:div.col-xs-2.full-height {:id "designer-right-menu"}
   (init-right-menu-chart-types)
   (init-right-menu-settings)])

(defn designer-init []
  (layout
    [:div.container-fluid.row {:id "designer-container"}
     [:div.row-fluid.full-height
      (init-left-menu)
      (init-center)
      (init-right-menu)]]))
