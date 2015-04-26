(ns database.connect
  (:require [config.parse :as cf]
            [clojure.core.typed :refer [ann Map Set] :as t]
            [clojure.tools.logging :as log]
            [clojure.java.jdbc :as jdbc]
            [yesql.core :refer [defquery]])
  (:import (java.sql Timestamp)
           (java.lang Boolean)))

(ann db-spec Map)
(def db-spec (cf/load-config "resources/config.clj"))

(defquery get-items-query "sql/select.sql")

(defn get-items [idplayer]
  (get-items-query db-spec (Integer/parseInt idplayer)))

(defquery existing-amount "sql/existingamount.sql")
(defquery update-item! "sql/updateitem.sql")
(defquery insert-item! "sql/insertitem.sql")
(defquery delete-item! "sql/deleteitem.sql")

;; (db/order "YHOO" 2 44.52 "1") -> 1
(defn order [ordersymbol amount price idpl]
  (jdbc/with-db-transaction [connection db-spec]
    (let [idplayer (Integer/parseInt idpl)
          money (:amount (first (existing-amount connection idplayer "CASH")))
          costs (* amount price)]
      (log/info money)
      (when (and (not (nil? money)) (>= money costs) )
        (update-item! connection (- money costs) "CASH" idplayer)
        (if-let [existingamount (:amount (first (existing-amount connection idplayer ordersymbol)))]
          (update-item! connection (+ existingamount amount) ordersymbol idplayer)
          (insert-item! connection ordersymbol amount price idplayer (java.sql.Timestamp. (System/currentTimeMillis))))))))

;; (db/sell "YHOO" 2 44.52 "1") -> 1
(defn sell [sellsymbol amount price idpl]
  (jdbc/with-db-transaction [connection db-spec]
   (let [idplayer (Integer/parseInt idpl)
         existingamount (:amount (first (existing-amount connection idplayer sellsymbol)))
         existingcash (:amount (first (existing-amount connection idplayer "CASH")))]
     (when (and (not (nil? existingamount)) (>= existingamount amount))
       (update-item! connection (+ existingcash (* amount price)) "CASH" idplayer)
       (if (> existingamount amount)
         (update-item! connection (- existingamount amount) sellsymbol idplayer)
         (delete-item! connection idplayer sellsymbol))))))



