(defproject simplection "0.1.0-SNAPSHOT"
  :description "The core of the Simplection Dashboard."
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [hiccup "1.0.5"]]
  :profiles {:dev 
             {:dependencies [[criterium "0.4.3"]
                             [midje "1.6.3"]]}})