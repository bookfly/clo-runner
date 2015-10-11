(ns clo-runner.core
  (:require [play-clj.core :refer :all]
            [play-clj.g2d :refer :all]))

(def player-speed 10)

(defn move [screen entity]
  (cond
    (= (:key screen) (key-code :dpad-up)) (if (<
                                                (+ (:y entity) player-speed) 
                                                (- 600 (:height entity)))
                                            (assoc entity :y (+ (:y entity) player-speed))
                                            (assoc entity :y  (- 600 (:height entity))))              
    (= (:key screen) (key-code :dpad-down)) (if (>
                                                (- (:y entity) player-speed) 
                                                0)
                                            (assoc entity :y (- (:y entity) player-speed))
                                            (assoc entity :y 0))         
    (= (:key screen) (key-code :dpad-right)) (if (<
                                                (+ (:x entity) player-speed) 
                                                (- 800 (:width entity)))
                                            (assoc entity :x (+ (:x entity) player-speed))
                                            (assoc entity :x  (- 800 (:width entity))))                 
    (= (:key screen) (key-code :dpad-left)) (if (>
                                                (- (:x entity) player-speed) 
                                                0)
                                            (assoc entity :x (- (:x entity) player-speed))
                                            (assoc entity :x 0))))


(defn create-box []  
  (assoc (texture "box.jpg") :x 859 :y (rand-int 500) :width 59 :height 59))

(defn move-box [entity] 
   (assoc entity :x (- (:x entity) 3)))

(defn move-boxes [boxes]
  (vec (map move-box boxes)))

(defn has-col [player box]
  (let [left-player (- (:x player) (/ (:width player) 2))
        right-player (+ (:x player) (/ (:width player) 2))        
        left-box (- (:x box) (/ (:width box) 2))
        right-box (+ (:x box) (/ (:width box) 2))        
        up-player (+ (:y player) (/ (:height player) 2))
        down-player (- (:y player) (/ (:height player) 2))        
        up-box (+ (:y box) (/ (:height box) 2))
        down-box (- (:y box) (/ (:height box) 2))]     
    
   
  (or  
  (and (> right-player  left-box) (< down-player up-box)
       (> up-player up-box) (< left-player left-box))
  (and (> right-player left-box) (> up-player down-box)
       (< down-player down-box) (< left-player left-box))  
   (and (< left-player right-box) (> up-player down-box)
        (< down-player down-box) (> right-player right-box))
   
  (and (< left-player right-box) (< down-player up-box)
       (> up-player up-box) (> right-player right-box)))))

(defn has-collide [player boxes]
  (let [collision false
        collision (loop [x (dec (count boxes))]
                    (when (>= x 0)
                      (if (has-col player (get boxes x))
                        true
                        (recur (dec x)))))]
    collision
    ))

(defn moving [entities]    
  (let [backgr (first entities)
        player (last entities)
        boxes (try              
                (move-boxes (vec (drop 1 (drop-last entities))))                
                (catch NullPointerException e
                  []))]
    (conj (apply conj (conj [] backgr) boxes) player)))

(defn collision [entities]
  (let [backgr (first entities)
        player (last entities)
        boxes (try              
                (vec (drop 1 (drop-last entities)))                
                (catch NullPointerException e
                  []))]
    (if (has-collide player boxes)
      (throw (Exception. "Game over"))
      (conj (apply conj (conj [] backgr) boxes) player))))

(defscreen main-screen
  :on-show
  (fn [screen entities]
    (update! screen :renderer (stage))
    (add-timer! screen :spawn-enemy 0 2)
    (let [
          sheet (texture "bird.png")
          birdy (texture! sheet :split (/ 506 3) (/ 918 5))
          
          background (texture "background.jpg")
          background (assoc background :x 0 :y 0 :width 800 :height 600)                
          
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
      [background player-image]))  
  
  :on-render
  (fn [screen entities]
    (clear!)
    (render! screen 
             (try
               (->
                 (moving entities)
                 (collision))
               (catch Exception e 
                 [(assoc (texture "gameover.jpg")
                        :x 0 :y 0 :width 800 :height 600)])))) 
       
  :on-timer
  (fn [screen entities]
    :spawn-enemy (conj (conj (vec (drop-last entities)) (create-box)) (last entities)))
  
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