(ns caves-of-clojure.core
  (:require [lanterna.screen :as lanterna-screen]))

; This is the run function
(defn run [screen-type]
  (let [my-screen (lanterna-screen/get-screen screen-type)]
    (lanterna-screen/in-screen
      my-screen
      (doto my-screen
        (lanterna-screen/put-string 0 0 "Welcome to the Caves of Clojure!")
        (lanterna-screen/put-string 0 1 "Press any key to exit...")
        (lanterna-screen/redraw)
        (lanterna-screen/get-key-blocking)))))

; Ths is -main
(defn -main [& args]
  (let [set-args (set args)
        screen-type (cond
                      (set-args ":swing") :swing
                      (set-args ":text") :text
                      :else :auto)]
    (run screen-type)))
