(ns html.css
  (:require [garden.core :refer [css]]
            [garden.units :refer [px em percent]]))

(def content
  [:.content {:margin-left "20px" :margin-top "40px"}])

(def ul
  [:ul {:list-style-type "none"}])

(def navelement
  [:.navelement
       {:float "left"
        :border "1px"
        :margin "5px"
        :font-weight "bold"}])

(defn generateCSS []
  (css
   {:pretty-print true :output-to "resources/public/main.css"}
   ul
   content
   navelement))
