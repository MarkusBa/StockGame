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
    (println temp)
    temp
    ))

;;handlers

;;TODO more handlers (for the go loops)

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
        (dispatch [:input-changed :stocks (read-stock-response response)])))
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

;; subscriptions

;; TODO are those necessary?

(register-sub
 :symbols
 (fn [db [_]]
   (reaction
    (let [symbols (get-in @db [:symbols])]
      symbols))))

(register-sub
 :stocks
 (fn [db [_]]
   (reaction
    (let [stocks (get-in @db [:stocks])]
      stocks))))

(register-sub
 :items
 (fn [db [_]]
   (reaction
    (let [items (get-in @db [:items])]
      items))))

(register-sub
 :input-symbol
 (fn [db [_]]
   (reaction
    (let [input-symbol (get-in @db [:input-symbol])]
      input-symbol))))

(register-sub
 :input-stock
 (fn [db [_]]
   (reaction
    (let [input-stock (get-in @db [:input-stock])]
      input-stock))))

(register-sub
 :amount
 (fn [db [_]]
   (reaction
    (let [amount (get-in @db [:amount])]
      amount))))

(register-sub
 :symbol
 (fn [db [_]]
   (reaction
    (let [smbl (get-in @db [:symbol])]
      smbl))))

(register-sub
 :is-order
 (fn [db [_]]
   (reaction
    (let [is-order (get-in @db [:is-order])]
      is-order))))

(register-sub
 :idplayer
 (fn [db [_]]
   (reaction
    (let [idplayer (get-in @db [:idplayer])]
      idplayer))))

(declare items)

(register-sub
 :current-page
 (fn [db [_]]
   (reaction
    (let [current-page (get-in @db [:current-page])
          page (if (nil? current-page) items current-page)]
      page))))

;;TODO remove, probably should be nested..
(register-sub
 :history
 (fn [db [_]]
   (reaction
    (let [his (get-in @db [:history])]
      his))))

(register-sub
 :sym
 (fn [db [_]]
   (reaction
    (let [his (get-in @db [:sym])]
      his))))

(register-sub
 :a
 (fn [db [_]]
   (reaction
    (let [his (get-in @db [:a])]
      his))))

(register-sub
 :b
 (fn [db [_]]
   (reaction
    (let [his (get-in @db [:b])]
      his))))

(register-sub
 :c
 (fn [db [_]]
   (reaction
    (let [his (get-in @db [:c])]
      his))))

(register-sub
 :d
 (fn [db [_]]
   (reaction
    (let [his (get-in @db [:d])]
      his))))

(register-sub
 :e
 (fn [db [_]]
   (reaction
    (let [his (get-in @db [:e])]
      his))))

(register-sub
 :f
 (fn [db [_]]
   (reaction
    (let [his (get-in @db [:f])]
      his))))

(register-sub
 :g
 (fn [db [_]]
   (reaction
    (let [his (get-in @db [:g])]
      his))))


(comment
(for [kw cd/history-keywords]
 (register-sub
   kw
   (fn [db [_]]
    (reaction
      (let [valu (get-in @db [kw])]
        valu))))))

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
        ]
    (fn []
      [:div
        [:div
          [:h2 "History"]
          [:input {:type "button" :value "Submit"
              :on-click #(dispatch
                          [:historyquery @sym @a @b @c @d @e @f @g])}]
          [tableview "History" :history historyKeyVals @his]
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



























