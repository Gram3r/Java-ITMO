(def p println)
(defn v+ [& vectors] (apply mapv + vectors))
(defn v- [& vectors] (apply mapv - vectors))
(defn v* [& vectors] (apply mapv * vectors))
(defn vd [& vectors] (apply mapv / vectors))

(defn scalar [& vectors] (apply + (apply mapv * vectors)))
;(defn vect [])
(defn v*s [vector & scalars] (mapv (fn [x] (* x (apply * scalars))) vector))

(defn m+ [& vectors] (apply mapv v+ vectors))
(defn m- [& vectors] (apply mapv v- vectors))
(defn m* [& vectors] (apply mapv v* vectors))
(defn md [& vectors] (apply mapv vd vectors))

(defn transpose [matrix] (apply mapv vector matrix))
(defn m*s [matrix & scalars] (mapv (fn [vector] (v*s vector (apply * scalars))) matrix))
(defn m*v [matrix vector] (mapv (fn [vec] (scalar vec vector)) matrix))
(defn m*m [& matrixes] (reduce (fn [a b] (transpose (mapv (fn [x] (m*v a x)) (transpose b)))) matrixes))


(p (m*m [[1 2 3 4]
         [1 6 3 1]
         [2 3 5 1]]
        [[1 2 1]
         [0 4 2]
         [3 4 3]
         [1 1 4]]
        [[1 26 3]
         [4 5 13]
         [1 7 3]]
        [[1 2 3]
         [2 3 4]
         [3 4 5]]))
