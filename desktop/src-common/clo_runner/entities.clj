(ns clo-runner.entities
  (:require [play-clj.core :refer :all]
            [play-clj.g2d :refer :all]))

(defn create
  [moving]
  (assoc moving
         :moving (animation 0.15
                               (map #(texture % :flip true false) moving)
                               :set-play-mode (play-mode :loop-pingpong))))