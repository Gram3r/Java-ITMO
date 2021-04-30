(use '[clojure.string :only (index-of join)])

(defn checkVSSize? [v] (and
                         (every? vector? v)
                         (or (empty? v)
                             (apply == (mapv count v)))))

(defn isV? [v] (and (vector? v) (every? number? v)))

(defn isM? [m] (and (vector? m) (every? isV? m) (checkVSSize? m)))

; :NOTE: Упростить
(defn isT? [t] (or
                 (number? t)
                 (and (isV? t))
                 (and (not (empty? t)) (every? isT? t) (checkVSSize? t))))

(defn op [f check] (fn [& args]
                     {:pre [(checkVSSize? args) (every? check args)]}
                     (apply mapv f args)))

(defn *s [f check] (fn [x & scals]
                     {:pre [(check x) (every? number? scals)]}
                     (let [sc (apply * scals)]
                       mapv #(f % sc) x)))

(defn v-op [f] (op f isV?))
(defn m-op [f] (op f isM?))

; :NOTE: Дубли
(def v+ (v-op +))
(def v- (v-op -))
(def v* (v-op *))
(def vd (v-op /))

(def m+ (m-op v+))
(def m- (m-op v-))
(def m* (m-op v*))
(def md (m-op vd))

(def v*s (*s * isV?))
(def m*s (*s v*s isM?))

(defn scalar [& v]
  {:pre [(every? isV? v) (checkVSSize? v)]}
  (apply + (apply mapv * v)))

(defn vect [& v]
  {:pre [(every? isV? v) (every? #(== (count %) 3) v)]}
  (reduce (fn [a b] (letfn [(*coord [v1 v2] (- (* (nth a v1) (nth b v2)) (* (nth a v2) (nth b v1))))]
                      (vector (*coord 1 2) (*coord 2 0) (*coord 0 1)))) v))

(defn transpose [m]
  {:pre [(isM? m)]}
  (apply mapv vector m))

(defn m*v [m v]
  {:pre [(isM? m) (isV? v) (== (count (first m)) (count v))]}
  (mapv #(scalar % v) m))

(defn m*m [& ms]
  {:pre [(every? isM? ms)]}
  (reduce (fn [a b] (transpose (mapv #(m*v a %) (transpose b)))) ms))

; :NOTE: Названия
(defn get-shape [t]
  (if (or (number? t) (nil? t))
    ()
    (cons (count t) (get-shape (first t)))))

(defn max-shape [ts] (apply max-key count (mapv get-shape ts)))

; :NOTE: преобразование в строку
(defn prefix? [max-sh] (fn [t]
                         (let [shape (get-shape t)
                               min (min (count shape) (count max-sh))]
                              (= (take min max-sh) (take min shape)))))


(defn broadcast [ts]
  {:pre [(apply = (mapv (prefix? (max-shape ts)) ts))]}
  (letfn [(castNum [num sh] (if (> (count sh) 0)
                              (apply vector (repeat (first sh) (castNum num (rest sh))))
                              num))
          (castTen [t sh] (if (number? t)
                            (castNum t sh)
                            (mapv #(castTen % (rest sh)) t)))]
    (mapv #(castTen % (max-shape ts)) ts)))

(defn tbop [f]
  (letfn [(downOp [& args]
            (if (every? number? args)
              (apply f args)
              (apply mapv downOp args)))]
    (fn [& ts]
      {:pre [(every? isT? ts)]}
      (apply downOp (broadcast ts)))))

(def tb+ (tbop +))
(def tb- (tbop -))
(def tb* (tbop *))
(def tbd (tbop /))