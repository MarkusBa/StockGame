(ns routing.routes
  (:require [ring.util.response :refer [file-response]]
            [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.edn :refer [wrap-edn-params]]
            [clj-http.client :as client]
            [compojure.core :refer [defroutes GET PUT DELETE POST]]
            [compojure.route :as route]
            [compojure.handler :as handler]
            [clojure.data.json :as json]
            [clojure.string :as cljstr]
            [clojure.tools.logging :as log]
            [database.connect :as db])
  (:import (java.sql Date)))

(defn index []
  (file-response "public/html/index.html" {:root "resources"}))

(defn date-writer [key value]
  (if (= key :ts)
    (str (java.sql.Date. (.getTime value)))
    value))

(defn generate-response [data & [status]]
  {:status (or status 200)
   :headers {"Content-Type" "application/edn"}
   :body (pr-str data)})

(defn items []
  (generate-response (db/get-items )))

(defn queryyahoo [searchstring]
  (let [res (cljstr/replace (:body (client/get (str "http://d.yimg.com/autoc.finance.yahoo.com/autoc?query=" searchstring
                                  "&callback=YAHOO.Finance.SymbolSuggest.ssCallback")))
                  #"YAHOO.Finance.SymbolSuggest.ssCallback\((.*?)\)" "$1")
        text res]
        ;;text (get (get (json/read-str res) "ResultSet") "Result")]
        (log/info "queryyahoo: " text)
        text))

(defn exquery [{:keys [query] :as params}]
  (generate-response (queryyahoo query)))

(defn querystock [symbole]
  (let [url "http://query.yahooapis.com/v1/public/yql"
        q-param (str "select * from yahoo.finance.quotes where symbol in(\"" symbole "\")"  )
        env-param "http://datatables.org/alltables.env"
        format-param "json"]
    (:body (client/get url {:query-params {"q" q-param "env" env-param "format" format-param}} ))))

(defn exstock [{:keys [symbole] :as params}]
  (generate-response (querystock symbole)))

(defroutes routes
  (GET "/" [] (index))
  (GET "/query" {params :params}
    (exquery params))
  (GET "/stock" {params :params}
    (exstock params))
  (GET "/items" [] (items))
  (route/files "/" {:root "resources/public"}))

(def app
  (-> routes handler/site))

(defonce server
  (run-jetty #'app {:port 8080 :join? false}))

