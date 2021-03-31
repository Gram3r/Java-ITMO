"use strict";


function prototypeChanges(nameOfOperator, toString, evaluate, diff, simplify) {
    nameOfOperator.prototype.toString = toString;
    nameOfOperator.prototype.evaluate = evaluate;
    nameOfOperator.prototype.diff = diff;
    nameOfOperator.prototype.simplify = simplify;
    nameOfOperator.prototype.constructor = nameOfOperator
}

function prototypeChangesForOperator(nameOfOperator, func, symbol, operDiffer, operSimplify) {
    nameOfOperator.prototype = Object.create(Operator.prototype);
    nameOfOperator.prototype.func = func;
    nameOfOperator.prototype.symbol = symbol;
    nameOfOperator.prototype.operDiffer = operDiffer;
    nameOfOperator.prototype.operSimplify = operSimplify;
}




function Const(value) {
    this.value = value;
}
let zero = new Const(0)
let one = new Const(1)

prototypeChanges(Const,
    function() {return `${this.value}`;},
    function() {return this.value;},
    function(parameter) {return zero},
    function() {return this}
)




function Variable(name) {
    this.name = name
    this.index = variableTokens.get(name)
}

prototypeChanges(Variable,
    function() {return this.name;},
    function(...args) {return args[this.index];},
    function(parameter) {return parameter === this.name ? one : zero},
    function() {return this}
)





function Operator(...innerOperators) {
    this.inner = innerOperators;
}
prototypeChanges(Operator,
    function() {
        return this.inner.join(" ") + " " + this.symbol;
    },
    function(...args) {
        return this.func(...this.inner.map(value => value.evaluate(...args)))
    },
    function(parameter) {
        return this.operDiffer(parameter, ...this.inner)
    },
    function() {
        let innerSimplified = this.inner.map(value => value.simplify())
        if (innerSimplified.every(function(op) {return op instanceof Const})) {
            return new Const(this.func(...innerSimplified.map(value => value.evaluate())));
        } else {
            return this.operSimplify(...innerSimplified)
        }
    })



function Add(firstOp, secondOp) {
    Operator.call(this,
        firstOp,
        secondOp)
}
prototypeChangesForOperator(Add,
    (a, b) => a + b,
    "+",
    function(parameter, firstOp, secondOp) {return new Add(firstOp.diff(parameter), secondOp.diff(parameter))},
    function(firstOp, secondOp) {
        if (firstOp.toString() === "0") {
            return secondOp;
        } else if (secondOp.toString() === "0") {
            return firstOp;
        } else {
            return new Add(firstOp, secondOp)
        }
    })



function Subtract(firstOp, secondOp) {
    Operator.call(this,
        firstOp,
        secondOp)
}
prototypeChangesForOperator(Subtract,
    (a, b) => a - b,
    "-",
    function(parameter, firstOp, secondOp) {return new Subtract(firstOp.diff(parameter), secondOp.diff(parameter))},
    function(firstOp, secondOp) {
        if (firstOp.toString() === "0") {
            return new Negate(secondOp);
        } else if (secondOp.toString() === "0") {
            return firstOp;
        } else {
            return new Subtract(firstOp, secondOp)
        }
    })




function Multiply(firstOp, secondOp) {
    Operator.call(this,
        firstOp,
        secondOp)
}
prototypeChangesForOperator(Multiply,
    (a, b) => a * b,
    "*",
    function (parameter, firstOp, secondOp) {return new Add(
        new Multiply(
            firstOp.diff(parameter),
            secondOp),
        new Multiply(
            firstOp,
            secondOp.diff(parameter)))},
    function(firstOp, secondOp) {
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




function Divide(firstOp, secondOp) {
    Operator.call(this,
        firstOp,
        secondOp)
}
prototypeChangesForOperator(Divide,
    (a, b) => a / b,
    "/",
    function (parameter, firstOp, secondOp) {return new Divide(
        new Subtract(
            new Multiply(firstOp.diff(parameter), secondOp),
            new Multiply(firstOp, secondOp.diff(parameter))),
        new Multiply(secondOp, secondOp)
    )},
    function(firstOp, secondOp) {
        if (firstOp.toString() === "0") {
            return zero;
        } else {
            return new Divide(firstOp, secondOp)
        }
    })




function Negate(oper) {
    Operator.call(this,
        oper)
}
prototypeChangesForOperator(Negate,
    (a) => -a,
    "negate",
    function(parameter, oper) {return new Negate(oper.diff(parameter))},
    function(oper) {
        if (oper.toString() === "0") {
            return zero
        } else {
            return new Negate(oper)
        }
    })




function HMean (firstOp, secondOp) {
    Operator.call(this,
        firstOp,
        secondOp)
}
prototypeChangesForOperator(HMean ,
    (a, b) => 2 / (1 / a + 1 / b),
    "hmean",
    function(parameter, firstOp, secondOp) {return new Divide(
        new Multiply(
            new Const(2),
            new Multiply(firstOp, secondOp)),
        new Add(firstOp, secondOp)).diff(parameter)},
    function(firstOp, secondOp) {
        if (firstOp.toString() === "0") {
            return zero;
        } else if (secondOp.toString() === "0") {
            return zero;
        } else {
            return new HMean (firstOp, secondOp)
        }
    })




function Hypot(firstOp, secondOp) {
    Operator.call(this,
        firstOp,
        secondOp)
}
prototypeChangesForOperator(Hypot,
    (a, b) => a * a + b * b,
    "hypot",
    function(parameter, firstOp, secondOp) {return new Add(
        new Multiply(firstOp, firstOp),
        new Multiply(secondOp, secondOp)).diff(parameter)},
    function(firstOp, secondOp) {
        if (firstOp.toString() === "0") {
            return secondOp;
        } else if (secondOp.toString() === "0") {
            return firstOp;
        } else {
            return new Hypot(firstOp, secondOp)
        }
    })



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
            return new operator(... exp.splice(-operator.length))
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
