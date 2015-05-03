(ns core.main
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [reagent.core :as r :refer [atom]]
            [cognitect.transit :as transit]
            [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]))

(enable-console-print!)

(def json-reader (transit/reader :json))

(def itemKeyVals
  (partition 2 ["Symbol" "symbol"
   "Amount" "amount"
   "Price" "price"
   "Current price" "currentprice"
   "Date" "ts"]))

(def symbolKeyVals
  (partition 2 ["Symbol" "symbol"
   "Name" "name"
   "Exchange" "exchDisp"
   "Type" "typeDisp"]))

(def stockKeyVals
  (partition 2 ["Symbol" "symbol"
   "StockExchange" "StockExchange"
   "Ask" "Ask"
   "Bid" "Bid"
   "AverageDailyVolume" "AverageDailyVolume"
   "BookValue" "BookValue"
   "Currency" "Currency"
   "Change" "Change"
   "YearLow" "YearLow"
   "YearHigh" "YearHigh"
   "MarketCapitalization" "MarketCapitalization"
   "PercentChangeFromYearLow" "PercentChangeFromYearLow"
   "100dayMovingAverage" "HundreddayMovingAverage"
   "50dayMovingAverage" "FiftydayMovingAverage"
   "DividendYield" "DividendYield"
   "Notes" "Notes"
   "PEGRatio" "PEGRatio"
   "ExDividendDate" "ExDividendDate"]))


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
(def state (atom {:timeout nil
                  :order true
                  :current-page nil
                  :ordersymbol nil
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
  (let [ordersymbol (:symbol @state)
        orderamount (:amount @state)]
    (go (let [response (<! (http/post "order" {:form-params {"idplayer" param "ordersymbol" ordersymbol "amount" orderamount}}))]
        (println response)
        (itemquery idplayer)))))

(defn sellquery [param]
  (let [sellsymbol (:symbol @state)
        sellamount (:amount @state)]
    (go (let [response (<! (http/post "sell" {:form-params {"idplayer" param "sellsymbol" sellsymbol "amount" sellamount}}))]
        (println response)
        (itemquery idplayer)))))

(defn symbolquery [param]
  (go (let [response (<! (http/get "symbol" {:query-params {"query" param}}))]
        (swap! state assoc :symbols (read-symbol-response response)))))

(defn stockquery [param]
  (go (let [response (<! (http/get "stock" {:query-params {"companyname" param}}))]
        (swap! state assoc :stocks (read-stock-response response)))))

;; ----components-----

(defn tablizer [items keyVals]
  (let [headers (map first itemKeyVals)]
    [:table
     ;;heading
     [:tr
       (for [[k v] keyVals]
         [:th
                          k])]
     (for [item items]
       ^{:key item} [:tr
                      (for [[k v] keyVals]
                         [:td
                          (get item v)])])]))


(defn tableview [tablename listkeyword keyVals]
  [:div
   [:h1 tablename]
   [tablizer (listkeyword @state) keyVals]])


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

(defn atom-input-blur [place state atomkeyword queryfunction]
  [:input {:type "text"
           :placeholder place
           :value (atomkeyword @state)
           :on-change #(let [text (-> % .-target .-value)]
                         (swap! state assoc atomkeyword text)
                         (let [timeout (:timeout @state)]
                            (if timeout ((.-clearTimeout js/window) timeout)))
                         (swap! state assoc :timeout ((.-setTimeout js/window) (fn [] (queryfunction (atomkeyword @state))) 600  )))}])

(defn atom-input [place state atomkeyword]
  [:input {:type "text"
           :placeholder place
           :value (atomkeyword @state)
           :on-change #(let [text (-> % .-target .-value)]
                         (swap! state assoc atomkeyword text))}])

(defn symbols []
  [:div
     [atom-input-blur "symbol" state :input-symbol symbolquery]
     [tableview "Symbols" :symbols symbolKeyVals]])

(defn stocks []
  [:div
    [atom-input-blur "company-name" state :input-stock stockquery]
    [tableview "Stock" :stocks stockKeyVals]])

(defn order-sell []
  [:div
    [:h2 (if (:order @state) "Order" "Sell")]
    [:input {:type "checkbox"
             :checked (:order @state)
             :on-change #(let [old (:order @state)]
                         (swap! state assoc :order (not old)))}
     "Order"] [:br]
    [atom-input "symbol" state :symbol] [:br]
    [atom-input "amount" state :amount] [:br]
    [:input {:type "button" :value "Commit"
            :on-click #((if (:order @state) orderquery sellquery) idplayer)}]])

(defn items []
  [:div
    [order-sell]
    [tableview "Items" :items itemKeyVals]])

(declare content)

(defn render-page [innercontent]
  (swap! state assoc :current-page innercontent)
  (r/render-component
   [content innercontent]
     (.-body js/document)))

(defn pageToKeyword [page]
  (if (= page (:current-page @state)) :div.navelement :div.navelement-link))

(defn content [innercontent]
  [:div.all
     [:div.navigation
       [(pageToKeyword items)
        {:on-click #(render-page items)}
        "Portfolio"]
       [(pageToKeyword stocks)
        {:on-click #(render-page stocks)} "Stocks"]
       [(pageToKeyword symbols)
        {:on-click #(render-page symbols)} "Symbols"]
      ]
     [:br]
     [:div.content
      [innercontent]]])


(itemquery idplayer)
(render-page items)


































