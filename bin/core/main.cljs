(ns core.main
  (:require-macros [cljs.core.async.macros :refer [go]]
                   [reagent.ratom :refer [reaction]])
  (:require [core.definitions :as cd :refer [json-reader
                                             itemKeyVals
                                             historyKeyVals
                                             symbolKeyVals
                                             stockKeyVals
                                             initial-state]]
            [cognitect.transit :as transit]
            [reagent.core  :as r]
            [re-frame.core :as rf :refer [subscribe
                                          dispatch
                                          dispatch-sync
                                          register-handler
                                          register-sub]]
            [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]))

(enable-console-print!)

;;helper functions for cleaning the arriving json
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

(defn read-history-response [response]
  (let [temp1 (:body response)
        _ (println response)
        temp (transit/read json-reader temp1)]
    ;;(println temp)
    temp
    ))

;;handlers

(register-handler
 :initialize
 (fn [db [_ idplayer]]
   (dispatch [:itemquery idplayer])
   (merge db initial-state)))

(register-handler
 :itemquery
 (fn [db [_ idplayer]]
   (go (let [response (<! (http/get "items" {:query-params {"idplayer" idplayer}}))]
        (dispatch [:input-changed :items (read-items-response response)])))
   db))

(register-handler
 :orderquery
 (fn [db [_ idplayer ordersymbol orderamount]]
    (go (let [response (<! (http/post "order" {:form-params {"idplayer" idplayer "ordersymbol" ordersymbol "amount" orderamount}}))]
        (println response)
        (dispatch  [:itemquery idplayer])))
   db))

(register-handler
 :sellquery
 (fn [db [_ idplayer sellsymbol sellamount]]
    (go (let [response (<! (http/post "sell" {:form-params {"idplayer" idplayer "sellsymbol" sellsymbol "amount" sellamount}}))]
        (println response)
        (dispatch  [:itemquery idplayer])))
   db))

(register-handler
 :symbolquery
 (fn [db [_ param]]
  (go (let [response (<! (http/get "symbol" {:query-params {"query" param}}))]
        (dispatch [:input-changed :symbols (read-symbol-response response)])))
  db))

(register-handler
 :stockquery
 (fn [db [_ param]]
  (go (let [response (<! (http/get "stock" {:query-params {"companyname" param}}))]
        (dispatch ^:flush-dom [:input-changed :stocks (read-stock-response response)])))
  db))

(register-handler
 :historyquery
 (fn [db [_ sym a b c d e f g]]
  (println sym a b c d e f g)
  (go (let [response (<! (http/get "history" {:query-params {"sym" sym "a" a "b" b "c" c "d" d "e" e "f" f "g" g}}))]

        (dispatch [:input-changed :history (read-history-response response)])))
  db))

;; TODO refactor away
(register-handler
 :handle-actual-search
 (fn [db [_ correspkeyword querykey]]
   (let [param (correspkeyword db)]
     (dispatch [querykey param]))))

(register-handler
 :handle-timeout
 (fn [db [_ correspkeyword querykey]]
   (let [timeout (:timeout db)]
                            (if timeout ((.-clearTimeout js/window) timeout)))
                         (assoc db :timeout
                                   ((.-setTimeout js/window)
                                    (fn [] (dispatch [:handle-actual-search correspkeyword querykey]))
                                    600  ))))


(register-handler
 :handle-search
 (fn [db [_ text correspkeyword querykey]]
                         (dispatch [:handle-timeout correspkeyword querykey])
                         (assoc db correspkeyword text)))

(register-handler
 :input-changed
 (fn [db [_ inputkey text]]
   (assoc db inputkey text)))

(register-handler
 :input-changed-force
 (fn [db [_ inputkey text]]
   ;;TODO
   (println inputkey text)
   (assoc db inputkey text)
    db))

(declare render-page)

(register-handler
 :nothing
 (fn [db [_]]
  db))

;; ----components without subscriptions-----

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

(defn tableview [tablename listkeyword keyVals items]
  [:div
   [:h1 tablename]
   [tablizer items keyVals]])

(defn listview [listname listkeyword keyVals items]
  [:div
   [:h1 listname]
   [lister items keyVals]])

(defn home [my-data]
  (fn [my-data]
    (println "rendering home" my-data)
    [:div [:h1 "Chart"]
     [:div#d3-node [:svg {:style {:width "1200" :height "600"}}]]
     ]))

(defn home-did-mount [my-data]
    (println "did-mount" my-data)
    (.addGraph js/nv (fn []
                     (let [chart (.. js/nv -models lineChart
                                     (margin #js {:left 100})
                                     (useInteractiveGuideline true)
                                     (transitionDuration -1)
                                     (showLegend true)
                                     (showYAxis true)
                                     (showXAxis false))]
                       (.. chart -xAxis
                           (axisLabel "x-axis")
                           (tickFormat (.format js/d3 ",r")))
                       (.. chart -yAxis
                           (tickFormat (.format js/d3 ",r")))

                         (.. js/d3 (select "#d3-node svg")
                             (datum (clj->js [{:values  my-data
                                               :color "red"
                                               }]))
                             (call chart))))))

(defn mychart [_]
  (let [my-data (subscribe [:history])]
    (println "mychart" @my-data)
    (r/create-class {:reagent-render #(home @my-data)
                     :display-name  "my-chart"
                     :component-did-update (fn []
                                             ;;(.. js/document (getElementById "d3-node") -firstChild remove)
                                             ;;(println my-data)
                                             (home-did-mount @my-data))
                     :component-did-mount #(home-did-mount @my-data)})))

;; subscriptions

;; TODO are those necessary?
(defn reg! [kw]
 (register-sub
   kw
   (fn [db [_]]
    (reaction
      (let [valu (get-in @db [kw])]
        valu)))))

(reg! :symbols)
(reg! :stocks)
(reg! :items)
(reg! :input-symbol)
(reg! :input-stock)
(reg! :amount)
(reg! :symbol)
(reg! :is-order)
(reg! :idplayer)

(declare items)

(register-sub
 :current-page
 (fn [db [_]]
   (reaction
    (let [current-page (get-in @db [:current-page])
          page (if (nil? current-page) items current-page)]
      page))))


(reg! :counter)
;;TODO remove, probably should be nested..
(reg! :history)

(reg! :sym)
(reg! :a)
(reg! :b)
(reg! :c)
(reg! :d)
(reg! :e)
(reg! :f)
(reg! :g)

;; ----components--------

;;TODO rename items keyword

(defn atom-input-blur [place oldvalue atomkeyword querykey]
  [:input {:type "text"
           :placeholder place
           :value oldvalue
           :on-change #(let [text (-> % .-target .-value)]
                         (dispatch [:handle-search text atomkeyword querykey]))}])

(defn symbols []
  (let [input-symbol (subscribe [:input-symbol])
        symbolslist  (subscribe [:symbols])]
  (fn []
    [:div
       [atom-input-blur "symbol" @input-symbol :input-symbol :symbolquery]
       [tableview "Symbols" :symbols symbolKeyVals @symbolslist]])))

(defn stocks []
  (let [input-stock (subscribe [:input-stock])
        stockslist  (subscribe [:stocks])]
    (fn []
      [:div
        [atom-input-blur "company-name" @input-stock :input-stock :stockquery]
        [tableview "Stock" :stocks stockKeyVals @stockslist]])))

(defn atom-input [place oldvalue atomkeyword]
  [:input {:type "text"
           :placeholder place
           :value oldvalue
           :on-change #(let [text (-> % .-target .-value)]
                         (dispatch [:input-changed atomkeyword text]))}])

(defn items []
  (let [amount (subscribe [:amount])
        items (subscribe [:items])
        idplayer (subscribe [:idplayer])
        is-order (subscribe [:is-order])
        smbl (subscribe [:symbol])]
    (fn []
      [:div
        [:div
          [:h2 (if @is-order "Order" "Sell")]
          [:input {:type "checkbox"
                 :checked @is-order
                 :on-change #(dispatch [:input-changed :is-order (not @is-order)])}
           "Order"] [:br]
          [atom-input "symbol" @smbl :symbol] [:br]
          [atom-input "amount" @amount :amount] [:br]
          [:input {:type "button" :value "Commit"
              :on-click #(dispatch
                          [(if @is-order :orderquery :sellquery) @idplayer @smbl @amount])}]]
        [tableview "Items" :items itemKeyVals @items]])))

;;TODO refactor for less repetitions
(defn history []
  (let [his (subscribe [:history])
        sym (subscribe [:sym])
        a (subscribe [:a])
        b (subscribe [:b])
        c (subscribe [:c])
        d (subscribe [:d])
        e (subscribe [:e])
        f (subscribe [:f])
        g (subscribe [:g])
        counter (subscribe [:counter])
        ]
    (fn []
      [:div
        [:div
          [:h2 "History"]
          [:input {:type "button" :value "Submit"
              :on-click #(dispatch
                          [:historyquery @sym @a @b @c @d @e @f @g])}]
          [mychart nil @his]
         ]
        ])))

;;[tableview "History" :history historyKeyVals @his]

(defn pageToKeyword [page current-page]
  (if (= page current-page) :div.navelement :div.navelement-link))


(defn actual-content []
  (let [page (subscribe [:current-page])]
    (fn []
     [:div.all
       [:div.navigation
         [(pageToKeyword items @page)
          {:on-click #(dispatch ^:flush-dom [:input-changed :current-page items])}
          "Portfolio"]
         [(pageToKeyword stocks @page)
          {:on-click #(dispatch  ^:flush-dom [:input-changed :current-page stocks])}
          "Stocks"]
         [(pageToKeyword symbols @page)
          {:on-click #(dispatch ^:flush-dom [:input-changed :current-page symbols])}
          "Symbols"]
         [(pageToKeyword history @page)
          {:on-click #(dispatch ^:flush-dom [:input-changed :current-page history])}
          "History"]
        ]
       [:br]
       [:div.content
        [@page]
        ]])))

(defn content []
  [:div
    [actual-content]])

(defn render-page []
      (r/render
       [content]
       (.-body js/document)))

(defn run []
  (dispatch-sync ^:flush-dom [:initialize 1])
  (render-page))

(run)



























