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

;; (db/order "YHOO" 2 44.52 "1")
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

(comment
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

)

(comment

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
)

(comment

  (defn get-items [idplayer]
  (select item
    (where {:idplayer (Integer/parseInt idplayer)})))

  )
