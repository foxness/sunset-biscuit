(ns rivershy
    (:require [clojure.core.async :as a]))

(def c1 (a/chan 10))
(def c2 (a/chan 10))
(def cr (a/chan 10))

(a/onto-chan c1 ["a" "1" "c" "d" "q"])
(a/onto-chan c2 ["a" "b" "c" "d" "e"])

; (defn eq-chans-2 [x]
;    (when (pos? x)
;    (a/put! c x (fn [_] (producer (dec x))))) )

; (defn eq-chans [chans r]
;     (a/go-loop []
;         (let [vals (map (fn [x] (a/<! x) chans))]
;             (when (apply = vals)
;                 (a/>! r (first vals))
;             )
;             (recur)
;         )
;     )
; )

; (defn eq-chans [a b r]
;     (a/go-loop []
;         (let [av (a/<! a)
;             bv (a/<! b)]
;             (when (or (some? av) (some? bv))
;                 (when (= av bv)
;                     (a/>! r [av bv])
;                 )
;                 (recur)
;             )
;         )
;     )
; )

; (ns clonure-lab.lec14
;     (:require [promesa.core :as p])
;     (:require [clojure.core.async :as a])
;     (:import (java.util.concurrent ArrayBlockingQueue)))
  ;
  ;(def c (ArrayBlockingQueue. 10))
  ;
  ;(defn producer [x]
  ;  (.start (Thread. (fn [] (loop [x x] (when (> x 0)
  ;                                        (.put c x) (recur (dec x))))))))
  ;
  ;(defn consumer []
  ;  (.start (Thread. (loop [ (let [v (.take c)]
  ;                             (println "resieved" v) (when v
  ;                                                      (consumer)))]))))
  ;
  ;(producer 10)
  ;(consumer)
  
  
  
  ;(def c (a/chan 10))
  ;(defn producer [x]
  ;  ;(when (pos? x)
  ;  ;(a/put! c x (fn [_] (producer (dec x))))) )
  ;  (a/go (loop [x x]
  ;          (if (pos? x)
  ;            (do (a/>! c x)
  ;                (recur (dec x)))
  ;            (a/close! c)
  ;            ))))
  ;
  ;
  ;;(defn consumer [name]
  ;;  (a/take! c (fn [x] (println (str name "received" x)) (consumer name))))
  ;(defn consumer [name]
  ;  (a/go-loop []
  ;    (when-some [value (a/<! c)]
  ;    (println name "received " value)
  ;    (recur))))
  
  
  ;(consumer "lol1")
  ;(consumer "lol2")
  ;(consumer "lol3")
  ;(producer 10)
  
  
  ;(a/take! (consumer "ddd") (fn [c] (println "consumer finished" c)))
  
  
;   (def c1 (a/chan 10))
;   (def c2 (a/chan 10))
;   (def cr (a/chan 10))
  
;   (a/onto-chan c1 (range 1 10))
;   (a/onto-chan c2 ["a" "b" "c" "d" "e"])
  
  ;(defn zip-chans [a b r]
  ;  (a/go-loop []
  ;    (let [av (a/<! a)
  ;          bv (a/<! b)]
  ;      (when (and (some? av) (some? bv))
  ;        (a/>! r [av bv])
  ;        (recur)
  ;        )
  ;      )
  ;    )
  ;  )
  ;
  ;(zip-chans c1 c2 cr)
  ;
  ;(defn consumer [name]
  ;  (a/go-loop []
  ;    (when-some [value (a/<! cr)]
  ;      (println name "received " value)
  ;      (recur))))
  ;
  ;(consumer "qwrt")
  
  
  
;   (a/go-loop []
;     (when-some [[rr cc] (a/alts! [c1 c2] :priority true)]
;       (println "received " rr cc)
;       (recur))
;     )

; (defn eq-chans [chans r]
;     (let [vals (map (fn [x] (a/take! x)) chans)]
;         (when (apply = vals)
;             (a/put! r (first vals) (fn [_] (eq-chans (chans r))))
;         )
;     )
; )

(defn eq-chans [chans r]
    (println (count chans))
    (a/go-loop []
        (with-local-vars [vals []]
            (for [chan chans]
                (var-set vals (conj vals (a/<! chan)))
                (println (str "vals: " vals))
            )
            ; (def vals (map (fn [x] (a/<! x)) chans))
            (when (every? some? vals)
                (println (str "vals: " vals))
                (when (apply = vals)
                    (a/>! r (first vals))
                )
                (recur)
            )
        )
    )
)

(defn consumer []
    (a/take! cr (fn [x] (println (str "received" x)) (consumer))))
  
(eq-chans [c1 c2] cr)
  
(consumer)