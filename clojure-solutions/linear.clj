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
                 (and (> (count t) 0) (isV? t))
                 (and (> (count t) 0) (every? isT? t) (checkVSSize? t))))

(defn op [f check] (fn [& args]
                     {:pre [(checkVSSize? args) (every? check args)]}
                     (apply mapv f args)))

(defn *s [f check] (fn [x & scals]
                     {:pre [(check x) (every? number? scals)]}
                     (let [sc (apply * scals)]
                       mapv #(f % sc) x)))

; :NOTE: Дубли
(def v+ (op + isV?))
(def v- (op - isV?))
(def v* (op * isV?))
(def vd (op / isV?))

(def m+ (op v+ isM?))
(def m- (op v- isM?))
(def m* (op v* isM?))
(def md (op vd isM?))

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
(defn getSh [t]
  (if (number? t)
    ()
    (cons (count t) (getSh (first t)))))

(defn maxSh [ts] (apply max-key count (mapv getSh ts)))

; :NOTE: преобразование в строку
(defn prefix? [str] (fn [t] (let [ind (index-of str (join " " (getSh t)))]
                              (and (= ind 0) (not (nil? ind))))))

(defn broadcast [ts]
  {:pre [(apply = (mapv (prefix? (join " " (maxSh ts))) ts))]}
  (letfn [(castNum [num sh] (if (> (count sh) 0)
                              (apply vector (repeat (first sh) (castNum num (rest sh))))
                              num))
          (castTen [t sh] (if (number? t)
                            (castNum t sh)
                            (mapv #(castTen % (rest sh)) t)))]
    (mapv #(castTen % (maxSh ts)) ts)))

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