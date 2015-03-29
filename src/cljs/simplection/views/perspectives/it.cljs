(ns simplection.views.perspectives.it
  (:require [reagent.core :refer [atom]]
            [jayq.core :as $]
            [simplection.templates.layout :as layout :refer [layout]]
            [cljs.core.async :refer [<! >! chan close! sliding-buffer put! alts!]]
            [simplection.canvasgraph.coordinates :as cs]
            [simplection.canvasgraph.path :as p]
            [simplection.report :as rep]
            [simplection.canvasgraph.aggregator :as agg]
            [simplection.canvasgraph.aggregates :as aggs]
            [simplection.canvasgraph.data :as g-data]
            [simplection.canvasgraph.definition :as definition]
            [simplection.canvasgraph.series :as series]
            [simplection.range :as ran]
            [simplection.canvasgraph.scale :as scale]
            [simplection.canvasgraph.intersection :as inter]
            [simplection.canvasgraph.csnormalization :as norm]
            [historian.core :as hist]
            [historian.keys]
            [simplection.rendering :as rendering])
  (:require-macros [cljs.core.async.macros :as m :refer [go alt!]]))

(defn init-tab-nav []
  [:div.col-xs-3.full-height.white-border {:role "tabpanel"}
    [:ul.nav.nav-tabs {:role "tablist"}
     [:li.active {:role "presentation" :data-toggle "tooltip" :title "Connect to data"}
      [:a {:href ".tab-pane-1"
           :role "tab"
           :data-toggle "tab"}
       [:span.glyphicon.glyphicon-link {:style {:font-size "20px"}}]]]
     [:li {:role "presentation" :data-toggle "tooltip" :title "Analysis modules"}
      [:a {:href ".tab-pane-2"
           :role "tab"
           :data-toggle "tab"}
       [:span.glyphicon.glyphicon-stats {:style {:font-size "20px"}}]]]
     [:li {:role "presentation" :data-toggle "tooltip" :title "Extend / Integrate"}
      [:a {:href ".tab-pane-3"
           :role "tab"
           :data-toggle "tab"}
       [:span.glyphicon.glyphicon-asterisk {:style {:font-size "20px"}}]]]
     [:li {:role "presentation" :data-toggle "tooltip" :title "Configure"}
      [:a {:href ".tab-pane-4"
           :role "tab"
           :data-toggle "tab"}
       [:span.glyphicon.glyphicon-wrench {:style {:font-size "20px"}}]]]]
     
     [:div.tab-content
      [:div.tab-pane.tab-pane-1.active {:role "tabpanel" :id "tab-pane-1"}
       [:div.compound-btn
        [:img.icon-img {:src "images/cloud-connection.png"}]
        [:span "Connect to a cloud service"]]       
       [:div.compound-btn
        [:img.icon-img {:src "images/sql-connection.png"}]
        [:span "Connect to an SQL instance"]]       
       [:div.compound-btn
        [:img.icon-img {:src "images/live-feed-connection.png"}]
        [:span "Connect to a live feed"]]       
       [:div.compound-btn
        [:img.icon-img {:src "images/csv-connection.png"}]
        [:span "Upload a CSV file"]]       
       [:div.compound-btn
        [:img.icon-img {:src "images/file-connection.png"}]
        [:span "Other"]]
       ]
      
      [:div.tab-pane.tab-pane-2 {:role "tabpanel"} 
       [:div.compound-btn
        [:span.glyphicon.glyphicon-resize-full.icon-img]
        [:span "In/Out"]]       
       [:div.compound-btn
        [:span.glyphicon.glyphicon-tasks.icon-img]
        [:span "Preparation"]]       
       [:div.compound-btn
        [:span.glyphicon.glyphicon-random.icon-img]
        [:span "Join"]]       
       [:div.compound-btn
        [:span.glyphicon.glyphicon-th.icon-img]
        [:span "Parse"]]       
       [:div.compound-btn
        [:span.glyphicon.glyphicon-th-list.icon-img]
        [:span "Transform"]]]
      
      [:div.tab-pane.tab-pane-3 {:role "tabpanel"} 
       [:div.compound-btn
        [:img.icon-img {:src "images/function.png"}]
        [:span "Create New Function"]]       
       [:div.compound-btn
        [:img.icon-img {:src "images/sum.png"}]
        [:span "Create New Aggregate"]]       
       [:div.compound-btn
        [:img.icon-img {:src "images/interface.png"}]
        [:span "ISeriesMarker"]]       
       [:div.compound-btn
        [:img.icon-img {:src "images/interface.png"}]
        [:span "ISeriesArea"]]       
       [:div.compound-btn
        [:img.icon-img {:src "images/interface.png"}]
        [:span "ISeriesLine"]]       
       [:div.compound-btn
        [:img.icon-img {:src "images/interface.png"}]
        [:span "IGraphGrid"]]
       ]
      
      [:div.tab-pane.tab-pane-4 {:role "tabpanel"} 
       [:div.compound-btn
        [:span.glyphicon.glyphicon-user.icon-img]
        [:span "Users"]]       
       [:div.compound-btn
        [:span.glyphicon.glyphicon-eye-open.icon-img]
        [:span "User Roles & Permissions"]]       
       [:div.compound-btn
        [:span.glyphicon.glyphicon-certificate.icon-img]
        [:span "Extensibility"]]       
       [:div.compound-btn
        [:span.glyphicon.glyphicon-briefcase.icon-img]
        [:span "BA Perspective"]]   
       [:div.compound-btn
        [:span.glyphicon.glyphicon-superscript.icon-img]
        [:span "IT Perspective"]]   
       [:div.compound-btn
        [:span.glyphicon.glyphicon-blackboard.icon-img]
        [:span "Design Perspective"]]       
       [:div.compound-btn
        [:span.glyphicon.glyphicon-cog.icon-img]
        [:span "Personal Settings"]]
        [:div.compound-btn
        [:span.glyphicon.glyphicon-question-sign.icon-img]
        [:span "Help"]]   ]
      ]
     ])

(defn init-body []
  [:div.col-xs-9.full-height.white-border.tab-content
   [:div.tab-pane.full-height.tab-pane-1.active {:role "tabpanel" :style {:padding "20px"}}
    [:div 
     [:div.compound-btn
        [:img.icon-img {:src "images/cloud-connection.png"}]
        [:span "Customer Reports"]]
     
     [:div.compound-btn
        [:img.icon-img {:src "images/sql-connection.png"}]
        [:span "Customer Reports Local"]]     
     [:div.compound-btn
        [:img.icon-img {:src "images/sql-connection.png"}]
        [:span "Daily Reports"]]
     [:div.compound-btn
        [:img.icon-img {:src "images/sql-connection.png"}]
        [:span "Executive Reports"]]
       
       [:div.compound-btn
        [:img.icon-img {:src "images/csv-connection.png"}]
        [:span "Excel Improts Sales 2012"]]
       
       [:div.compound-btn
        [:img.icon-img {:src "images/csv-connection.png"}]
        [:span "Excel Improts Sales 2013"]]
       
       [:div.compound-btn
        [:img.icon-img {:src "images/csv-connection.png"}]
        [:span "Excel Improts Sales 2014"]]
       
       [:div.compound-btn
        [:img.icon-img {:src "images/live-feed-connection.png"}]
        [:span "Exchange Rates Live Feed"]]
       
       [:div.compound-btn
        [:img.icon-img {:src "images/file-connection.png"}]
        [:span "Work Hours"]]
       
       
       ]]
   
   [:div.tab-pane.full-height.tab-pane-2  {:role "tabpanel" :style {:padding "20px"}}
    [:div     
     
     [:div.compound-btn
        [:span.glyphicon.glyphicon-resize-full.icon-img]
        [:span "Browser"]]
     [:div.compound-btn
        [:span.glyphicon.glyphicon-resize-full.icon-img]
        [:span "Input Data"]]
     [:div.compound-btn
        [:span.glyphicon.glyphicon-resize-full.icon-img]
        [:span "Output Data"]]
     [:div.compound-btn
        [:span.glyphicon.glyphicon-resize-full.icon-img]
        [:span "Text Input"]]    
     
     
     [:div.compound-btn
        [:span.glyphicon.glyphicon-tasks.icon-img]
        [:span "Auto Fields"]]
     [:div.compound-btn
        [:span.glyphicon.glyphicon-tasks.icon-img]
        [:span "Auto Rows"]]
     [:div.compound-btn
        [:span.glyphicon.glyphicon-tasks.icon-img]
        [:span "Random Sample"]]
     [:div.compound-btn
        [:span.glyphicon.glyphicon-tasks.icon-img]
        [:span "Filter"]]
     [:div.compound-btn
        [:span.glyphicon.glyphicon-tasks.icon-img]
        [:span "Formula"]]
     [:div.compound-btn
        [:span.glyphicon.glyphicon-tasks.icon-img]
        [:span "Multi-field Formula"]]
     [:div.compound-btn
        [:span.glyphicon.glyphicon-tasks.icon-img]
        [:span "Multi-row Formula"]]
     
     [:div.compound-btn
        [:span.glyphicon.glyphicon-random.icon-img]
        [:span "Join"]]
     [:div.compound-btn
        [:span.glyphicon.glyphicon-random.icon-img]
        [:span "Union"]]
     [:div.compound-btn
        [:span.glyphicon.glyphicon-random.icon-img]
        [:span "Interesection"]]
     ]]
   [:div.tab-pane.full-height.tab-pane-3 {:role "tabpanel" :style {:padding "20px"}}
    [:div     
     [:div.compound-btn
        [:img.icon-img {:src "images/function.png"}]
        [:span "Get Profit"]]
     [:div.compound-btn
        [:img.icon-img {:src "images/function.png"}]
        [:span "Get Losses"]]
      [:div.compound-btn
        [:img.icon-img {:src "images/sum.png"}]
        [:span "Get Average Profit"]]
     [:div.compound-btn
        [:img.icon-img {:src "images/sum.png"}]
        [:span "Get Average Salary Per Working Day"]]
     [:div.compound-btn
        [:img.icon-img {:src "images/interface.png"}]
        [:span "SeriesMarkerStar"]]
     [:div.compound-btn
        [:img.icon-img {:src "images/interface.png"}]
        [:span "SeriesLineEdgy"]]
     [:div.compound-btn
        [:img.icon-img {:src "images/interface.png"}]
        [:span "SeriesLineSmooth"]]
     [:div.compound-btn
        [:img.icon-img {:src "images/interface.png"}]
        [:span "SeriesAreaSemiTransparent"]]
     [:div.compound-btn
        [:img.icon-img {:src "images/interface.png"}]
        [:span "GraphGridContrast"]]
     ]
    ]
   [:div.tab-pane.full-height.tab-pane-4.span-spaced  {:role "tabpanel" :style {:padding "20px"}}
    [:div
     [:span.glyphicon.glyphicon-ok.right-spaced]
     [:span.right-spaced "Super Admin"]
     [:a [:span.right-spaced "Read/Write Users"]]]
    [:div
     [:span.glyphicon.glyphicon-ok.right-spaced]
     [:span.right-spaced "Super Admin"]
     [:a [:span.right-spaced "Read/Write User Groups"]]]
    [:div
     [:span.glyphicon.glyphicon-ok.right-spaced]
     [:span.right-spaced "Super Admin"]
     [:a [:span.right-spaced "Read/Write Reports"]]]
    [:div
     [:span.glyphicon.glyphicon-ok.right-spaced]
     [:span.right-spaced "Super Admin"]
     [:a [:span.right-spaced "Read/Write Data Sources"]]]
    [:div
     [:span.glyphicon.glyphicon-ok.right-spaced]
     [:span.right-spaced "Super Admin"]
     [:a [:span.right-spaced "Read/Write Stylesheets"]]]
    
    [:div
     [:span.glyphicon.glyphicon-ok.right-spaced]
     [:span.right-spaced "Admin"]
     [:a [:span.right-spaced "Read/Write Users"]]]
    [:div
     [:span.glyphicon.glyphicon-ok.right-spaced]
     [:span.right-spaced "Admin"]
     [:a [:span.right-spaced "Read/Write User Groups"]]]
    [:div
     [:span.glyphicon.glyphicon-ok.right-spaced]
     [:span.right-spaced "Admin"]
     [:a [:span.right-spaced "Read/Write Reports"]]]
    [:div
     [:span.glyphicon.glyphicon-ok.right-spaced]
     [:span.right-spaced "Admin"]
     [:a [:span.right-spaced "Read/Write Data Sources"]]]
    [:div
     [:span.glyphicon.glyphicon-ok.right-spaced]
     [:span.right-spaced "Admin"]
     [:a [:span.right-spaced "Read/Write Stylesheets"]]]
    
    [:div
     [:span.glyphicon.glyphicon-remove.right-spaced]
     [:span.right-spaced "BA"]
     [:a [:span.right-spaced "Read/Write Users"]]]
    [:div
     [:span.glyphicon.glyphicon-remove.right-spaced]
     [:span.right-spaced "BA"]
     [:a [:span.right-spaced "Read/Write User Groups"]]]
    [:div
     [:span.glyphicon.glyphicon-ok.right-spaced]
     [:span.right-spaced "BA"]
     [:a [:span.right-spaced "Read/Write Reports"]]]
    [:div
     [:span.glyphicon.glyphicon-remove.right-spaced]
     [:span.right-spaced "BA"]
     [:a [:span.right-spaced "Read/Write Data Sources"]]]
    [:div
     [:span.glyphicon.glyphicon-remove.right-spaced]
     [:span.right-spaced "BA"]
     [:a [:span.right-spaced "Read/Write Stylesheets"]]]
    
    [:div
     [:span.glyphicon.glyphicon-remove.right-spaced]
     [:span.right-spaced "IT"]
     [:a [:span.right-spaced "Read/Write Users"]]]
    [:div
     [:span.glyphicon.glyphicon-remove.right-spaced]
     [:span.right-spaced "IT"]
     [:a [:span.right-spaced "Read/Write User Groups"]]]
    [:div
     [:span.glyphicon.glyphicon-remove.right-spaced]
     [:span.right-spaced "IT"]
     [:a [:span.right-spaced "Read/Write Reports"]]]
    [:div
     [:span.glyphicon.glyphicon-ok.right-spaced]
     [:span.right-spaced "IT"]
     [:a [:span.right-spaced "Read/Write Data Sources"]]]
    [:div
     [:span.glyphicon.glyphicon-remove.right-spaced]
     [:span.right-spaced "IT"]
     [:a [:span.right-spaced "Read/Write Stylesheets"]]]
    
    [:div
     [:span.glyphicon.glyphicon-remove.right-spaced]
     [:span.right-spaced "IT"]
     [:a [:span.right-spaced "Read/Write Users"]]]
    [:div
     [:span.glyphicon.glyphicon-remove.right-spaced]
     [:span.right-spaced "IT"]
     [:a [:span.right-spaced "Read/Write User Groups"]]]
    [:div
     [:span.glyphicon.glyphicon-remove.right-spaced]
     [:span.right-spaced "IT"]
     [:a [:span.right-spaced "Read/Write Reports"]]]
    [:div
     [:span.glyphicon.glyphicon-remove.right-spaced]
     [:span.right-spaced "IT"]
     [:a [:span.right-spaced "Read/Write Data Sources"]]]
    [:div
     [:span.glyphicon.glyphicon-ok.right-spaced]
     [:span.right-spaced "IT"]
     [:a [:span.right-spaced "Read/Write Stylesheets"]]]
    ]])

(defn designer-init []
  (layout
    [:div.container-fluid.row
     {:id "designer-container"
      :style {:min-height "600px"}}
     [:div.full-height      
      [:a.menu-nav {:href "#/start-now/ba"} " BA Perspective "]
      [:a.menu-nav {:href "#/start-now/ux"} " Design Perspective "]
      [:div {:style {:height "95%"}}
       (init-tab-nav)
       (init-body)]
      ]]))
