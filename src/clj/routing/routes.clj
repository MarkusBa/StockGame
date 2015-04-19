(ns routing.routes
  (:require [ring.util.response :refer [file-response]]
            [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.edn :refer [wrap-edn-params]]
            [clj-http.client :as client]
            [compojure.core :refer [defroutes GET PUT DELETE POST]]
            [compojure.route :as route]
            [compojure.handler :as handler]
            [clojure.data.json :as json]
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
  (log/error "searchstring is" searchstring)
  (:body (client/get (str "http://d.yimg.com/autoc.finance.yahoo.com/autoc?query=" searchstring
                                  "&callback=YAHOO.Finance.SymbolSuggest.ssCallback"))))

(defn query [{:keys [query] :as params}]
  (log/error "query" query)
  (generate-response (queryyahoo query)))

(defroutes routes
  (GET "/" [] (index))
  (GET "/query" {params :params edn-params :edn-params}
    (log/error "fuck you" params edn-params)
    (query edn-params))
  (GET "/items" [] (items))
  (route/files "/" {:root "resources/public"}))

(def app
  (-> routes
      wrap-edn-params))

(defonce server
  (run-jetty #'app {:port 8080 :join? false}))
