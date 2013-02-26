(ns caves-of-clojure.core
  (:require [lanterna.screen :as lanterna-screen]))

(defrecord UI [kind])
(defrecord Game [world uis input])

(defn clear-screen [screen]
  (let [blank (apply str (repeat 80 \space))]
    (doseq [row (range 24)]
      (lanterna-screen/put-string screen 0 row blank))))

(defmulti draw-ui
  (fn [ui game screen]
    (:kind ui)))

(defmethod draw-ui :start [ui game screen]
  (lanterna-screen/put-string screen 0 0 "Welcome to the Caves of Clojure!")
  (lanterna-screen/put-string screen 0 1 "Press enter to win, anything else to lose."))

(defmethod draw-ui :win [ui game screen]
  (lanterna-screen/put-string screen 0 0 "Congratulations, you win!")
  (lanterna-screen/put-string screen 0 1 "Press escape to exit, anything else to restart."))

(defmethod draw-ui :lose [ui game screen]
  (lanterna-screen/put-string screen 0 0 "Sorry, better luck next time.")
  (lanterna-screen/put-string screen 0 1 "Press escape to exit, anything else to go."))

(defn draw-game [game screen]
  (clear-screen screen)
  (doseq [ui (:uis game)]
    (draw-ui ui game screen))
  (lanterna-screen/redraw screen))

(defmulti process-input
  (fn [game input]
    (:kind (last (:uis game)))))

(defmethod process-input :start [game input]
  (if (= input :enter)
    (assoc game :uis [(new UI :win)])
    (assoc game :uis [(new UI :lose)])))

(defmethod process-input :win [game input]
  (if (= input :escape)
    (assoc game :uis [])
    (assoc game :uis [(new UI :start)])))

(defmethod process-input :lose [game input]
  (if (= input :escape)
    (assoc game :uis [])
    (assoc game :uis [(new UI :start)])))

(defn get-input [game screen]
  (assoc game :input (lanterna-screen/get-key-blocking screen)))

(defn run-game [input-game screen]
  (loop [{:keys [input uis] :as loop-game} input-game]
    (when-not (empty? uis)
      (draw-game loop-game screen)
      (if (nil? input)
        (recur (get-input loop-game screen))
        (recur (process-input (dissoc loop-game :input) input))))))

(defn new-game []
  (new Game
    (new World)
    [(new UI :start)]
    nil))

; This is the run function
(defn run
  ([screen-type]
    (run screen-type false))
  ([screen-type block?]
    (letfn [(go []
              (let [screen (lanterna-screen/get-screen screen-type)]
                (lanterna-screen/in-screen screen
                  (run-game (new-game) screen))))]
      (if block?
        (go)
        (future (go))))))

; This is -main
(defn -main [& args]
  (let [args (set args)
        screen-type (cond
                      (args ":swing") :swing
                      (args ":text") :text
                      :else :auto)]
    (run screen-type true)))
