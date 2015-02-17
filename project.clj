(defproject simplection "0.1.0-SNAPSHOT"
  :description "The core of the Simplection Business Analytics Cloud."
  :url "http://localhost:3000/"
  :source-paths ["src/clj" "src_generated/clj"]
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :min-lein-version "2.5.0"
  :uberjar-name "uber.simplection.jar"
  :auto-clean false
  :clean-targets ^{:protect false} ["resources/public/js/out"]
  :main simplection.server.core

  :dependencies [[org.clojure/clojure "1.6.0"]
                 [compojure "1.3.1"]
                 [ring/ring-core "1.3.2"]
                 [jumblerg/ring.middleware.cors "1.0.1"]
                 [org.clojure/clojurescript "0.0-2755"]
                 [org.clojure/core.async "0.1.346.0-17112a-alpha"]
                 [cljs-http "0.1.21"]
                 [hiccup "1.0.5"]
                 [hiccups "0.3.0"]
                 [garden "1.2.5"]
                 [reagent "0.5.0-alpha"]
                 [reagent-utils "0.1.2"]
                 [http-kit "2.0.0"]
                 [figwheel "0.2.3-SNAPSHOT"]
                 [secretary "1.2.1"]
                 [jayq "2.5.4"]]

  :plugins [[lein-cljsbuild "1.0.3"]
            [lein-ring "0.8.13"]
            [lein-pdo "0.1.1"]
            [lein-figwheel "0.2.3-SNAPSHOT"]]

  :profiles {:dev
             {:dependencies [[criterium "0.4.3"]
                             [midje "1.6.3"]
                             [javax.servlet/servlet-api "2.5"]]
              :plugins [[com.keminglabs/cljx "0.5.0"]]}}

  :aliases {"up" ["pdo" "run," "cljsbuild" "auto" "dev"]
            "interactive" ["pdo" "run," "cljsbuild" "auto" "dev," "cljx" "auto," "figwheel"]}

  :cljx {:builds [{:source-paths ["src/cljx"]
                   :output-path "src_generated/clj"
                   :rules :clj}

                  {:source-paths ["src/cljx"]
                   :output-path "src_generated/cljs"
                   :rules :cljs}]}

  :cljsbuild {:builds [{:id "dev"
                        :source-paths ["src/cljs/simplection/templates"
                                       "src/cljs/simplection/designer"
                                       "src/cljs/simplection/views"
                                       "src/cljs/simplection"
                                       "src_generated/cljs"
                                       "dev/simplection"]
                        :compiler {:output-to "resources/public/js/app.js"
                                   :output-dir "resources/public/js/out"
                                   :optimizations :none
                                   :main simplection.dev
                                   :asset-path "js/out"
                                   :source-map true
                                   :source-map-timestamp true
                                   :cache-analysis true}}
                       {:id "prod"}]}

  :figwheel {:http-server-root "public"
             :server-port 3449
             :css-dirs ["resources/public/css"]})
