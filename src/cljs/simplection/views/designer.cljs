(ns simplection.views.designer
  (:require [simplection.templates.layout :as layout :refer [layout]]
            [simplection.designer.core :as controller]
            [clojure.set :as set]
            [reagent.core :refer [atom]]
            [jayq.core :as $]))

(def dimension-dragged? (atom false))
(def measure-dragged? (atom false))

(defn get-css-display-value [visible?]
  (if (identity visible?) "" "none"))

(defn format-measure-dimension [obj]
  (if (nil? (:aggregate obj))
    (:displayValue obj)
    (str (:aggregate obj) "(" (:displayValue obj) ")")))

(defn case-insensitive-search [query collection]
  (if (empty? query) collection
    (let [q (.toLowerCase query)]
      (filter #(not= -1 (.indexOf (.toLowerCase (% :displayValue)) q))
              collection))))

(defn init-left-menu-search []
  [:div.row.white-border {:id "designer-left-menu-search"
                          :style {:height "5%"}}
   [:input.col-xs-12
    {:type "text"
     :value @controller/search-term
     :on-change #(reset! controller/search-term (-> % .-target .-value))
     :placeholder "Filter Measures / Dimensions"
     :style {:height "100%"}
     :on-key-down #(case (.-which %)
                     27 (reset! controller/search-term "")
                     nil)}]])

(defn init-left-menu-measures []
  (let [query @controller/search-term
        measures @controller/measures]
    [:div.row.white-border {:id "designer-left-menu-measures"
                            :style {:height "40%"}}
     [:h4 "Measures"]
     [:ul
      (for [m (case-insensitive-search query measures)]
        [:li [:a {:href "#"
                  :draggable true
                  :on-click #(.preventDefault %)
                  :on-drag-start
                  #(let [dt (.-dataTransfer %)]
                     (do (.setData dt "type" "measure")
                       (.setData dt "value" (:value m))
                       (.setData dt "displayValue" (:displayValue m))
                       (reset! measure-dragged? true)))
                  :on-drag-end
                  #(reset! measure-dragged? false)}
              (:displayValue m)]])]]))

(defn init-left-menu-dimensions []
  (let [query @controller/search-term
        dimensions @controller/dimensions]
    [:div.row.white-border {:id "designer-left-menu-dimensions"
                            :style {:height "35%"}}
     [:h4 "Dimensions"]
     [:ul
      (for [m (case-insensitive-search query dimensions)]
        [:li [:a {:href "#"
                  :draggable true
                  :on-click #(.preventDefault %)
                  :on-drag-start
                  #(let [dt (.-dataTransfer %)]
                     (do (.setData dt "type" "dimension")
                       (.setData dt "value" (:value m))
                       (.setData dt "displayValue" (:displayValue m))
                       (reset! dimension-dragged? true)))
                  :on-drag-end #(reset! dimension-dragged? false)}
              (:displayValue m)]])]]))

(defn init-left-menu-it []
  [:div.row.white-border {:id "designer-left-menu-it"
                          :style {:height "20%"}}
   [:h4 {:style {:height "20%"
                 :margin "0"}}
    "IT" ]
   [:textarea {:id "designer-left-menu-it-textarea"
               :placeholder "File your reqest to IT here"
               :style {:height "80%"
                       :resize "none"
                       :width "100%"}
               :on-key-up
               #(case (.-which %)
                  27 (set! (-> % .-target .-value) "")
                  13 (do (set! (-> % .-target .-value) "")
                       (layout/show-notification "Your request has been sent." :success)
                       );TODO: send data to back-end
                  nil)}]])

(defn init-left-menu []
  [:div.col-xs-1.full-height {:id "designer-left-menu"}
   (init-left-menu-search)
   (init-left-menu-measures)
   (init-left-menu-dimensions)
   (init-left-menu-it)])

(defn init-center-columns-container []
  [:div.row.white-border {:id "designer-center-columns-container"
                          :style {:height "40px"}}
   [:div {:style {:height "100%"}}
    [:div.col-xs-1 {:style {:height "100%"
                            :width "100px"
                            :line-height "36px"
                            :border-right "solid 1px white"
                            :text-align "center"}}
     "Columns :"]
    [:div.col-xs-11 {:style {:height "100%"
                             :position "relative"
                             :padding "0"
                             :width "calc(100% - 100px)"}}
     (for [m @controller/selected-columns]
       [:div.white-border
        {:style {:display "inline-block"
                 :margin-left "2px"
                 :margin-top "2px"
                 :height "34px"
                 :padding "5px"}}
        [:span (format-measure-dimension m)]
        [:a {:href "#"
             :on-click #(do
                          (swap! controller/selected-columns
                                 set/difference
                                 #{m})
                          (.preventDefault %))
             :style {:float "right"
                     :margin-left "30px"
                     :clear "both"
                     :font-weight "bold"}}
         "X"]])
     [:div  {:id "column-group-drop-container"
             :on-drag-enter
             #(do
                (.preventDefault %)
                (set! (.-effectAllowed (.-dataTransfer %)) "copy")
                (set! (.-effectAllowed (.-dataTransfer %)) "copy")
                (.add (-> % .-target .-classList) "highlighted"))
             :on-drag-leave
             #(do
                (.preventDefault %)
                (.remove (-> % .-target .-classList) "highlighted")
                )
             :on-drop
             #(let [dt (.-dataTransfer %)
                    t (.getData dt "type")
                    val (.getData dt "value")
                    dVal (.getData dt "displayValue")]
                (do
                  (swap! controller/selected-columns
                         conj
                         {:value val :displayValue dVal :aggregate "CATEGORY"})
                  (.remove (-> % .-target .-classList) "highlighted")))
             :on-drag-over #(.preventDefault %)
             :style {:position "absolute"
                     :top "0"
                     :margin "0"
                     :background-color "white"
                     :padding "0px"
                     :height "40px"
                     :width "100%"
                     :display (get-css-display-value
                                @measure-dragged?)}}
      "Drop Here to create a Category"]

     [:div  {:id "column-aggregate-holder"
             :style {:position "absolute"
                     :top "0"
                     :margin "0"
                     :background-color "white"
                     :padding "0px"
                     :height "40px"
                     :width "100%"
                     :display (get-css-display-value
                                @dimension-dragged?)}}
      (for [aggr @controller/aggregates]
        [:div.white-border
         {:on-drag-enter
          #(do
             (.preventDefault %)
             (set! (.-effectAllowed (.-dataTransfer %)) "copy")
             (set! (.-effectAllowed (.-dataTransfer %)) "copy")
             (.add (-> % .-target .-classList) "highlighted"))
          :on-drag-leave
          #(do
             (.preventDefault %)
             (.remove (-> % .-target .-classList) "highlighted")
             )
          :on-drop
          #(let [dt (.-dataTransfer %)
                 t (.getData dt "type")
                 val (.getData dt "value")
                 dVal (.getData dt "displayValue")]
             (do
               (swap! controller/selected-columns
                      conj
                      {:value val :displayValue dVal :aggregate aggr})
               (.remove (-> % .-target .-classList) "highlighted")))
          :on-drag-over #(.preventDefault %)
          :style {:display "inline-block"
                  :margin-left "2px"
                  :height "38px"
                  :line-height "28px"
                  :padding "5px 35px"}}
         aggr])]]
    ]])

(defn init-center-rows-container-series-grouping []
  [:div.white-border-top {:style {:height "50%" :position "relative"}}
   [:div {:style {:height "22px"}} "Series Grouping"]
   [:div
    (for [gr @controller/selected-groupings]
      [:div.white-border-top
       {:style {:padding "2px 4px"}}
       (format-measure-dimension gr)
       [:a {:href "#"
            :style {:float "right" :font-weight "bold"}
            :on-click
            #(do (swap! controller/selected-groupings
                        set/difference
                        #{gr})
               (.preventDefault %))}
        "X"]])
    [:div.white-border-top]]
   [:div {:id "series-grouping-drop-place"
          :on-drag-enter
          #(do
             (.preventDefault %)
             (set! (.-effectAllowed (.-dataTransfer %)) "copy")
             (.add (-> % .-target .-classList) "highlighted"))
          :on-drag-leave
          #(do
             (.preventDefault %)
             (.remove (-> % .-target .-classList) "highlighted"))
          :on-drop
          #(let [dt (.-dataTransfer %)
                 t (.getData dt "type")
                 val (.getData dt "value")
                 dVal (.getData dt "displayValue")]
             (do
               (swap! controller/selected-groupings
                      conj
                      {:value val :displayValue dVal :aggregate "SERIES"})
               (.remove (-> % .-target .-classList) "highlighted")))
          :on-drag-over #(.preventDefault %)
          :style {:position "absolute"
                  :background-color "rgba(255, 255, 255, 0.9)"
                  :text-align "center"
                  :border "solid 1px white"
                  :vertical-align "middle"
                  :top "22px"
                  :margin "0"
                  :padding "15px"
                  :width "100%"
                  :height "calc(100% - 22px)"
                  :display (get-css-display-value
                             (or @dimension-dragged? @measure-dragged?))}}
    "Drop Here to create Series Grouping"]])

(defn init-center-rows-container []
  [:div {:style {:height "50%" :position "relative"}
         }
   [:div {:style {:border-bottom "solid 1px white" :height "22px"}}
    "Rows :"]
   [:div
    (for [m @controller/selected-rows]
      [:div.white-border-top
       {:style {:padding "2px 4px"}}
       (format-measure-dimension m)
       [:a {:href "#"
            :on-click
            #(do
               (swap! controller/selected-rows
                      set/difference
                      #{m})
               (.preventDefault %))
            :style {:float "right"
                    :font-weight "bold"}}
        "X"]])
    [:div.white-border-top]]
   [:div  {:id "row-group-drop-container"
           :on-drag-enter
           #(do
              (.preventDefault %)
              (set! (.-effectAllowed (.-dataTransfer %)) "copy")
              (.add (-> % .-target .-classList) "highlighted"))
           :on-drag-leave
           #(do
              (.preventDefault %)
              (.remove (-> % .-target .-classList) "highlighted"))
           :on-drop
           #(let [dt (.-dataTransfer %)
                  t (.getData dt "type")
                  val (.getData dt "value")
                  dVal (.getData dt "displayValue")
                  ]
              (do
                (swap! controller/selected-rows
                       conj
                       {:value val
                        :displayValue dVal
                        :aggregate "CATEGORY"})
                (.remove (-> % .-target .-classList) "highlighted")))
           :on-drag-over
           #(.preventDefault %)
           :style {:position "absolute"
                   :background-color "white"
                   :top "22px"
                   :margin "0"
                   :width "100%"
                   :height "calc(100% - 22px)"
                   :display (get-css-display-value
                              @measure-dragged?)}}
    "Drop Here to create a Category"]

   [:div  {:id "row-aggregate-holder"
           :style {:position "absolute"
                   :background-color "white"
                   :top "22px"
                   :margin "0"
                   :width "100%"
                   :height "calc(100% - 22px)"
                   :display (get-css-display-value
                              @dimension-dragged?)}}
    (for [aggr @controller/aggregates]
      [:div.white-border
       {:style {:padding "10px"}
        :on-drag-enter
        #(do
           (.preventDefault %)
           (set! (.-effectAllowed (.-dataTransfer %)) "copy")
           (.add (-> % .-target .-classList) "highlighted"))
        :on-drag-leave
        #(do
           (.preventDefault %)
           (.remove (-> % .-target .-classList) "highlighted"))
        :on-drop
        #(let [dt (.-dataTransfer %)
               t (.getData dt "type")
               val (.getData dt "value")
               dVal (.getData dt "displayValue")
               ]
           (do
             (swap! controller/selected-rows
                    conj
                    {:value val
                     :displayValue dVal
                     :aggregate aggr})
             (.remove (-> % .-target .-classList) "highlighted")))
        :on-drag-over
        #(.preventDefault %)} aggr])]])

(defn init-center-plot-area []
  [:div.col-xs-10.full-height.white-border
   {:id "designer-center-plot-area"}
   @controller/graph])

(defn init-center []
  [:div.col-xs-10.full-height.white-border
   {:id "designer-center"}
   (init-center-columns-container)
   [:div.row.white-border
    {:style {:height "calc(100% - 40px)"}}
    [:div.col-xs-2.full-height.white-border
     {:id "designer-center-rows-container" :style {:padding "0"}}
     (init-center-rows-container)
     (init-center-rows-container-series-grouping)]
    (init-center-plot-area)]])

(defn init-right-menu-chart-types []
  [:div.row.white-border {:id "designer-right-menu--types"
                          :style {:height "40%"}}
   [:h4 (str "Chart Type: " @controller/selected-chart-type)]
   (let [c-types @controller/chart-types]
     [:div.container-fluid {:style {:padding "0"}}
      (for [mod '(0 1)]
        [:div.col-lg-12
         (for [t (take-nth 2 (drop mod c-types))]
           [:button {:value t
                     :class "draggable-chart-item"
                     :on-click
                     #(reset! controller/selected-chart-type t)
                     :style {:width "100%"
                             :margin "4px 0 0 0"}}
            t])])])
   [:h4 (str "Coordinate System: " (:displayValue @controller/selected-coord-sys-type))]
   (let [c-types @controller/coord-sys-types]
     [:div.container-fluid {:style {:padding "0"}}
         (for [t c-types]
           [:button {:value (:value t)
                     :class "draggable-chart-item"
                     :on-click
                     #(reset! controller/selected-coord-sys-type t)
                     :style {:width "100%"
                             :margin "4px 0 0 0"}}
            (:displayValue t)])])
   ])

(defn init-right-menu-settings []
  [:div.row.white-border {:id "designer-right-menu-settings"
                          :style {:height "60%"}}
   "Settings"])

(defn init-right-menu []
  [:div.col-xs-1.full-height {:id "designer-right-menu"}
   (init-right-menu-chart-types)
   (init-right-menu-settings)])

(defn designer-init []
  (layout
    [:div.container-fluid.row
     {:id "designer-container"
      :style {:min-height "600px"}}
     [:div.row-fluid.full-height
      (init-left-menu)
      (init-center)
      (init-right-menu)]]))