(load-file "proto.clj")

(defn double_divide ([arg] (/ 1 (double arg)))
                    ([frst & rst] (reduce (fn [a b] (/ (double a) (double b))) frst rst)))

(defn constant [val] (constantly val))

(defn variable [name] #(get % name))

(defn operation [f] (fn [& args] (fn [vars] (apply f (mapv #(% vars) args)))))

(def add (operation +))
(def subtract (operation -))
(def multiply (operation *))
(def divide (operation double_divide))
(def negate subtract)

(defn mean_val [& args] (/ (apply + args) (count args)))
(def mean (operation mean_val))

(defn square [x] (* x x))

(defn varn_val [& args] (- (apply mean_val (mapv square args))
                           (square (apply mean_val args))))
(def varn (operation varn_val))

(def operator_func_Tokens {
                           '+ add
                           '- subtract
                           '* multiply
                           '/ divide
                           'negate negate
                           'mean mean
                           'varn varn})

(def variable_func_Tokens {
                           'x (variable "x")
                           'y (variable "y")
                           'z (variable "z")})

(defn parseAll [op_token cnst var_token]
  (fn [expr]
    (letfn [(parse [token]
              (cond
                (list? token) (parseList token)
                (number? token) (cnst token)
                (contains? var_token token) (get var_token token)))
            (parseList [lst] (apply (get op_token (first lst))
                                    (mapv parse (rest lst))))]
      (parse (read-string expr)))))

(def parseFunction (parseAll operator_func_Tokens constant variable_func_Tokens))

(println (varn_val 2 5 11))
