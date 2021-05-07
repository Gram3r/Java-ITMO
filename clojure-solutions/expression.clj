(defn div [a b] (/ (double a) (double b)))
(defn double_divide ([arg] (/ (double arg)))
                    ([frst & rst] (reduce (fn [a b] (/ (double a) (double b))) frst rst)))

(defn constant [val] (constantly val))

(defn variable [name] #(get % name))

(defn operation [op] (fn [& args] (fn [vars] (apply op (mapv #(% vars) args)))))

(def add (operation +))
(def subtract (operation -))
(def multiply (operation *))
(def divide (operation double_divide))
(def negate subtract)

(def operator_func_Tokens {
                           '+ add
                           '- subtract
                           '* multiply
                           '/ divide
                           'negate negate})

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
