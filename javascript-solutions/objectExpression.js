"use strict";

let abstractOperator = function (...args) {
    this.args = args;
    this.toString = () => "" + args[0];
    this.prefix = this.toString;
    this.postfix = this.toString;
    this.evaluate = () => args[0];
    this.diff = () => Const.zero;
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

const Const = function(value) {
    abstractOperator.apply(this, [value]);
};

const Variable = function(str) {
    abstractOperator.apply(this, [str, variableTokens.get(str)]);
    this.evaluate = function(...arg) {return arg[this.args[1]]};
    this.diff = function(parameter) {return (parameter === str ? Const.one : Const.zero)}
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
    (parameter, f, s, fx, sx) => new Divide(
        new Multiply(
            new Const(2),
            new Multiply(f, s)),
        new Add(f, s)).diff(parameter),
)

const Hypot = fabricForOperations(
    (a, b) => a * a + b * b,
    "hypot",
    (parameter, f, s, fx, sx) => new Add(
        new Multiply(f, f),
        new Multiply(s, s)).diff(parameter),
)

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
    (parameter, ...args) => args.slice(0, args.length / 2).reduce((first, second) => new Multiply(first, second), Const.one).diff(parameter)
);

const Log = fabricForOperations(
    Math.log,
    "log",
    (parameter, f, fx) => new Divide(fx, f)
);

const Pow = fabricForOperations(
    Math.pow,
    "pow",
    (parameter, f, s, fx, sx) => (
        new Multiply(
            new Pow(f, new Subtract(s, Const.one)),
            new Add(
                new Multiply(s, fx),
                new LongMul(f, new Log(f), sx))
            ))
);

const Abs = fabricForOperations(
    Math.abs,
    "abs",
    (parameter, f, fx) => new Divide(new Multiply(f, fx), new Abs(f))
);

const ArithMean = fabricForOperations(
    (...args) => longadd(...args) / args.length,
    "arith-mean",
    (parameter, ...args) => new Divide(new LongAdd(...args.slice(args.length / 2)), new Const(args.length / 2)),
);

const GeomMean = fabricForOperations(
    (...args) => Math.pow(Math.abs(longmul(...args)), 1 / args.length),
    "geom-mean",
    function (parameter, ...args) {
        let s = new Const(2 / args.length);
        let f =  new Abs(new LongMul(...args.slice(0, args.length / 2)));
        return new Multiply(
            new Pow(f, new Subtract(s, Const.one)),
            new Add(
                new Multiply(s, f.diff(parameter)),
                new LongMul(f, new Log(f), s.diff(parameter)))
        )
    }
);

const HarmMean = fabricForOperations(
    (...args) => args.length / longadd(...args.map(val => 1 / val)),
    "harm-mean",
    (parameter, ...args) => new Divide(
        new Const(args.length / 2),
        new LongAdd(...args.slice(0, args.length / 2).map(val => new Divide(Const.one, val)))).diff(parameter)
);

//HHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH

Const.zero = new Const(0);
Const.one = new Const(1);

const variableTokens = new Map([
    ["x", 0],
    ["y", 1],
    ["z", 2]
]);

const constTokens = new Map([
    [0, Const.zero],
    [1, Const.one]
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

const parsePrefix = str => parseAll(str, 'prefix');
const parsePostfix = str => parseAll(str, 'postfix');

function parseAll(str, mode) {
    const source = new Source(str);
    let res;
    if (source.getExpression().length === 0) {
        throw new EmptyStringError([''], 0);
    }
    if (source.currentToken !== '(') {
        if (source.getExpression().length > 1) {
            throw new EndOfExpressionError(source.getExpression(), source.getInd(2), source.getToken(1))
        }
        res = parseTokens(source, 'prefix')
    } else {
        source.nextToken()
        res = parseExpr(source, mode);
    }
    if (source.hasNext()) {
        throw new EndOfExpressionError(source.getExpression(), source.getInd(1), source.getToken(1));
    }
    return res[0];
}

function Source(str) {
    let pos = -1;
    str = str.replace(/[(]/g, ' ( ').replace(/[)]/g, ' ) ');
    let expression = str.split(/ /).filter(c => c !== "");
    this.currentToken = expression[0];
    this.nextToken = function () {
        if (this.hasNext()) {
            pos++;
            this.currentToken = expression[pos]
            return this.currentToken;
        } else {
            throw new EndOfExpressionError(expression, this.getInd(0), this.currentToken)
        }
    }
    this.hasNext = () => {return pos < expression.length - 1}
    this.getToken = (ind) => {return expression[ind]}
    this.getExpression = () => {return expression}
    this.getPos = () => {return pos}
    this.getInd = (delta) => {
        return pos + delta > 0 ? expression.slice(0, pos + delta).map(x => x.length).reduce((a, b) => a + b) + pos + delta : 0}
}

function parseExpr(source, mode) {
    let args = parseTokens(source, mode)
    let Op = mode === 'prefix' ? args.shift() : args.pop()
    if (typeof Op !== "function") {
        throw new TokenError(source.getExpression(), source.getInd(0), source.currentToken)
    }
    if (Op.prototype.func.length !== args.length && Op.prototype.func.length !== 0) {
        throw new LengthOfArgumentsError(source.getExpression(), source.getInd(-1), args.length, Op.prototype.func.length, Op.prototype.symbol)
    }
    return [new Op(...args)]
}

function parseTokens(source, mode) {
    let args = [];
    while (source.hasNext() && source.nextToken() !== ')') {
        let ex = source.currentToken;
        if (ex === '(') {
            let expression = parseExpr(source, mode);
            args.push(expression[0])
            if (source.currentToken !== ')') {
                throw new TokenError(source.getExpression(), source.getInd(0), ex)
            }
        } else if (variableTokens.has(ex)) {
            args.push(new Variable(ex))
        } else if (constTokens.has(ex)) {
            args.push(new constTokens.get(ex));
        } else if (isFinite(ex)) {
            args.push(new Const(Number(ex)))
        } else if (operatorTokens.has(ex)) {
            if (mode === 'prefix' && source.getToken(source.getPos() - 1) === '(' || mode === 'postfix' && source.getToken(source.getPos() + 1) === ')') {
                args.push(operatorTokens.get(ex));
            } else {
                throw new OperatorError(source.getExpression(), source.getInd(0), ex);
            }
        } else {
            throw new TokenError(source.getExpression(), source.getInd(0), ex)
        }
    }
    return args
}

function errorFactory(errorMessageFunc) {
    class ExprError extends Error {
        constructor(expr, index, ...args) {
            super(errorMessageFunc(...args) + ' on ' + (index + 1) + ' index' + '\n' +
                expr.join(' ') + '\n' +
                '-'.repeat(index) + '^');
        }
    }
    return ExprError
}

const EndOfExpressionError = errorFactory(
    (symbols) =>
        ('Unexpected Symbols : ' + symbols));
const TokenError = errorFactory(
    (token) =>
        ('Unexpected token : ' + token));
const BracketsError = errorFactory(
    (bracket) =>
        ('Wrong number of brackets ' + bracket));
const OperatorError = errorFactory(
    (op) =>
        ('No bracket after ' + op));
const LengthOfArgumentsError = errorFactory(
    (found, need, op) =>
        ('Wrong number of arguments. Found ' + found + ', but ' + op + ' needs ' + need));
const EmptyStringError = errorFactory(
    (index) => 'Was given empty expression');