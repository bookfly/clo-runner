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
                                            (assoc entity :x 0))))
(defn move-box [entity]
   (assoc entity :x (- (:x entity) 3)))

(defn move-boxes [boxes]
  (vec (map move-box boxes)))

(defn moving [entities]    
      (let [backgr (first entities)
            player (last entities)
            boxes (move-boxes (vec (drop 1 (drop-last entities))))]
      
     ;;   (->
     ;;     (conj [] backgr)
     ;;     (apply conj boxes)
    ;;      (conj player))
       (conj (apply conj (conj [] backgr) boxes) player)))


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
          box-image (assoc box :x 100 :y 100 :width 59 :height 59)
          box-image2 (assoc box :x 100 :y 400 :width 59 :height 59)
          box-image3 (assoc box :x 250 :y 100 :width 59 :height 59)
          box-image4 (assoc box :x 500 :y 300 :width 59 :height 59)
          
          
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
      [background box-image box-image2 box-image3 box-image4 player-image]))
     ;;[background box-image player-image]))
  
  
  :on-render
  (fn [screen entities]
    (clear!)
   ;; (merge (first entities) (animation->texture screen ((first entities) :moving)))
    (render! screen (moving entities))) 
  
    
 ;;dodavanje na on-timer 
  

  
  :on-key-down
  (fn [screen entities]
    (->
      (drop-last entities)
      (vec)
      (conj (move screen (last entities)))))
)
        

(defgame clo-runner-game
  :on-create
  (fn [this]
    (set-screen! this main-screen)))