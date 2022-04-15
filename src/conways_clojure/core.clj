(ns conways-clojure.core
  (:use clojure.pprint)
  (:require [quil.core :as q :include-macros true])
  (:require [quil.middleware :as m]))

;; length of each side of state
(def side-length 40)

;; vector of random t/f values
(defn random-row []
  (vec (repeatedly side-length #(= (rand-int 2) 0))))

;; initial (randomized) state
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
(defn get-cell [board i j]
  (nth (nth board i []) j false))

;; get all neighbors of a cell at coordinates i, j
(defn get-neighbors [board i j]
  (vector
    (get-cell board (- i 1) (- j 1))
    (get-cell board (- i 1) j)
    (get-cell board (- i 1) (+ j 1))
    (get-cell board i (- j 1))
    (get-cell board i (+ j 1))
    (get-cell board (+ i 1) (- j 1))
    (get-cell board (+ i 1) j)
    (get-cell board (+ i 1) (+ j 1))))

;; cycle one evolution of the board, calculating liveliness of each cell
(defn evolve-state [state]
  (vec (for [i (range (count state))]
         (vec (for [j (range (count (nth state i)))]
                (evolve-cell (get-cell state i j) (get-neighbors state i j)))))))

(defn multiplier [idx]
  (int (/ idx side-length)))

(defn coordinate [cell-size idx]
  (* cell-size (- idx (* (multiplier idx) side-length))))

;; draw the initial state
(defn draw-state [state]
  (q/background 240)
  (let [c-size (/ (q/width) side-length)]
    (doseq [[row-idx row] (map-indexed vector state)]
      (let [x (coordinate c-size row-idx)]
        (doseq [[col-idx cell] (map-indexed vector row)]
          (let [y (coordinate c-size col-idx)]
            (q/fill (if cell 0 255))
            (q/rect x y c-size c-size)))))))

(defn setup []
  (q/frame-rate 10)
  (q/color-mode :hsb)
  (q/no-stroke)
  state)

(q/defsketch conways-clojure.core
             :host "host"
             :size [500 500]
             :setup setup
             :update evolve-state
             :draw draw-state
             :middleware [m/fun-mode])

(defn -main [& args])
