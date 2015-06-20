(ns clo-runner.core
  (:require [play-clj.core :refer :all]
            [play-clj.g2d :refer :all]))


(defn move [screen entity]
  (cond
    (= (:key screen) (key-code :dpad-up)) (if (<
                                                (+ (:y entity) 30) 
                                                (- 600 (:height entity)))
                                            (assoc entity :y (+ (:y entity) 30))
                                            (assoc entity :y  (- 600 (:height entity))))              
    (= (:key screen) (key-code :dpad-down)) (if (>
                                                (- (:y entity) 30) 
                                                0)
                                            (assoc entity :y (- (:y entity) 30))
                                            (assoc entity :y 0))         
    (= (:key screen) (key-code :dpad-right)) (if (<
                                                (+ (:x entity) 30) 
                                                (- 800 (:width entity)))
                                            (assoc entity :x (+ (:x entity) 30))
                                            (assoc entity :x  (- 800 (:width entity))))                 
    (= (:key screen) (key-code :dpad-left)) (if (>
                                                (- (:x entity) 30) 
                                                0)
                                            (assoc entity :x (- (:x entity) 30))
                                            (assoc entity :x 0))    ))
  

(defscreen main-screen
  :on-show
  (fn [screen entities]
    (update! screen :renderer (stage))
    (let [
          ; load a sprite sheet from your resources dir
          sheet (texture "bird.png")
          birdy (texture! sheet :split (/ 506 3) (/ 918 5))
          
          background (texture "background.jpg")
          background (assoc background :x 0 :y 0 :width 800 :height 600)
          
          box (texture "box.jpg")
          
          
          player-image (texture (aget birdy 0 0))
          ;;images (->
            ;;       []
              ;;     (conj (texture (aget birdy 0 0)))
                ;;   (conj (texture (aget birdy 0 1)))
                ;;   (conj (texture (aget birdy 0 2)))                   
                ;;   (conj (texture (aget birdy 0 3)))
                ;;   (conj (texture (aget birdy 0 4)))
                ;;   (conj (texture (aget birdy 1 0)))
                ;;   (conj (texture (aget birdy 1 1)))
                ;;   (conj (texture (aget birdy 1 2)))                  
                ;;   (conj (texture (aget birdy 1 3)))
                ;;   (conj (texture (aget birdy 1 4))))
     ;;     player-image (assoc player-image :moving (animation 0.15 images))
          player-image (assoc player-image :x 10 :y 10 :width 59 :height 59)]
      ; return a new entities vector with player-image inside of it
      [background player-image]))
  
  
  :on-render
  (fn [screen entities]
    (clear!)
   ;; (merge (first entities) (animation->texture screen ((first entities) :moving)))
    (render! screen entities)) 
  
  :on-key-down
  (fn [screen entities]
    (->
      (drop-last entities)
      (vec)
      (conj (move screen (last entities))))))
        

(defgame clo-runner-game
  :on-create
  (fn [this]
    (set-screen! this main-screen)))
