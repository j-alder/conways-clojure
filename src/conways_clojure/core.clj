(ns conways-clojure.core
  (:use clojure.pprint)
  (:require [quil.core :as q :include-macros true])
  (:require [quil.middleware :as m]))

;; length of each side of state
(def side-length 100)

;; vector of random t/f values
(defn random-row []
  (vec (repeatedly side-length #(= (rand-int 3) 0))))

;; initial (randomized) state - 2 dimensional vector
(def state
  (vec (repeatedly side-length random-row)))

;; get number of live cells in a collection
(defn count-live [neighbors]
  (count (filter true? neighbors)))

;; apply rules to individual cell and return true or false
;; depending on outcome
(defn evolve-cell [cell neighbors]
  (let [live-neighbors (count-live neighbors)]
    (if cell
      ; any live cell with two or three live neighbors stays alive (else dies)
      (or (= live-neighbors 2) (= live-neighbors 3))
      ; any dead cell with three live neighbors becomes alive (else stays dead)
      (or (= live-neighbors 3)))))

;; get the value of a cell at a given coordinate in a board
(defn get-cell [board x y]
  (nth (nth board x []) y false))

;; get all neighbors of a cell at coordinates i, j
(defn get-neighbors [board x y]
  (vector
    (get-cell board (- x 1) (- y 1))
    (get-cell board (- x 1) y)
    (get-cell board (- x 1) (+ y 1))
    (get-cell board x (- y 1))
    (get-cell board x (+ y 1))
    (get-cell board (+ x 1) (- y 1))
    (get-cell board (+ x 1) y)
    (get-cell board (+ x 1) (+ y 1))))

;; cycle one evolution of the board, calculating liveliness of each cell
(defn evolve-state [state]
  (vec (for [i (range (count state))]
         (vec (for [j (range (count (nth state i)))]
                (evolve-cell (get-cell state i j) (get-neighbors state i j)))))))

(defn offset [idx]
  (if (= idx side-length) side-length 0))

(defn pos [cell-size idx]
  (* cell-size (- idx (offset idx))))

;; draw the initial state
(defn draw-state [state]
  (q/background 240)
  (let [cell-size (/ (q/width) side-length)]
    (doseq [[row-idx row] (map-indexed vector state)]
      (let [x (pos cell-size row-idx)]
        (doseq [[col-idx cell] (map-indexed vector row)]
          (let [y (pos cell-size col-idx)]
            (if cell (q/fill 253 246 227) (q/fill 0 43 54))
            (q/rect x y cell-size cell-size)))))))

(defn setup []
  (q/frame-rate 10)
  (q/no-stroke)
  state)

(q/defsketch conways-clojure.core
             :host "host"
             :title "Conway's Clojure"
             :size [800 800]
             :setup setup
             :update evolve-state
             :draw draw-state
             :middleware [m/fun-mode])

(defn -main [& args])
