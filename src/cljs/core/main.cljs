(ns core.main
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [reagent.core :as r :refer [atom]]
            [cognitect.transit :as transit]
            [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]))

(enable-console-print!)

(def json-reader (transit/reader :json))

(defn read-symbol-response [response]
  (let [temp1 (:body response)
        temp (transit/read json-reader temp1)
        text (get (get temp "ResultSet") "Result")]
      text))

(defn read-stock-response [response]
  (let [temp1 (:body response)
        temp (transit/read json-reader temp1)
        text (get (get (get temp "query") "results")"quote")]
      (if (vector? text) text (vector text))))

;; state
(def state (atom {:qstock nil :stock nil :query nil :stocks [{"symbol" "SZG.SG", "name" "SALZGITTER", "exch" "STU", "type" "S", "exchDisp" "Stuttgart", "typeDisp" "Equity"}
                           {"symbol" "SZG.MU", "name" "SALZGITTER", "exch" "MUN", "type" "S", "exchDisp" "Munich", "typeDisp" "Equity"}]}))

;; json reading
(defn symbolquery [param]
  (go (let [response (<! (http/get "symbol" {:query-params {"query" param}}))]
        (swap! state assoc :stocks (read-symbol-response response)))))

(defn stockquery [param]
  (go (let [response (<! (http/get "stock" {:query-params {"companyname" param}}))]
        (swap! state assoc :stock (read-stock-response response)))))

;; ----components-----
(defn listview [listname element listkeyword]
  [:div
   [:h1 listname]
   [element (listkeyword @state)]])

(defn atom-input [state atomkeyword queryfunction]
  [:input {:type "text"
           ;:value (:qstock @state)
           :on-blur #(let [text (-> % .-target .-value)]
                         (swap! state assoc atomkeyword text)
                         (queryfunction text))}])

;;; ----symbols----

(defn symbollister [items]
  [:ul
   (for [item items]
     ^{:key item} [:li
                   [:ul
                    [:li "Symbol:" (get item "symbol")]
                    [:li "Name:" (get item "name")]
                    [:li "Exchange:" (get item "exchDisp")]
                    [:li "Type:" (get item "typeDisp")]
                   ]])])

(defn symbols []
  [:div
     [atom-input state :query symbolquery]
     [listview "Symbols" symbollister :stocks]])

;;; -----stocks-----

(defn stocklister [items]
  [:ul
   (for [item items]
     ^{:key item} [:li
                   [:ul
                    [:li "Symbol:" (get item "symbol")]
                    [:li "StockExchange:" (get item "StockExchange")]
                    [:li "Ask:" (get item "Ask")]
                    [:li "Bid:" (get item "Bid")]
                    [:li "AverageDailyVolume:" (get item "AverageDailyVolume")]
                    [:li "BookValue:" (get item "BookValue")]
                    [:li "Currency:" (get item "Currency")]
                    [:li "Change:" (get item "Change")]
                    [:li "YearLow:" (get item "YearLow")]
                    [:li "YearHigh:" (get item "YearHigh")]
                    [:li "MarketCapitalization:" (get item "MarketCapitalization")]
                    [:li "PercentChangeFromYearLow:" (get item "PercentChangeFromYearLow")]
                    [:li "HundreddayMovingAverage:" (get item "HundreddayMovingAverage")]
                    [:li "FiftydayMovingAverage:" (get item "FiftydayMovingAverage")]
                    [:li "DividendYield:" (get item "DividendYield")]
                    [:li "Notes:" (get item "Notes")]
                    [:li "PEGRatio:" (get item "PEGRatio")]
                    [:li "ExDividendDate:" (get item "ExDividendDate")]

                   ]])])

(defn stocks []
  [:div
    [atom-input state :qstock stockquery]
    [listview "Stock" stocklister :stock]])

(defn render-page [innercontent]
  (r/render-component
   [content innercontent]
     (.-body js/document)))

(defn content [innercontent]
  [:div.all
     [:div.navigation
       [:div.navelement "Portfolio"]
       [:div.navelement {:on-click #(render-page stocks)} "Stocks"]
       [:div.navelement {:on-click #(render-page symbols)} "Symbols"]
      ]
     [:br]
     [:div.content
      [innercontent]]])

(render-page stocks)


































