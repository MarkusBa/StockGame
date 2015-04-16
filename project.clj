(defproject stock "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0-alpha6"]
                 [org.clojure/core.typed "0.2.87"]
                 [korma "0.4.0"]
                 [org.clojure/java.jdbc "0.3.5"] ;; workaround for korma
                 [org.postgresql/postgresql "9.4-1201-jdbc41"]
                 [com.theoryinpractise.frege/frege "3.21.586-g026e8d7"]
                 [ch.qos.logback/logback-classic "1.1.1"]
                 [org.clojure/tools.logging "0.2.3"]
                 [clj-http "1.1.0"]
                 [compojure "1.3.3"]
                 [org.clojure/data.json "0.2.6"]
                 [ring "1.3.2"]]
  :plugins [[lein-fregec "0.1.0-SNAPSHOT"]
            [lein-typed "0.3.5"]
            [lein-deps-tree "0.1.2"]]
  :frege-source-paths ["src/frege"]
  :java-source-paths ["src/java"]
  :resource-paths ["resources"]
  :core.typed {:check [config.parse]}
  :source-paths ["src/clj"]
  )
