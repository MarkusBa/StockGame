(ns core.start
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [reagent.core :as r :refer [atom]]
            [cognitect.transit :as transit]
            [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]))

(enable-console-print!)

(def rdr (transit/reader :json))

(defn read-response [response]
  (let [temp1 (:body response)
        nthg  (println temp1)
        temp (transit/read rdr temp1)
        text (get (get temp "ResultSet") "Result")]
     (println text)
      text))

;;;;;; state
(def state (atom {:stocks [{"symbol" "SZG.SG", "name" "SALZGITTER", "exch" "STU", "type" "S", "exchDisp" "Stuttgart", "typeDisp" "Equity"}
                           {"symbol" "SZG.MU", "name" "SALZGITTER", "exch" "MUN", "type" "S", "exchDisp" "Munich", "typeDisp" "Equity"}]}))

(defn yahooquery [param]
  ;(println param)
  (go (let [response (<! (http/get "query" {:query-params {"query" param}}))]
        (swap! state assoc :stocks (read-response response)))))

(defn lister [items]
  [:ul
   (for [item items]
     ^{:key item} [:li (get item "symbol")])])

(defn list-of-stocks []
  [:div
   [:h1 "List of stocks"]
   [lister (:stocks @state)]])

(defn init []
  (r/render-component
   [list-of-stocks]
   (.-body js/document)))

(init)

(yahooquery "basf")

(comment
  (defn yahooquery [param]
  (read-response (http/get "query" {:query-params {"query" param}})))

  (defn read-response [response]
  (let [temp (transit/read rdr (:body response))]
     (println temp)
      temp))

  (temp (cljs.reader/read-string temp1))
  (temp (.parse js/JSON temp1))
)


































