(ns clo-runner.core.desktop-launcher
  (:require [clo-runner.core :refer :all])
  (:import [com.badlogic.gdx.backends.lwjgl LwjglApplication]
           [org.lwjgl.input Keyboard])
  (:gen-class))

(defn -main
  []
  (LwjglApplication. clo-runner-game "clo-runner" 800 600)
  (Keyboard/enableRepeatEvents true))
