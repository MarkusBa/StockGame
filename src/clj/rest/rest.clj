(ns rest.rest
  (:require [clojure.data.json :as json]
            [clj-http.client :as client]
            [clojure.core.typed :refer [ann Map Any] :as t]))

(def resp (json/read-str (client/get "http://google.com")))
