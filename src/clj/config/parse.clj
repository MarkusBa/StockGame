(ns config.parse
  (:require [clojure.java.io :as io])
  (:import (java.io PushbackReader BufferedReader)
           (java.net Socket)))

;; use "lein typed check" to check this among other ns

;(ann ^:no-check clojure.java.io/reader [String -> BufferedReader])

;(ann ^:no-check load-config [String -> Map])
(defn load-config [filename]
  (with-open [r (io/reader filename)]
    (read (java.io.PushbackReader. r))))

;(ann config Any)
(def config (load-config "resources/config.clj"))
