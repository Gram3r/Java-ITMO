"use strict";



let abstractOperator = function (...args) {
    this.args = args;
    this.toString = () => "" + args[0];
    this.prefix = this.toString;
    this.postfix = this.toString;
    this.evaluate = () => args[0];
    this.diff = () => zero;
};

function fabricForOperations(func, symbol, operDiffer) {
    let constructorOp = function(...args) {
        Operator.apply(this, args)
    };
    constructorOp.prototype = Object.create(Operator);
    constructorOp.prototype.func = func;
    constructorOp.prototype.symbol = symbol;
    constructorOp.prototype.operDiffer = operDiffer;
    return constructorOp
}

function fabricForError(message) {
    let ExprError = function(expr, index) {
        this.message = message + " : " + expr + " on " + index + " index";
    };
    ExprError.prototype = Object.create(Error.prototype);
    return ExprError
}

const Const = function(value) {
    abstractOperator.apply(this, [value]);
};

const Variable = function(str) {
    abstractOperator.apply(this, [str, variableTokens.get(str)]);
    this.evaluate = function(...arg) {return arg[this.args[1]]};
    this.diff = function(parameter) {return (parameter === str ? one : zero)}
};

const Operator = function(...args) {
    abstractOperator.apply(this, args);
    this.toString = function() {return this.args.join(" ") + ` ${this.symbol}`;};
    this.prefix = function() {return `(${this.symbol} ` + this.args.map(val => val.prefix()).join(" ") + ')'};
    this.postfix = function() {return '(' + this.args.map(val => val.postfix()).join(" ") + ` ${this.symbol})`};
    this.evaluate = function(...arg) {return this.func(...this.args.map(value => value.evaluate(...arg)))};
    this.diff = function (parameter) {return this.operDiffer(parameter, ...this.args, ...this.args.map(val => val.diff(parameter)))}
};


const Add = fabricForOperations(
    (a, b) => a + b,
    "+",
    (parameter, f, s, fx, sx) => new Add(fx, sx),
);



const Subtract = fabricForOperations(
    (a, b) => a - b,
    "-",
    (parameter, f, s, fx, sx) => new Subtract(fx, sx),
);




const Multiply = fabricForOperations(
    (a, b) => a * b,
    "*",
    (parameter, f, s, fx, sx) => new Add(
        new Multiply(fx, s),
        new Multiply(f, sx)),
);




const Divide = fabricForOperations(
    (a, b) => a / b,
    "/",
    (parameter, f, s, fx, sx) => new Divide(
        new Subtract(
            new Multiply(fx, s),
            new Multiply(f, sx)),
        new Multiply(s, s)),
);


const Negate = fabricForOperations(
    (a) => -a,
    "negate",
    (parameter, f, fx) => new Negate(fx),
);


const HMean = fabricForOperations(
    (a, b) => 2 / (1 / a + 1 / b),
    "hmean",
    (parameter, f, s) => new Divide(
        new Multiply(
            new Const(2),
            new Multiply(f, s)),
        new Add(f, s)).diff(parameter),
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




const longadd = (...args) => (args.reduce((a, b) => a + b, 0))
const longmul = (...args) => (args.reduce((a, b) => a * b, 1))

const LongAdd = fabricForOperations(
    longadd,
    "long-add",
    (parameter, ...args) => (args.slice(args.length / 2).reduce((first, second) => new Add(first, second)))
);

const LongMul = fabricForOperations(
    longmul,
    "long-mul",
    (parameter, ...args) => args.slice(0, args.length / 2).reduce((first, second) => new Multiply(first, second), one).diff(parameter)
);

const Log = fabricForOperations(
    Math.log,
    "log",
    (parameter, a) => new Divide(a.diff(parameter), a)
);

const Pow = fabricForOperations(
    Math.pow,
    "pow",
    (parameter, a, b) => (
        new Multiply(new Pow(a, new Subtract(b, one)),
            new Add(
                new Multiply(b, a.diff(parameter)),
                new LongMul(a, new Log(a), b.diff(parameter))
            ))
    )
);

const Abs = fabricForOperations(
    Math.abs,
    "abs",
    (parameter, a) => new Divide(new Multiply(a, a.diff(parameter)), new Abs(a))
);

const ArithMean = fabricForOperations(
    (...args) => longadd(...args) / args.length,
    "arith-mean",
    (parameter, ...args) => new Divide(new LongAdd(...args.slice(args.length / 2)), new Const(args.length / 2)),
);

const GeomMean = fabricForOperations(
    (...args) => Math.pow(Math.abs(longmul(...args)), 1 / args.length),
    "geom-mean",
    (parameter, ...args) => new Pow(new Abs(new LongMul(...args.slice(0, args.length / 2))), new Const(2 / args.length)).diff(parameter),
);

const HarmMean = fabricForOperations(
    (...args) => args.length / longadd(...args.map(val => 1 / val)),
    "harm-mean",
    (parameter, ...args) => new Divide(
        new Const(args.length / 2),
        new LongAdd(...args.slice(0, args.length / 2).map(val => new Divide(one, val)))).diff(parameter)
);

// :NOTE: Const.ZERO
const zero = new Const(0);
const one = new Const(1);


const variableTokens = new Map([
    ["x", 0],
    ["y", 1],
    ["z", 2]
]);


const constTokens = new Map([
    [0, zero],
    [1, one]
]);


const operatorTokens = new Map([
    ["+", Add],
    ["-", Subtract],
    ["/", Divide],
    ["*", Multiply],
    ["negate", Negate],
    ["hypot", Hypot],
    ["hmean", HMean],
    ["arith-mean", ArithMean],
    ["geom-mean", GeomMean],
    ["harm-mean", HarmMean]
]);



function parse(str) {
    let exp = [];
    let stack = str.split(' ').filter(c => c !== "");

    function apply(i) {
        if (operatorTokens.has(i)) {
            let operator = operatorTokens.get(i);
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


function parsePrefix(str) {
    return parseAll(str, 'prefix')
}

function parsePostfix(str) {
    return parseAll(str, 'postfix')
}

function parseAll(str, mode) {
    str = str.replace(/[(]/g, ' ( ').replace(/[)]/g, ' ) ');
    let expr = str.split(/[ ]/).filter(c => c !== "");
    if (expr.length === 0) {
        throw new EmptyStringError('', 0);
    }
    if (expr[0] !== '(') {
        return parseTokens(expr,0, 0, 'prefix')[0][0]
    }
    let res = parseExpr(expr, 1, 1, mode);
    if (res[1] !== 0) {
        throw new BracketsError(')', expr.length - 1);
    }
    if (res[2] < expr.length - 1) {
        throw new EndOfExpressionError(expr[res[1] + 1], res[1] + 1);
    }
    return res[0];
}


function parseExpr(expr, balance, index, mode) {
    //print(index)
    let tokens = parseTokens(expr, balance, index, mode)
    let op;
    let args;
    //print(tokens[0])
    if (mode === 'prefix') {
        op = tokens[0][0];
        args = tokens[0].slice(1)
    } else {
        args = tokens[0].slice(0 , tokens[0].length - 1)
        op = tokens[0][tokens[0].length - 1]
    }
    index = tokens[2];
    if (op.prototype.func.length !== args.length && op.prototype.func.length !== 0) {
        throw new LengthOfArgumentsError(args, index)
    }
    return [new op(...args), tokens[1], index]
}

function parseTokens(expr, balance, index, mode) {
    let args = [];
    while (index < expr.length) {
        let ex = expr[index];
        if (ex === '(') {
            let expression = parseExpr(expr, balance + 1, index + 1, mode);
            index = expression[1];
            args.push(expression[0])
        } else if (ex === (')')) {
            balance--;
            if (balance < 0) {
                throw new BracketsError(ex, index)
            }
            break;
        } else if (variableTokens.has(ex)) {
            args.push(new Variable(ex))
        } else if (constTokens.has(ex)) {
            args.push(new constTokens.get(ex));
        } else if (!isNaN(ex)) {
            args.push(new Const(Number(ex)))
        } else if (operatorTokens.has(ex)) {
            args.push(operatorTokens.get(ex));
        } else {
            throw new TokenError(ex, index)
        }
        index++;
        if (balance === 0 && index !== expr.length) {
            throw new EndOfExpressionError(ex, index)
        }
    }
    return [args, balance, index]
}


const EndOfExpressionError = fabricForError('Unexpected Symbols');

const TokenError = fabricForError("Unexpected token");

const BracketsError = fabricForError("Wrong number of brackets");

const OperatorError = fabricForError("No bracket after operator");

const LengthOfArgumentsError = fabricForError("Wrong number of arguments");

const EmptyStringError = fabricForError("Empty expression");

let print = console.log
let expr = parsePostfix('(x (3 2 +) *)')
console.log(expr.evaluate(2));
