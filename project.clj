(defproject stock "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0-RC1"]
                 [org.clojure/core.contracts "0.0.5"]
                 [org.clojure/core.async "0.1.346.0-17112a-alpha"]
                 [yesql "0.4.1"]
                 [org.postgresql/postgresql "9.4-1201-jdbc41"]
                 [ch.qos.logback/logback-classic "1.1.1"]
                 [org.clojure/tools.logging "0.2.3"]
                 [org.clojure/clojurescript "0.0-2268"]
                 [fogus/ring-edn "0.2.0"]
                 [reagent "0.5.0"]
                 [re-frame "0.3.2"]
                 [garden "1.2.5"]
                 [clj-http "1.1.0"]
                 [cljs-http "0.1.30"]
                 [com.cognitect/transit-cljs "0.8.207"]
                 [compojure "1.3.3"]
                 [org.clojure/data.json "0.2.6"]
                 [ring "1.3.2"]]
  :plugins [[lein-cljsbuild "1.0.5"]
           ]
  :java-source-paths ["src/java"]
  :resource-paths ["resources"]
  :source-paths ["src/clj" "src/cljs"]
  :cljsbuild
      {:builds [{
                        :source-paths ["src/cljs"]
                        :compiler {
                          :closure-warnings {:non-standard-jsdoc :off}
                          :output-to "resources/public/js/Main.js"
                          :output-dir "resources/public/js/build"
                          :source-map "resources/public/js/stock.js.map"
                          :optimizations :simple }}]})




