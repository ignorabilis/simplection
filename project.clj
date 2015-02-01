(defproject simplection "0.1.0-SNAPSHOT"
  :description "The core of the Simplection Dashboard."
  :url "http://localhost:3000/"
  :source-paths ["src" "src/clj"]
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.6.0"]
                 [compojure "1.1.5"]
                 [ring/ring-core "1.3.2"]
                 [jumblerg/ring.middleware.cors "1.0.1"]
                 [org.clojure/clojurescript "0.0-2371"]
                 [org.clojure/core.async "0.1.346.0-17112a-alpha"]
                 [cljs-http "0.1.21"]
                 [hiccup "1.0.5"]
                 [http-kit "2.0.0"]
                 [cheshire "5.4.0"]]

  :profiles {:dev
             {:dependencies [[criterium "0.4.3"]
                             [midje "1.6.3"]
                             [javax.servlet/servlet-api "2.5"]]}}

  :plugins [[lein-cljsbuild "1.0.3"]
            [lein-ring "0.8.13"]
            [lein-pdo "0.1.1"]]

  :main simplection.core

  :aliases {"up" ["pdo" "run," "cljsbuild" "auto" "dev"]}

  :cljsbuild {:builds [{:id "dev"
                        :source-paths ["src/cljs/simplection"]
                        :compiler {:output-to "resources/public/js/app.js"
                                   :output-dir "resources/public/js/out"
                                   :optimizations :none
                                   :source-map true}}]})
