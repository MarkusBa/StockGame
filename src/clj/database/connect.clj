(ns database.connect
  (:require [config.parse :as cf]
            [clojure.core.typed :refer [ann Map] :as t]
            [clojure.tools.logging :as log]
            [korma.core :as k :refer [defentity delete select where values insert update fields set-fields]]
            [korma.db :refer [defdb postgres transaction]])
  (:import (java.sql Timestamp)))

(ann db-spec Map)
(def db-spec (cf/load-config "resources/config.clj"))

(ann ^:no-check korma.db/postgres [Map -> Map])
(ann ^:no-check korma.db/create-db [Map -> Map])
(ann ^:no-check korma.db/default-connection [Map -> Map])
(ann ^:no-check korma.core/create-entity [String -> Map])

(ann db Map)
(defdb db (postgres db-spec))

(defentity item)

(defn get-items [idplayer]
  (select item
    (where {:idplayer (Integer/parseInt idplayer)})))

(defmacro existing-amount [idplayer# ordersymbol#]
   `(:amount (first (select item
                  (fields :amount)
                  (where {:idplayer (Integer/parseInt ~idplayer#)
                          :symbol   ~ordersymbol#})))))

(defmacro change-position [newamount# change-symbol# idplayer#]
    `(update item
      (set-fields {:amount ~newamount#})
      (where {:idplayer (Integer/parseInt ~idplayer#)
                          :symbol   ~change-symbol#})))


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
        (change-position (- money costs) "CASH" idplayer)
        (if-let [existingamount (existing-amount idplayer ordersymbol)]
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

;; (db/sell "YHOO" 2 44.52 "1")
(defn sell [sellsymbol amount price idplayer]
  (transaction
   (let [existingamount (existing-amount idplayer sellsymbol)
         existingcash (existing-amount idplayer "CASH")]
     (when (and (not (nil? existingamount)) (>= existingamount amount))
       (change-position (+ existingcash (* amount price)) "CASH" idplayer)
       (if (> existingamount amount)
         (change-position (- existingamount amount) sellsymbol idplayer)
         (delete item
            (where {:idplayer (Integer/parseInt idplayer)
                          :symbol   sellsymbol})))))))
