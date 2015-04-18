(ns core.start
  (:require [reagent.core :as r]))

(enable-console-print!)

(defn greeting []
  [:div
   [:h1 "Hi"]])


 (defn bla []
  (r/render-component
   [greeting]
   (.-body js/document)))

 (do
   (println "test")
   (bla)
   )

   ;;
   ;;(.getElementById js/document "app"))
