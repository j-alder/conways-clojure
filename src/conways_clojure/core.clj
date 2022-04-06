(ns conways-clojure.core
  (:use seesaw.core)
  (:use clojure.pprint))

;Any live cell with two or three live neighbours survives.
;Any dead cell with three live neighbours becomes a live cell.
;All other live cells die in the next generation. Similarly, all other dead cells stay dead.

; create the starting board with initial live cell coordinates
; set live cells to true, dead cells to false
(defn initial-board [coords]
  (vec
    (for [i (range 10)]
      (vec
        (for [j (range 10)]
          (if (contains? coords [i j]) true false))))))

; get number of live cells in a collection
(defn count-live [neighbors]
  (count (filter true? neighbors)))

; apply rules to individual cell and mutate appropriately
(defn determine-fate [cell neighbors]
  (let [live-neighbors (count-live neighbors)]
    (if (= cell true)
      ; any live cell with two or three live neighbors survives (else dies)
      (or (= live-neighbors 2) (= live-neighbors 3))
      ; any dead cell with three live neighbors reanimates (else stays dead)
      (or (= live-neighbors 3)))))

; get the value of a cell at a given coordinate in a board
(defn get-cell [board i j]
  (nth (nth board i []) j false))

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

(defn evolve [board]
  (vec
    (for [i (range (count board))]
      (vec
        (for [j (range (count (nth board i)))]
          (determine-fate (get-cell board i j) (get-neighbors board i j)))))))

(defn -main [& args]
  (let [b (initial-board #{[3 3] [2 2] [3 2] [5 5] [5 4] [5 3]})]
    (pprint b)
    (let [c (evolve b)]
      (pprint c))))

;(defn -main [& args]
;  (invoke-later
;    (-> (frame :title "Conway's Clojure",
;               :content content,
;               :on-close :exit)
;        pack!
;        show!)))
