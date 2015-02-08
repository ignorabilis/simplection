(ns simplection.templates.layout)

(defn header []
  [:div.navbar
   [:div
    [:a {:href "#/"} "Simplection"]
    " "
    [:a {:href "#/pricing"} "Pricing"]
    " "
    [:a {:href "#/start-now"} "Start Now"]]])

(defn footer []
  [:div.navbar
   [:div
    [:a {:href "#/"} "Simplection"]
    " "
    [:a {:href "#/data-mining"} "Data Mining"]
    " "
    [:a {:href "#/reporting"} "Reporting"]]])

(defn layout [content]
  [:div.full-height
   [header] 
   content 
   [footer]])
