(defn checkVSSize? [v] (and
                         (every? vector? v)
                         (or (empty? v)
                             (apply == (mapv count v)))))

(defn is_vector? [v] (and (vector? v) (every? number? v)))

(defn is_matrix? [m] (and (vector? m) (every? is_vector? m) (checkVSSize? m)))

(defn get-shape [t]
  (if (or (number? t) (nil? t))
    ()
    (cons (count t) (get-shape (first t)))))
; :NOTE: Исравить
(defn is_tensor? [t] (or
                       (number? t)
                       (and (vector? t)
                            (or (empty? t)
                                (and (every? is_tensor? t) (apply = (mapv get-shape t)))))))

(defn op [f check] (fn [& args]
                     {:pre [(checkVSSize? args) (every? check args)]}
                     (apply mapv f args)))

(defn *s [f check] (fn [x & scals]
                     {:pre [(check x) (every? number? scals)]}
                     (let [sc (apply * scals)]
                       mapv #(f % sc) x)))

(defn v-op [f] (op f is_vector?))
(defn m-op [f] (op f is_matrix?))

; :NOTE: Дубли
(def v+ (v-op +))
(def v- (v-op -))
(def v* (v-op *))
(def vd (v-op /))

(def m+ (m-op v+))
(def m- (m-op v-))
(def m* (m-op v*))
(def md (m-op vd))

(def v*s (*s * is_vector?))
(def m*s (*s v*s is_matrix?))

(defn scalar [& v]
  {:pre [(every? is_vector? v) (checkVSSize? v)]}
  (apply + (apply mapv * v)))

(defn vect [& v]
  {:pre [(every? is_vector? v) (every? #(== (count %) 3) v)]}
  (reduce (fn [a b] (letfn [(*coord [v1 v2] (- (* (nth a v1) (nth b v2)) (* (nth a v2) (nth b v1))))]
                      (vector (*coord 1 2) (*coord 2 0) (*coord 0 1)))) v))

(defn transpose [m]
  {:pre [(is_matrix? m)]}
  (apply mapv vector m))

(defn m*v [m v]
  {:pre [(is_matrix? m) (is_vector? v) (== (count (first m)) (count v))]}
  (mapv #(scalar % v) m))

(defn m*m [& ms]
  {:pre [(every? is_matrix? ms)]}
  (reduce (fn [a b] (transpose (mapv #(m*v a %) (transpose b)))) ms))


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
      {:pre [(every? is_tensor? ts)]}
      (apply downOp (broadcast ts)))))

(def tb+ (tbop +))
(def tb- (tbop -))
(def tb* (tbop *))
(def tbd (tbop /))