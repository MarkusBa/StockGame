(ns rest.rest
  (:require [clojure.data.json :as json]
            [clj-http.client :as client]
            [clojure.core.typed :refer [ann Map Any] :as t]))

;;http://finance.yahoo.com/webservice/v1/symbols/COALINDIA.NS/quote?format=json
(def resp (json/read-str (client/get "http://google.com")))
