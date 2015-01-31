(defproject simplection "0.1.0-SNAPSHOT"
  :description "The core of the Simplection Dashboard."
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [compojure "1.1.5"]
                 [ring/ring-core "1.3.2"]
                 [hiccup "1.0.5"]]
  :profiles {:dev
             {:dependencies [[criterium "0.4.3"]
                             [midje "1.6.3"]]}}

  :plugins [[lein-ring "0.9.1"]]
  :ring {:handler simplection.handler/app})
