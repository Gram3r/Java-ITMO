"use strict";



let abstractOperator = function (...args) {
    this.args = args;
    this.toString = () => "" + args[0];
    this.prefix = this.toString;
    this.postfix = this.toString;
    this.evaluate = () => args[0];
    this.diff = () => zero;
}

function fabricForOperations(func, symbol, operDiffer) {
    let constructorOp = function(...args) {
        Operator.apply(this, args)
    }
    constructorOp.prototype = Object.create(Operator);
    constructorOp.prototype.func = func;
    constructorOp.prototype.symbol = symbol;
    constructorOp.prototype.operDiffer = operDiffer;
    return constructorOp
}

function fabricForError(message) {
    let ExprError = function(expr) {
        this.message = message + " : " + expr;
    }
    ExprError.prototype = Object.create(Error.prototype);
    return ExprError
}

const Const = function(value) {
    abstractOperator.apply(this, [value]);
}

const Variable = function(str) {
    abstractOperator.apply(this, [str, variableTokens.get(str)]);
    this.evaluate = function(...arg) {return arg[this.args[1]]}
    this.diff = function(parameter) {return (parameter === str ? one : zero)}
}

const Operator = function(...args) {
    abstractOperator.apply(this, args);
    this.toString = function() {return this.args.join(" ") + ` ${this.symbol}`;}
    this.prefix = function() {return `(${this.symbol} ` + this.args.map(val => val.prefix()).join(" ") + ')'}
    this.postfix = function() {return '(' + this.args.map(val => val.postfix()).join(" ") + ` ${this.symbol})`}
    this.evaluate = function(...arg) {return this.func(...this.args.map(value => value.evaluate(...arg)))}
    this.diff = function (parameter) {return this.operDiffer(parameter, ...this.args)}
}


const Add = fabricForOperations(
    (a, b) => a + b,
    "+",
    (parameter, f, s) => new Add(f.diff(parameter), s.diff(parameter)),
)



const Subtract = fabricForOperations(
    (a, b) => a - b,
    "-",
    (parameter, f, s) => new Subtract(f.diff(parameter), s.diff(parameter)),
)




const Multiply = fabricForOperations(
    (a, b) => a * b,
    "*",
    (parameter, f, s) => new Add(
        new Multiply(f.diff(parameter), s),
        new Multiply(f, s.diff(parameter))),
)




const Divide = fabricForOperations(
    (a, b) => a / b,
    "/",
    (parameter, f, s) => new Divide(
        new Subtract(
            new Multiply(f.diff(parameter), s),
            new Multiply(f, s.diff(parameter))),
        new Multiply(s, s)),
)




const Negate = fabricForOperations(
    (a) => -a,
    "negate",
    (parameter, f) => new Negate(f.diff(parameter)),
)

function FoldLeft(func, start) {
    return (...args) => {
        let result = start;
        for (const arg of args) {
            result = func(result, arg);
        }
        return result;
    }
}

const longadd = FoldLeft((a, b) => a + b, 0);
const longmul = FoldLeft((a, b) => a * b, 1);

const LongAdd = fabricForOperations(
    longadd,
    "long-add",
    (parameter, ...args) => (args.reduce((first, second) => new Add(first, second)).diff(parameter))
)

const LongMul = fabricForOperations(
    longmul,
    "long-mul",
    (parameter, ...args) => args.reduce((first, second) => new Multiply(first, second), one).diff(parameter)
)

const Log = fabricForOperations(
    (a) => Math.log(a),
    "log",
    (parameter, a) => new Divide(a.diff(parameter), a)
)

const Pow = fabricForOperations(
    (a, b) => Math.pow(a, b),
    "pow",
    (parameter, a, b) => (
        new Multiply(new Pow(a, new Subtract(b, one)),
            new Add(
                new Multiply(b, a.diff(parameter)),
                new LongMul(a, new Log(a), b.diff(parameter))
            ))
    )
)

const Abs = fabricForOperations(
    (a) => Math.abs(a),
    "abs",
    (parameter, a) => new Divide(new Multiply(a, a.diff(parameter)), new Abs(a))
)

const ArithMean = fabricForOperations(
    (...args) => longadd(...args) / args.length,
    "arith-mean",
    (parameter, ...args) => new Divide(new LongAdd(...args).diff(parameter), new Const(args.length)),
)

const GeomMean = fabricForOperations(
    (...args) => Math.pow(Math.abs(longmul(...args)), 1 / args.length),
    "geom-mean",
    (parameter, ...args) => new Pow(new Abs(new LongMul(...args)), new Const(1 / args.length)).diff(parameter),
)

const HarmMean = fabricForOperations(
    (...args) => args.length / longadd(...args.map(val => 1 / val)),
    "harm-mean",
    (parameter, ...args) => new Divide(
        new Const(args.length),
        new LongAdd(...args.map(val => new Divide(one, val)))).diff(parameter)
)

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
    ["long-add", LongAdd],
    ["long-mul", LongMul],
    ["arith-mean", ArithMean],
    ["geom-mean", GeomMean],
    ["harm-mean", HarmMean],
    ["pow", Pow],
    ["abs", Abs]
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


function parsePrefix(str) {
    return parseAll(str, 'prefix')
}

function parsePostfix(str) {
    return parseAll(str, 'postfix')
}

function parseAll(str, mode) {
    str = str.replace(/[(]/g, ' ( ').replace(/[)]/g, ' ) ')
    let expr = str.split(/[ ]/).filter(c => c !== "");
    if (expr.length === 0) {
        throw new EmptyString('');
    }
    if (expr[0] !== '(') {
        return parseArgs(expr,0, 0, 'prefix')[0][0]
    }
    let res = parse(expr, 1, 0, mode)
    if (res[2] !== 0) {
        throw new BracketsError(')');
    }
    if (res[1] < expr.length - 1) {
        throw new EndOfExpressionError(expr[res[1]]);
    }
    return res[0];
}


function parse(expr, balance, index, mode) {
    let op;
    let args;
    if (mode === 'prefix') {
        op = parseOper(expr, ++index)
        args = parseArgs(expr, balance, ++index, mode)
        index = args[1]
    } else {
        args = parseArgs(expr, balance, ++index, mode)
        index = args[1]
        op = parseOper(expr, index - 1)
    }
    if (op.prototype.func.length !== args[0].length && op.prototype.func.length !== 0) {
        throw new LengthOfArgumentsError(args[0])
    }
    return [new op(...args[0]), index, args[2]]
}

function parseOper(expr, index) {
    let ex = expr[index]
    if (operatorTokens.has(ex)) {
        return operatorTokens.get(ex)
    } else {
        throw new OperatorError(ex)
    }
}

function parseArgs(expr, balance, index, mode) {
    let args = [];
    while (index < expr.length) {
        let ex = expr[index]
        if (operatorTokens.has(ex)){
            if (mode === 'prefix' || expr[index + 1] !== ')') {
                throw new OperatorError(ex);
            }
        } else if (ex === (')')) {
            balance--;
            if (balance < 0) {
                throw new BracketsError(ex)
            }
            break;
        } else if (ex === '(') {
            let expression = parse(expr, balance + 1, index, mode)
            index = expression[1]
            args.push(expression[0])
        } else if (variableTokens.has(ex)) {
            args.push(new Variable(ex))
        } else if (constTokens.has(ex)) {
            args.push(new constTokens.get(ex));
        } else if (!isNaN(ex)) {
            args.push(new Const(Number(ex)))
        } else {
            throw new TokenError(ex)
        }
        index++;
        if (balance === 0 && index !== expr.length) {
            throw new EndOfExpressionError(ex)
        }
    }
    return [args, index, balance]
}


const EndOfExpressionError = fabricForError('Unexpected Symbols')

const TokenError = fabricForError("Unexpected token")

const BracketsError = fabricForError("Wrong number of brackets")

const OperatorError = fabricForError("No operator after bracket")

const LengthOfArgumentsError = fabricForError("Too much arguments")

const EmptyString = fabricForError("Empty expression")

