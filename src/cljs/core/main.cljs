(ns core.main
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [reagent.core :as r :refer [atom]]
            [cognitect.transit :as transit]
            [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]))

(enable-console-print!)

(def json-reader (transit/reader :json))

(def itemKeyVals
  (partition 2 ["Symbol:" "symbol"
   "Amount:" "amount"
   "Price:" "price"
   "Date:" "ts"]))

(def symbolKeyVals
  (partition 2 ["Symbol:" "symbol"
   "Name:" "name"
   "Exchange:" "exchDisp"
   "Type:" "typeDisp"]))

(def stockKeyVals
  (partition 2 ["Symbol:" "symbol"
   "StockExchange:" "StockExchange"
   "Ask:" "Ask"
   "Bid:" "Bid"
   "AverageDailyVolume:" "AverageDailyVolume"
   "BookValue:" "BookValue"
   "Currency:" "Currency"
   "Change:" "Change"
   "YearLow:" "YearLow"
   "YearHigh:" "YearHigh"
   "MarketCapitalization:" "MarketCapitalization"
   "PercentChangeFromYearLow:" "PercentChangeFromYearLow"
   "HundreddayMovingAverage:" "HundreddayMovingAverage"
   "FiftydayMovingAverage:" "FiftydayMovingAverage"
   "DividendYield:" "DividendYield"
   "Notes:" "Notes"
   "PEGRatio:" "PEGRatio"
   "ExDividendDate:" "ExDividendDate"]))

;;TODO
(def idplayer 1)

(defn read-items-response [response]
  (let [temp1 (:body response)
        temp (transit/read json-reader temp1)]
      temp))

(defn read-symbol-response [response]
  (let [temp1 (:body response)
        temp (transit/read json-reader temp1)
        text (get (get temp "ResultSet") "Result")]
      (println "type: " (type text))
      text))

(defn read-stock-response [response]
  (let [temp1 (:body response)
        temp (transit/read json-reader temp1)
        text (get (get (get temp "query") "results")"quote")]
      (if (vector? text) text (vector text))))

;; state
(def state (atom {:ordersymbol nil
                  :orderamount nil
                  :sellsymbol nil
                  :sellamount nil
                  :items nil
                  :input-stock nil
                  :stocks nil
                  :input-symbol nil
                  :symbols [{"symbol" "SZG.SG", "name" "SALZGITTER", "exch" "STU", "type" "S", "exchDisp" "Stuttgart", "typeDisp" "Equity"}
                           {"symbol" "SZG.MU", "name" "SALZGITTER", "exch" "MUN", "type" "S", "exchDisp" "Munich", "typeDisp" "Equity"}]}))

;; queries
(defn itemquery [param]
  (go (let [response (<! (http/get "items" {:query-params {"idplayer" param}}))]
        (swap! state assoc :items (read-items-response response)))))

(defn orderquery [param]
  (let [ordersymbol (:ordersymbol @state)
        orderamount (:orderamount @state)]
    (go (let [response (<! (http/post "order" {:form-params {"idplayer" param "ordersymbol" ordersymbol "amount" orderamount}}))]
        (println response)))))

(defn sellquery [param]
  (let [sellsymbol (:sellsymbol @state)
        sellamount (:sellamount @state)]
    (go (let [response (<! (http/post "sell" {:form-params {"idplayer" param "sellsymbol" sellsymbol "amount" sellamount}}))]
        (println response)))))

(defn symbolquery [param]
  (go (let [response (<! (http/get "symbol" {:query-params {"query" param}}))]
        (swap! state assoc :symbols (read-symbol-response response)))))

(defn stockquery [param]
  (go (let [response (<! (http/get "stock" {:query-params {"companyname" param}}))]
        (swap! state assoc :stocks (read-stock-response response)))))

;; ----components-----

(defn lister [items keyVals]
  [:ul
   (for [item items]
     ^{:key item} [:li
                   [:ul.datalist
                    (for [[k v] keyVals]
                      ^{:key k}
                       [:li
                        [:span {:style {:display "inline-block" :width "250px" :text-align "left"}} k ]
                        [:span {:style {:display "inline-block" :text-align "left"}} (get item v)]])]])])


(defn listview [listname listkeyword keyVals]
  [:div
   [:h1 listname]
   [lister (listkeyword @state) keyVals]])

(defn atom-input-blur [state atomkeyword queryfunction]
  [:input {:type "text"
           ;:value (:qstock @state)
           :on-blur #(let [text (-> % .-target .-value)]
                         (swap! state assoc atomkeyword text)
                         (queryfunction text))}])

(defn atom-input [state atomkeyword]
  [:input {:type "text"
           :value (atomkeyword @state)
           :on-change #(let [text (-> % .-target .-value)]
                         (swap! state assoc atomkeyword text))}])

(defn symbols []
  [:div
     [atom-input-blur state :input-symbol symbolquery]
     [listview "Symbols" :symbols symbolKeyVals]])

(defn stocks []
  [:div
    [atom-input-blur state :input-stock stockquery]
    [listview "Stock" :stocks stockKeyVals]])

(defn order []
  [:div
   [:h1 "Order"]
   [:h2 "Symbol"] [atom-input state :ordersymbol]
   [:h2 "Amount"] [atom-input state :orderamount]
   [:input {:type "button" :value "Commit"
            :on-click #(orderquery idplayer)}]])

(defn sell []
  [:div
   [:h1 "Sell"]
   [:h2 "Symbol"] [atom-input state :sellsymbol]
   [:h2 "Amount"] [atom-input state :sellamount]
   [:input {:type "button" :value "Commit"
            :on-click #(sellquery idplayer)}]])

(defn items []
  [:div
    [order]
    [sell]
    [listview "Items" :items itemKeyVals]])

(declare content)

(defn render-page [innercontent]
  (r/render-component
   [content innercontent]
     (.-body js/document)))

(defn content [innercontent]
  [:div.all
     [:div.navigation
       [:div.navelement {:on-click #(render-page items)} "Portfolio"]
       [:div.navelement {:on-click #(render-page stocks)} "Stocks"]
       [:div.navelement {:on-click #(render-page symbols)} "Symbols"]
      ]
     [:br]
     [:div.content
      [innercontent]]])

(itemquery idplayer)

(render-page stocks)


































