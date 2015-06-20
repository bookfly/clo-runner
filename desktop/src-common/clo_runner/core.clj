(ns clo-runner.core
  (:require [play-clj.core :refer :all]
            [play-clj.g2d :refer :all]))


(defn move [entity direction]
  (case direction
    :down (assoc entity :y (dec (:y entity)))
    :up (assoc entity :y (inc (:y entity)))
    :right (assoc entity :x (inc (:x entity)))
    :left (assoc entity :x (dec (:x entity)))
    nil))

(defscreen main-screen
  :on-show
  (fn [screen entities]
    (update! screen :renderer (stage))
    (let [
          ; load a sprite sheet from your resources dir
          sheet (texture "bird.png")
          birdy (texture! sheet :split (/ 506 3) (/ 918 5))
          player-image (texture (aget birdy 1 1))
          player-image (assoc player-image :x 10 :y 10 :width 59 :height 59)]
      ; return a new entities vector with player-image inside of it
      [player-image]))
  
  
  :on-render
  (fn [screen entities]
    (clear!)
    (render! screen entities)) 
  
  :on-key-down
  (fn [screen entities]
    (cond
      (= (:key screen) (key-code :dpad-up))
      (move (first entities) :up)
      (= (:key screen) (key-code :dpad-down))
      (move (first entities) :down)
      (= (:key screen) (key-code :dpad-right))
      (move (first entities) :right)
      (= (:key screen) (key-code :dpad-left))
      (move (first entities) :left))))

(defgame clo-runner-game
  :on-create
  (fn [this]
    (set-screen! this main-screen)))
