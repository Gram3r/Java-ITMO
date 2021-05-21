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
(def toStringInfix (method :toStringInfix))
(def diff (method :diff))

(defn expression_prototype [evaluate toString diff & [toStringInfix]]
  {:evaluate evaluate
   :toString toString
   :diff diff
   :toStringInfix (or toStringInfix toString)})

(declare Zero)
(def Constant (let [_val (field :val)]
                (constructor
                     (fn [this val] (assoc this :val val))
                     (expression_prototype
                       (fn [this _] (_val this))
                       (fn [this] (str (_val this)))
                       (fn [_ _] Zero)))))
(def Zero (Constant 0))
(def One (Constant 1))

(def Variable (let [_name (field :name)]
                (constructor
                  (fn [this name] (assoc this :name name))
                  (expression_prototype
                     (fn [this vars] (vars (clojure.string/lower-case (str (first (_name this))))))
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
                               ((_diff_rule this) (_args this) (mapv #(diff % parameter) (_args this))))
                             (fn [this]
                               (if (== (count (_args this)) 1)
                                 (str (_symbol this) "(" (toStringInfix (first (_args this))) ")")
                                 (str "(" (clojure.string/join
                                                  (str " " (_symbol this) " ")
                                                  (mapv toStringInfix (_args this))) ")"))))))


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

(defn bit_f [op] (fn [& args] (apply op (mapv (fn [x] (if (> x 0) 1 0)) args))))

(defn bit-no [a] (if (> a 0) 0 1))

(def And (Operation
           (bit_f bit-and)
           '&&
           nil))

(def Or (Operation
          (bit_f bit-or)
          '||
          nil))

(def Xor (Operation
           (bit_f bit-xor)
           (symbol "^^")
           nil))

(def Impl (Operation
            (bit_f (fn [& args] (reduce (fn [a b] (bit-or (if (> a 0) 0 1) b)) args)))
            '->
            nil))

(def Iff (Operation
           (bit_f (fn [& args] (if (apply = args) 1 0)))
           '<->
           nil))

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
                          'harm-mean HarmMean
                          '&& And
                          '|| Or
                          (symbol "^^") Xor
                          '-> Impl
                          '<-> Iff})

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


;Start of combinatoric parser

(defn -return [value tail] {:value value :tail tail})
(def -valid? boolean)
(def -value :value)
(def -tail :tail)
(defn -show [result]
  (if (-valid? result) (str "-> " (pr-str (-value result)) " | " (pr-str (apply str (-tail result))))
                       "!"))

(defn tabulate [parser inputs]
  (run! (fn [input] (printf "    %-10s %s\n" (pr-str input) (-show (parser input)))) inputs))

(defn _empty [value] (partial -return value))
(defn _char [p]
  (fn [[c & cs]]
    (if (and c (p c))
      (-return c cs))))

(defn _map [f result]
  (if (-valid? result)
    (-return (f (-value result)) (-tail result))))

(defn _combine [f a b]
  (fn [input]
    (let [ar ((force a) input)]
      (if (-valid? ar)
        (_map (partial f (-value ar))
              ((force b) (-tail ar)))))))

(defn _either [a b]
  (fn [input]
    (let [ar ((force a) input)]
      (if (-valid? ar)
        ar
        ((force b) input)))))

(defn _parser [p]
  (let [pp (_combine (fn [v _] v) p (_char #{\u0000}))]
    (fn [input] (-value (pp (str input \u0000))))))

(defn +char [chars] (_char (set chars)))
(defn +char-not [chars] (_char (comp not (set chars))))
(defn +map [f parser] (comp (partial _map f) parser))
(def +parser _parser)
(def +ignore (partial +map (constantly 'ignore)))
(defn iconj [coll value] (if (= value 'ignore)
                           coll
                           (conj coll value)))
(defn +seq [& ps] (reduce (partial _combine iconj) (_empty []) ps))
(defn +seqf [f & ps] (+map (partial apply f) (apply +seq ps)))
(defn +seqn [n & ps] (apply +seqf #(nth %& n) ps))
(defn +or [p & ps] (reduce _either p ps))
(defn +opt [p] (+or p (_empty nil)))
(defn +star [p] (letfn [(rec [] (+or (+seqf cons p (delay (rec))) (_empty ())))]
                  (rec)))
(defn +plus [p] (+seqf cons p (+star p)))
(defn +str [p] (+map (partial apply str) p))
(defn +string [s]
  (apply +seqf str (mapv (fn [char] (+char (str char))) s)))

(def *digit (+char "0123456789"))
(def *space (+char " \t\n\r"))
(def *ws (+ignore (+star *space)))
(def *all-chars (mapv char (range 32 128)))
(def *letter (+char (apply str (filter #(Character/isLetter %) *all-chars))))


(def *number (+seqf
               (fn [sign digits dot digits_dot] (read-string (str sign (apply str digits) dot (apply str digits_dot))))
               (+opt (+char "-"))
               (+plus *digit)
               (+opt (+char "."))
               (+star *digit)))

(def *variable (+map Variable (+str (+plus *letter))))
(def *constant (+map Constant *number))
(defn +op [op] (+map (constantly (get operator_obj_Tokens (symbol op))) (+string op)))
(defn *operators [operators] (apply +or (mapv +op operators)))

(def *unary_ops (*operators  ["negate"]))

(declare *expression)
(def *expr_in_brackets (+seqn 1 (+char "(") (delay *expression) (+char ")")))
(defn apply_unary [[lst expr]]
  (letfn [(make_expr [lst]
            (if (empty? lst)
              expr
              ((first lst) (make_expr (rest lst)))))]
    (make_expr lst)))

(def *element (+map apply_unary
                    (+seq
                      *ws
                      (+star (+map first (+seq *unary_ops *ws)))
                      *ws
                      (+or *constant *variable *expr_in_brackets))))

(defn +level [+apply]
  (fn [*elem operators]
    (+map +apply (+map flatten (+seq *ws *elem *ws (+star (+seq (*operators operators) *ws *elem *ws)))))))

(defn +apply [f args]
  (reduce f (first args) (partition 2 (rest args))))

(defn left_apply [args]
  (+apply (fn [a [op b]] (op a b)) args))

(defn right_apply [args]
  (+apply (fn [a [op b]] (op b a)) (reverse args)))

(def +left_assoc
  (+level left_apply))

(def +right_assoc
  (+level right_apply))

(def ops [[+left_assoc ["*" "/"]]
          [+left_assoc ["+" "-"]]
          [+left_assoc ["&&"]]
          [+left_assoc ["||"]]
          [+left_assoc ["^^"]]
          [+right_assoc ["->"]]
          [+left_assoc ["<->"]]])

(def *expression
  (reduce (fn [*elem_res [_assoc operators]] (_assoc *elem_res operators)) *element ops))

(def parseObjectInfix (+parser *expression))