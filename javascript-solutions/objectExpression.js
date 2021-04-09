"use strict";

function fabricForOperator(constructorEx, toString, evaluate, diff, simplify) {
    constructorEx.prototype.toString = toString;
    constructorEx.prototype.evaluate = evaluate;
    constructorEx.prototype.diff = diff;
    constructorEx.prototype.simplify = simplify;
    return constructorEx
}

function fabricForOperations(func, symbol, operDiffer, operSimplify) {
    let constructorOp = function(...args) {
        Operator.call(this, ...args)
    }
    constructorOp.prototype = Object.create(Operator.prototype);
    constructorOp.prototype.func = func;
    constructorOp.prototype.symbol = symbol;
    constructorOp.prototype.operDiffer = operDiffer;
    constructorOp.prototype.operSimplify = operSimplify;
    return constructorOp
}

const Const = fabricForOperator(
    function(value) {this.value = value},
    function() {return `${this.value}`;},
    function() {return this.value;},
    function(parameter) {return zero},
    function() {return this}
)



const Variable = fabricForOperator(
    function(str) {this.str = str; this.ind  = variableTokens.get(str)},
    function() {return this.str;},
    function(...args) {return args[this.ind];},
    function(parameter) {return parameter === this.str ? one : zero},
    function() {return this}
)


const Operator = fabricForOperator(
    function(...args) {this.args = args},
    function() {
        return this.args.join(" ") + ` ${this.symbol}`;
    },
    function(...args) {
        return this.func(...this.args.map(value => value.evaluate(...args)))
    },
    function(parameter) {
        return this.operDiffer(parameter, ...this.args)
    },
    function() {
        let innerSimplified = this.args.map(value => value.simplify())
        if (innerSimplified.every(function(op) {return op instanceof Const})) {
            return new Const(this.func(...innerSimplified.map(value => value.evaluate())));
        } else {
            return this.operSimplify(...innerSimplified)
        }
    })



const Add = fabricForOperations(
    (a, b) => a + b,
    "+",
    (parameter, firstOp, secondOp) => new Add(firstOp.diff(parameter), secondOp.diff(parameter)),
    (firstOp, secondOp) => {
        if (firstOp.toString() === "0") {
            return secondOp;
        } else if (secondOp.toString() === "0") {
            return firstOp;
        } else {
            return new Add(firstOp, secondOp)
        }
    })



const Subtract = fabricForOperations(
    (a, b) => a - b,
    "-",
    (parameter, firstOp, secondOp) => new Subtract(firstOp.diff(parameter), secondOp.diff(parameter)),
    (firstOp, secondOp) => {
        if (firstOp.toString() === "0") {
            return new Negate(secondOp);
        } else if (secondOp.toString() === "0") {
            return firstOp;
        } else {
            return new Subtract(firstOp, secondOp)
        }
    })




const Multiply = fabricForOperations(
    (a, b) => a * b,
    "*",
    (parameter, firstOp, secondOp) => new Add(
        new Multiply(firstOp.diff(parameter), secondOp),
        new Multiply(firstOp, secondOp.diff(parameter))),
    (firstOp, secondOp) => {
        if (firstOp.toString() === "0" || secondOp.toString() === "0") {
            return zero;
        } else if (firstOp.toString() === "1") {
            return secondOp;
        } else if (secondOp.toString() === "1") {
            return firstOp;
        } else {
            return new Multiply(firstOp, secondOp)
        }
    })




const Divide = fabricForOperations(
    (a, b) => a / b,
    "/",
    (parameter, firstOp, secondOp) => new Divide(
        new Subtract(
            new Multiply(firstOp.diff(parameter), secondOp),
            new Multiply(firstOp, secondOp.diff(parameter))),
        new Multiply(secondOp, secondOp)
    ),
    (firstOp, secondOp) => {
        if (secondOp instanceof Multiply) {
            if (secondOp.args[0].toString() === firstOp.toString()) {
                return new Divide(one, secondOp.args[1])
            }
            if (secondOp.args[1].toString() === firstOp.toString()) {
                return new Divide(one, secondOp.args[0])
            }
        }
        if (firstOp.toString() === "0") {
            return zero;
        } else {
            return new Divide(firstOp, secondOp)
        }
    })




const Negate = fabricForOperations(
    (a) => -a,
    "negate",
    (parameter, oper) => new Negate(oper.diff(parameter)),
    (oper) => {
        if (oper.toString() === "0") {
            return zero
        } else {
            return new Negate(oper)
        }
    })




const HMean = fabricForOperations(
    (a, b) => 2 / (1 / a + 1 / b),
    "hmean",
    (parameter, firstOp, secondOp) => new Divide(
        new Multiply(
            new Const(2),
            new Multiply(firstOp, secondOp)),
        new Add(firstOp, secondOp)).diff(parameter),
    (firstOp, secondOp) => {
        if (firstOp.toString() === "0") {
            return zero;
        } else if (secondOp.toString() === "0") {
            return zero;
        } else {
            return new HMean (firstOp, secondOp)
        }
    })




const Hypot = fabricForOperations(
    (a, b) => a * a + b * b,
    "hypot",
    (parameter, firstOp, secondOp) => new Add(
        new Multiply(firstOp, firstOp),
        new Multiply(secondOp, secondOp)).diff(parameter),
    (firstOp, secondOp) => {
        if (firstOp.toString() === "0") {
            return secondOp;
        } else if (secondOp.toString() === "0") {
            return firstOp;
        } else {
            return new Hypot(firstOp, secondOp)
        }
    })


let zero = new Const(0)
let one = new Const(1)


const variableTokens = new Map([
    ["x", 0],
    ["y", 1],
    ["z", 2]
])


const constTokens = new Map([
    [0, zero],
    [1, one]
])


const operatorTokens = new Map([
    ["+", Add],
    ["-", Subtract],
    ["/", Divide],
    ["*", Multiply],
    ["negate", Negate],
    ["hypot", Hypot],
    ["hmean", HMean]
]);



function parse(str) {
    let exp = [];
    let stack = str.split(' ').filter(c => c !== "");

    function apply(i) {
        if (operatorTokens.has(i)) {
            let operator = operatorTokens.get(i)
            return new operator(... exp.splice(-operator.prototype.func.length))
        } else if (variableTokens.has(i)) {
            return new Variable(i)
        } else if (constTokens.has(i)) {
            return constTokens.get(i);
        } else {
            return new Const(Number(i))
        }
    }

    for (const i of stack) {
        exp.push(apply(i));
    }
    return exp[0]
}