(ns core.start
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [reagent.core :as r]
            [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]))

(enable-console-print!)

(defn init []
  (r/render-component
   [list-of-stocks]
   (.-body js/document)))


;;(query "salzgitter")

(go (let [response (<! (http/get "query" {:edn-params {:query "salzgitter"}}))]
      (println (:status response))
      (println (:body response))))




;;(init)

































(comment

(def app-state
  (r/atom
   {:query nil}))

(def ^:private meths
  {:get "GET"
   :put "PUT"
   :post "POST"
   :delete "DELETE"})

(defn edn-xhr [{:keys [method url data on-complete]}]
  (let [xhr (XhrIo.)]
    (events/listen xhr goog.net.EventType.COMPLETE
      (fn [e]
        (on-complete (reader/read-string (.getResponseText xhr)))))
    (. xhr
      (send url (meths method) (when data (pr-str data))
        #js {"Content-Type" "application/edn"}))))

(defn query [q]
  (edn-xhr
    {:method :get
     :url (str "query")
     :data {:query q}
     :on-complete
     (fn [res]
       (apply swap! app-state update-in [:query] res))}))

(defn lister [items]
  [:ul
   (for [item items]
     ^{:key item} [:li item])])

(defn list-of-stocks []
  [:div
   [:h1 "List of stocks"]
   [lister (:query (@app-state))]])

  )
