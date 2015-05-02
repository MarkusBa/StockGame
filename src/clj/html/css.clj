(ns html.css
  (:require [garden.core :refer [css]]
            [garden.units :refer [px em percent]]))

;; (require '[html.css :as cs] :reload)
;; (cs/generateCSS)

(def content
  [:.content {:margin-left "20px" :margin-top "40px"}])

(def ul
  [:.datalist {:list-style-type "none"}])

(def left
  [:.left {:float "left"}])

(def right
  [:.right {:float "right"}])

(def div-table
  [:.div-table {:display "table"
                :width   "auto"
                :background-color "#eee"
                :border "1px solid #666666"
                :border-spacing "5px"}])

(def div-table-row
  [:.div-table-row {:display "table-row"
                    :width   "auto"
                    :clear   "both"}])

(def div-table-col
  [:.div-table-col {:float     "left"
                    :display   "table-column"
                    :min-height "1px"
                    :width     "130px"}])


(def div-table-head-col
  [:.div-table-head-col {:float     "left"
                    :display   "table-column"
                    :width     "130px"
                    :background-color "#ccc"}])


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
   left
   right
   div-table
   div-table-row
   div-table-col
   div-table-head-col
   navelement))
