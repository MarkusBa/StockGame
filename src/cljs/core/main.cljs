(ns core.main
  (:require [reagent.core  :as r]
            [core.components :as cp :refer [content]]
            [core.subs :as subs]                            ;;executed before
            [re-frame.core :as rf :refer [dispatch-sync]]))

(enable-console-print!)

(defn render-page []
      (r/render
       [content]
       (.-body js/document)))

(defn run []
  (dispatch-sync ^:flush-dom [:initialize 1])
  (render-page))

(run)



























