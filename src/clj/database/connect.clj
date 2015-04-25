(ns database.connect
  (:require [config.parse :as cf]
            [clojure.tools.logging :as log]
            [korma.core :as k :refer [defentity select where values insert update fields set-fields]]
            [korma.db :refer [defdb postgres transaction]])
  (:import (java.sql Timestamp)))

(def db-spec (cf/load-config "resources/config.clj"))

(defdb db (postgres db-spec))

(defentity item)

(defn get-items [idplayer]
  (select item
    (where {:idplayer (Integer/parseInt idplayer)})))

;; (db/order "YHOO" 2 44.52 "1")
(defn order [ordersymbol amount price idplayer]
  (transaction
    (let [money (:amount (first (select item
                  (fields :amount)
                  (where {:idplayer (Integer/parseInt idplayer)
                          :symbol   "CASH"}))))
          costs (* amount price)]
      (log/info money)
      (when (and (not (nil? money)) (>= money costs) )
        (update item
          (set-fields {:amount (- money costs)})
          (where {:idplayer (Integer/parseInt idplayer)
                          :symbol   "CASH"}))
        (if-let [existingamount (:amount (first (select item
                  (fields :amount)
                  (where {:idplayer (Integer/parseInt idplayer)
                          :symbol   ordersymbol}))))]
          (update item
            (set-fields {:amount (+ existingamount amount)})
            (where {:idplayer (Integer/parseInt idplayer)
                          :symbol   ordersymbol}))
          (insert item
            (values [{:symbol ordersymbol
                      :amount amount
                      :price price
                      :idplayer (Integer/parseInt idplayer)
                      :ts (java.sql.Timestamp. (System/currentTimeMillis))}])))))))
