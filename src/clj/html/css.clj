(ns html.css
  (:require [garden.core :refer [css]]
            [garden.units :refer [px em percent]]))

(defn generateCSS []
  (css {:pretty-print true :output-to "resources/public/main.css"}
       [:ul {:list-style-type "none"}]))
