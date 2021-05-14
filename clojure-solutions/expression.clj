(use '[clojure.string :only (join)])
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

(def evaluate (method :evaluate))
(def toString (method :toString))
(def diff (method :diff))

(defn expression_prototype [evaluate toString diff]
  {:evaluate evaluate
   :toString toString
   :diff diff})

(declare Zero)
(def Constant (let [_val (field :val)]
                (constructor
                     (fn [this val] (assoc this :val val))
                     (expression_prototype
                       (fn [this _] (_val this))
                       (fn [this] (format "%.1f" (_val this)))
                       (fn [_ _] Zero)))))
(def Zero (Constant 0))
(def One (Constant 1))

(def Variable (let [_name (field :name)]
                (constructor
                  (fn [this name] (assoc this :name name))
                  (expression_prototype
                     (fn [this vars] (vars (_name this)))
                     _name
                     (fn [this parameter] (if (= (_name this) parameter)
                                            One
                                            Zero))))))

(def operation_prototype (let [_f (field :f)
                               _symbol (field :symbol)
                               _diff_rule (field :diff_rule)
                               _args (field :args)]
                           (expression_prototype
                             (fn [this vars] (apply (_f this) (mapv #(evaluate % vars) (_args this))))
                             (fn [this]
                               (str "(" (_symbol this) " " (join " " (mapv toString (_args this))) ")"))
                             (fn [this parameter]
                               ((_diff_rule this) (_args this) (mapv #(diff % parameter) (_args this)))))))


(defn Operation [f symbol diff_rule]
  (constructor
    (fn [this & args] (assoc this :args (vec args)))
    {:prototype operation_prototype
     :f f
     :symbol symbol
     :diff_rule diff_rule}))

(def Negate (Operation
              -
              'negate
              (fn [_ diff_args] (Negate (first diff_args)))))

(def Add (Operation
           +
           '+
           (fn [_ diff_args] (apply Add diff_args))))

(def Subtract (Operation
                -
                '-
                (fn [_ diff_args] (apply Subtract diff_args))))

(declare Multiply)
(defn diff_mul [args diff_args] (second
                                  (reduce
                                    (fn [[f fd] [s sd]] [(Multiply f s)
                                                         (Add (Multiply fd s) (Multiply f sd))])
                                    (mapv vector args diff_args))))
(def Multiply (Operation
                *
                '*
                diff_mul))

(defn Square [a] (Multiply a a))

(def Divide (Operation
              double_divide
              '/
              (fn [args diff_args]
                (if (empty? (rest args))
                  (Negate
                   (Divide
                     (first diff_args)
                     (Square (first args))))
                  (let [mul (apply Multiply (rest args))]
                    (Divide
                      (Subtract
                         (Multiply (first diff_args) mul)
                         (Multiply (first args) (diff_mul (rest args) (rest diff_args))))
                      (Square mul)))))))

(def ArithMean (Operation
                 (fn [& args] (/ (apply + args) (count args)))
                 'arith-mean
                 (fn [args diff_args] (Divide
                                        (apply Add diff_args)
                                        (Constant (count args))))))

(def GeomMean (Operation
                (fn [& args] (Math/pow (Math/abs (apply * args)) (/ 1.0 (double (count args)))))
                'geom-mean
                (fn [args diff_args] (Multiply
                                       (Constant (/ 1.0 (count args)))
                                       (apply GeomMean args)
                                       (apply Add (mapv (fn [[a ad]] (Divide ad a)) (mapv vector args diff_args)))))))

(def HarmMean (Operation
                (fn [& args] (double_divide (count args) (apply + (mapv #(/ 1.0 %) args))))
                'harm-mean
                (fn [args diff_args] (Multiply
                                       (Square (apply HarmMean args))
                                       (apply ArithMean (mapv (fn [[a ad]] (Divide ad (Square a))) (mapv vector args diff_args)))))))

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

(def operator_obj_Tokens {
                          '+ Add
                          '- Subtract
                          '* Multiply
                          '/ Divide
                          'negate Negate
                          'arith-mean ArithMean
                          'geom-mean GeomMean
                          'harm-mean HarmMean})

(def variable_obj_Tokens {
                          'x (Variable "x")
                          'y (Variable "y")
                          'z (Variable "z")})

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
(def parseObject (parseAll operator_obj_Tokens Constant variable_obj_Tokens))

