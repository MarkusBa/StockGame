(ns core.definitions
  (:require [cognitect.transit :as transit]))

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

(def initial-state
                 {:idplayer 1
                  :timeout nil
                  :is-order true
                  :current-page nil
                  :symbol nil
                  :amount nil
                  :items nil
                  :input-stock nil
                  :stocks nil
                  :input-symbol nil
                  :symbols [{"symbol" "SZG.SG", "name" "SALZGITTER", "exch" "STU", "type" "S", "exchDisp" "Stuttgart", "typeDisp" "Equity"}
                           {"symbol" "SZG.MU", "name" "SALZGITTER", "exch" "MUN", "type" "S", "exchDisp" "Munich", "typeDisp" "Equity"}]})

