(ns routing.routes
  (:require [ring.util.response :refer [file-response]]
            [ring.adapter.jetty :refer [run-jetty]]
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
   :headers {"Content-Type" "application/json"}
   :body (json/write-str data :value-fn date-writer )})

(defn items []
  (generate-response (db/get-items )))

(defroutes routes
  (GET "/" [] (index))
  (GET "/items" [] (items))
  (route/files "/" {:root "resources/public"}))

(def app
  (-> routes))

(defonce server
  (run-jetty #'app {:port 8080 :join? false}))
