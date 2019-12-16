(ns rivershy
    (:require [clojure.core.async :as a]))

(def c1 (a/chan 10))
(def c2 (a/chan 10))
(def cr (a/chan 10))

(a/onto-chan c1 ["a" "1" "c" "d" "q"])
(a/onto-chan c2 ["a" "b" "c" "d" "e"])

(defn eq-chans [a b r]
    (a/go-loop []
        (let [av (a/<! a)
            bv (a/<! b)]
            (when (= av bv)
                (a/>! r [av bv])
                (recur)
            )
        )
    )
)

(defn consumer []
    (a/take! cr (fn [x] (println (str "received" x)) (consumer))))
  
(eq-chans c1 c2 cr)
  
(consumer)