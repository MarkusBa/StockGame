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
            [database.connect :as db]))

(defn index []
  (file-response "public/html/index.html" {:root "resources"}))

(defn generate-response [data & [status]]
  {:status (or status 200)
   :headers {"Content-Type" "application/edn"}
   :body (pr-str data)})

(defn items []
  (generate-response (db/get-items )))

(defn symbol-from-yahoo [searchstring]
  (let [res (cljstr/replace (:body (client/get (str "http://d.yimg.com/autoc.finance.yahoo.com/autoc?query=" searchstring
                                  "&callback=YAHOO.Finance.SymbolSuggest.ssCallback")))
                  #"YAHOO.Finance.SymbolSuggest.ssCallback\((.*?)\)" "$1")
        text res]
        (log/info "queryyahoo: " text)
        text))

(defn getsymbol [{:keys [query] :as params}]
  (generate-response (symbol-from-yahoo query)))

(defn stock-from-yahoo [symbole]
  (let [url "http://query.yahooapis.com/v1/public/yql"
        q-param (str "select * from yahoo.finance.quotes where symbol in(\"" symbole "\")"  )
        env-param "http://datatables.org/alltables.env"
        format-param "json"]
    (:body (client/get url {:query-params {"q" q-param "env" env-param "format" format-param}} ))))

(defn getstock [{:keys [symbole] :as params}]
  (generate-response (stock-from-yahoo symbole)))

(defroutes routes
  (GET "/" [] (index))
  (GET "/symbol" {params :params}
    (getsymbol params))
  (GET "/stock" {params :params}
    (getstock params))
  (GET "/items" [] (items))
  (route/files "/" {:root "resources/public"}))

(def app
  (-> routes handler/site))

(defonce server
  (run-jetty #'app {:port 8080 :join? false}))

