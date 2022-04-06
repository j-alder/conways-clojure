(ns conways-clojure.core-test
  (:require [clojure.test :refer :all]
            [conways-clojure.core :refer :all]))

(deftest test-dimensions
  (testing "Board does not have proper dimensions"
    (let [board (initial-board (vector))]
      (is (= (count board) 10)))))

(deftest test-live-evolution
  (let [board (initial-board #{[3 3] [2 2] [3 2] [0 0] [1 0]})]
    (let [v2 (evolve board)]
      (testing "Live cell died when it should not have"
        (is (= (get-cell v2 3 3) true))
        (is (= (get-cell v2 2 2) true))
        (is (= (get-cell v2 3 2) true)))
      (testing "Live cell stayed alive when it should have died"
        (is (= (get-cell v2 0 0) false))
        (is (= (get-cell v2 1 0) false))))))