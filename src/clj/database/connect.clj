(ns database.connect
  (:require [config.parse :as cf]
            [korma.core :as k]
            [korma.db :refer [defdb postgres]]))

(def db-spec (cf/load-config "resources/config.clj"))

(defdb db (postgres db-spec))

(k/defentity item)

(defn get-items []
 (k/select item))
