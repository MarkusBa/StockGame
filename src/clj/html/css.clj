(ns html.css
  (:require [garden.core :refer [css]]
            [garden.units :refer [px em percent]]))

(def content
  [:.content {:margin "10px"}])

(def ul
  [:ul {:list-style-type "none"}])


(defn generateCSS []
  (css
   {:pretty-print true :output-to "resources/public/main.css"}
   ul
   content))
