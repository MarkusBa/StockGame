(ns routing.routes
  (:require [ring.util.response :refer [file-response]]
            [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.edn :refer [wrap-edn-params]]
            [clj-http.client :as client]
            [compojure.core :refer [defroutes GET PUT DELETE POST]]
            [compojure.route :as route]
            [clojure.test :as ct :refer [is with-test]]
            [compojure.handler :as handler]
            [clojure.data.json :as json]
            [clojure.set :as cljset :refer [subset?]]
            [clojure.string :as cljstr]
            [clojure.tools.logging :as log]
            [database.connect :as db])
  (:import (java.sql Date)))

;;(require '(require '[clojure.test :as ct])
;;(ct/run-tests 'routing.routes)

(defn date-writer [key value]
  (if (= key :ts)
    (str (java.sql.Date. (.getTime value)))
    value))

(defn index []
  (file-response "public/html/index.html" {:root "resources"}))

(defn generate-response [data & [status]]
  {:status (or status 200)
   :headers {"Content-Type" "application/json"}
   :body (pr-str data)})

(defn symbol-from-yahoo [searchstring]
  (let [response (cljstr/replace (:body (client/get (str "http://d.yimg.com/autoc.finance.yahoo.com/autoc?query=" searchstring
                                  "&callback=YAHOO.Finance.SymbolSuggest.ssCallback")))
                  #"YAHOO.Finance.SymbolSuggest.ssCallback\((.*?)\)" "$1")]
        (log/info "symbol-from-yahoo: " response)
        response))

(defn getsymbol [{:keys [query] :as params}]
  (generate-response (symbol-from-yahoo query)))

(defn stock-from-yahoo [symbole]
  (let [symbollist (cljstr/join "," (map #(str "\"" % "\"") symbole))
        url "http://query.yahooapis.com/v1/public/yql"
        q-param (str "select * from yahoo.finance.quotes where symbol in(" symbollist ")"  )
        env-param "http://datatables.org/alltables.env"
        format-param "json"
        response (client/get url {:query-params {"q" q-param "env" env-param "format" format-param}} )]
    (log/info "stock-from-yahoo:" response)
    (:body response)))

;;(rt/prices-from-yahoo ["YHOO" "PAH3.DE"]  true)
(with-test
  (defn prices-from-yahoo [symbole sell]
    (let [yhoostocks (get (get (get (json/read-str (stock-from-yahoo symbole))"query") "results")"quote")
          stocks (if (and (not (nil? symbole)) (= 1 (count symbole))) (vector yhoostocks) yhoostocks)
          prices (if sell
                   (map #(vector (get % "symbol") (get % "Bid")) stocks)
                   (map #(vector (get % "symbol") (get % "Ask")) stocks))]
       prices))
  (is (= #{"YHOO" "PAH3.DE"} (reduce #(conj %1 (first %2)) #{} (prices-from-yahoo ["YHOO" "PAH3.DE"]  true))))
  (is (= "YHOO" (first (first (prices-from-yahoo ["YHOO"]  false))))))

(defn items [{:keys [idplayer] :as params}]
  (let [itemsmap (db/get-items idplayer)
        symbols-to-prices (if (= 1 (count itemsmap)) {} (into {} (prices-from-yahoo (filter #(not (= % "CASH")) (map :symbol itemsmap)) true)))
        itemsrich (map #(assoc % :currentprice (get symbols-to-prices (:symbol %))) itemsmap)]
  (generate-response (json/write-str itemsrich :value-fn date-writer))))

(defn name->symbols [companyname]
  (log/info "name->symbols " companyname)
  (map #(get % "symbol") (get (get (json/read-str (symbol-from-yahoo companyname)) "ResultSet") "Result")))

(defn getstock [{:keys [symbole companyname] :as params}]
  (let [actualsymbols (if (cljstr/blank? symbole)
                            (name->symbols companyname)
                            (cljstr/split symbole #"%20"))]
      (log/info "actualsymbols: " actualsymbols)
      (generate-response (stock-from-yahoo actualsymbols))))

(defn order [{:keys [ordersymbol amount idplayer] :as params}]
  (let [price (second (first (prices-from-yahoo [ordersymbol] false)))]
      (log/info "order: " ordersymbol " " amount " " idplayer)
      (generate-response (db/order ordersymbol (Double/parseDouble amount) (Double/parseDouble price) idplayer))))

(defn sell [{:keys [sellsymbol amount idplayer] :as params}]
  (let [price (second (first (prices-from-yahoo [sellsymbol] true)))]
      (log/info "sell: " sellsymbol " " amount " " idplayer)
      (generate-response (db/sell sellsymbol (Double/parseDouble amount) (Double/parseDouble price) idplayer))))

(defroutes routes
  (GET "/" [] (index))
  (GET "/symbol" {params :params}
    (getsymbol params))
  (POST "/order" {params :params}
    (order params))
  (POST "/sell" {params :params}
    (sell params))
  (GET "/stock" {params :params}
    (getstock params))
  (GET "/items" {params :params} (items params))
  (route/files "/" {:root "resources/public"}))

(def app
  (-> routes handler/site))

(defonce server
  (run-jetty #'app {:port 8080 :join? false}))

(comment
  ;;symbol
  ;symbol-from-yahoo:  {"ResultSet":{"Query":"porsche","Result":
  ;[{"symbol":"PAH3.DE","name":"Porsche Automobil Holding SE","exch":"GER","type":"S","exchDisp":"XETRA","typeDisp":"Equity"},{"symbol":"POAHY","name":"Porsche Automobil Holding SE","exch":"PNK","type":"S","exchDisp":"OTC Markets","typeDisp":"Equity"},{"symbol":"PAH3.F","name":"PORSCHE AUTOHLDG VZ","exch":"FRA","type":"S","exchDisp":"Frankfurt","typeDisp":"Equity"},{"symbol":"PAH3.MU","name":"PORSCHE AUTOHLDG VZ","exch":"MUN","type":"S","exchDisp":"Munich","typeDisp":"Equity"},{"symbol":"POAHF.PK","name":"PORSCHE AUTOMOBIL HG","exch":"PNK","type":"S","exchDisp":"OTC Markets","typeDisp":"Equity"},{"symbol":"POAHY.PK","name":"PORSCHE AUTO ADR","exch":"PNK","type":"S","exchDisp":"OTC Markets","typeDisp":"Equity"},{"symbol":"PAH3.HM","name":"PORSCHE AUTOHLDG VZ","exch":"HAM","type":"S","exchDisp":"Hamburg","typeDisp":"Equity"},{"symbol":"PAH3.DU","name":"PORSCHE AUTOHLDG VZ","exch":"DUS","type":"S","exchDisp":"Dusseldorf Stock Exchange ","typeDisp":"Equity"},{"symbol":"0JHU.L","name":"Porsche Automobil Holding SE","exch":"LSE","type":"S","exchDisp":"London","typeDisp":"Equity"},{"symbol":"PAH3.SG","name":"PORSCHE AUTOHLDG VZ","exch":"STU","type":"S","exchDisp":"Stuttgart","typeDisp":"Equity"}]}}

  ;;stock
  ;{:status 200, :headers {X-YQL-Host pprd1-node1024-lh3.manhattan.bf1.yahoo.com, Server ATS, Age 0, Content-Type application/json; charset=UTF-8, Access-Control-Allow-Origin *, X-Content-Type-Options nosniff, Connection close, Transfer-Encoding chunked, Date Fri, 24 Apr 2015 10:14:52 GMT, Cache-Control no-cache},
  ;:body {"query":{"count":2,"created":"2015-04-24T10:14:52Z","lang":"en-US","results":{"quote":[{"symbol":"SZG.DE","Ask":"29.74","AverageDailyVolume":"479489","Bid":"29.71","AskRealtime":null,"BidRealtime":null,"BookValue":"56.95","Change_PercentChange":"+0.01 - +0.05%","Change":"+0.01","Commission":null,"Currency":"EUR","ChangeRealtime":null,"AfterHoursChangeRealtime":null,"DividendShare":null,"LastTradeDate":"4/24/2015","TradeDate":null,"EarningsShare":"-0.68","ErrorIndicationreturnedforsymbolchangedinvalid":null,"EPSEstimateCurrentYear":"1.04","EPSEstimateNextYear":null,"EPSEstimateNextQuarter":"0.00","DaysLow":"29.44","DaysHigh":"30.12","YearLow":"21.00","YearHigh":"33.81","HoldingsGainPercent":null,"AnnualizedGain":null,"HoldingsGain":null,"HoldingsGainPercentRealtime":null,"HoldingsGainRealtime":null,"MoreInfo":null,"OrderBookRealtime":null,"MarketCapitalization":"1.61B","MarketCapRealtime":null,"EBITDA":"309.78M","ChangeFromYearLow":"8.74","PercentChangeFromYearLow":"+41.59%","LastTradeRealtimeWithTime":null,"ChangePercentRealtime":null,"ChangeFromYearHigh":"-4.07","PercebtChangeFromYearHigh":"-12.04%","LastTradeWithTime":"11:59am - <b>29.74</b>","LastTradePriceOnly":"29.74","HighLimit":null,"LowLimit":null,"DaysRange":"29.44 - 30.12","DaysRangeRealtime":null,"FiftydayMovingAverage":"28.23","TwoHundreddayMovingAverage":"25.27","ChangeFromTwoHundreddayMovingAverage":"4.47","PercentChangeFromTwoHundreddayMovingAverage":"+17.70%","ChangeFromFiftydayMovingAverage":"1.51","PercentChangeFromFiftydayMovingAverage":"+5.35%","Name":"SALZGITTER","Notes":null,"Open":"29.67","PreviousClose":"29.73","PricePaid":null,"ChangeinPercent":"+0.05%","PriceSales":"0.17","PriceBook":"0.52","ExDividendDate":"5/23/2014","PERatio":null,"DividendPayDate":null,"PERatioRealtime":null,"PEGRatio":"0.00","PriceEPSEstimateCurrentYear":null,"PriceEPSEstimateNextYear":null,"Symbol":"SZG.DE","SharesOwned":null,"ShortRatio":"0.00","LastTradeTime":"11:59am","TickerTrend":null,"OneyrTargetPrice":null,"Volume":"161770","HoldingsValue":null,"HoldingsValueRealtime":null,"YearRange":"21.00 - 33.81","DaysValueChange":null,"DaysValueChangeRealtime":null,"StockExchange":"GER","DividendYield":null,"PercentChange":"+0.05%"},{"symbol":"SZG.MU","Ask":"29.730","AverageDailyVolume":"984","Bid":"29.705","AskRealtime":null,"BidRealtime":null,"BookValue":"0.000","Change_PercentChange":"-0.750 - -2.444%","Change":"-0.750","Commission":null,"Currency":"EUR","ChangeRealtime":null,"AfterHoursChangeRealtime":null,"DividendShare":null,"LastTradeDate":"4/24/2015","TradeDate":null,"EarningsShare":null,"ErrorIndicationreturnedforsymbolchangedinvalid":null,"EPSEstimateCurrentYear":null,"EPSEstimateNextYear":null,"EPSEstimateNextQuarter":"0.000","DaysLow":"29.870","DaysHigh":"29.935","YearLow":"21.170","YearHigh":"33.615","HoldingsGainPercent":null,"AnnualizedGain":null,"HoldingsGain":null,"HoldingsGainPercentRealtime":null,"HoldingsGainRealtime":null,"MoreInfo":null,"OrderBookRealtime":null,"MarketCapitalization":null,"MarketCapRealtime":null,"EBITDA":"0.00","ChangeFromYearLow":"8.765","PercentChangeFromYearLow":"+41.403%","LastTradeRealtimeWithTime":null,"ChangePercentRealtime":null,"ChangeFromYearHigh":"-3.680","PercebtChangeFromYearHigh":"-10.947%","LastTradeWithTime":"8:30am - <b>29.935</b>","LastTradePriceOnly":"29.935","HighLimit":null,"LowLimit":null,"DaysRange":"29.870 - 29.935","DaysRangeRealtime":null,"FiftydayMovingAverage":"28.245","TwoHundreddayMovingAverage":"25.267","ChangeFromTwoHundreddayMovingAverage":"4.668","PercentChangeFromTwoHundreddayMovingAverage":"+18.477%","ChangeFromFiftydayMovingAverage":"1.690","PercentChangeFromFiftydayMovingAverage":"+5.982%","Name":"SALZGITTER","Notes":null,"Open":"29.870","PreviousClose":"30.685","PricePaid":null,"ChangeinPercent":"-2.444%","PriceSales":null,"PriceBook":null,"ExDividendDate":"5/23/2014","PERatio":null,"DividendPayDate":null,"PERatioRealtime":null,"PEGRatio":"0.000","PriceEPSEstimateCurrentYear":null,"PriceEPSEstimateNextYear":null,"Symbol":"SZG.MU","SharesOwned":null,"ShortRatio":"0.000","LastTradeTime":"8:30am","TickerTrend":null,"OneyrTargetPrice":null,"Volume":"100","HoldingsValue":null,"HoldingsValueRealtime":null,"YearRange":"21.170 - 33.615","DaysValueChange":null,"DaysValueChangeRealtime":null,"StockExchange":"MUN","DividendYield":null,"PercentChange":"-2.444%"}]}}}, :request-time 2558, :trace-redirects [http://query.yahooapis.com/v1/public/yql], :orig-content-encoding nil}
)
