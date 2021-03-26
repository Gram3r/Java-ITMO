"use strict";

let operator = (f, ...inner_oper) => (...args) => (f(...inner_oper.map(value => value(...args))));

// let madd = operator((a, b, c) => a * b + c);
let madd = (x, y, z) => operator((a, b, c) => a * b + c, x, y, z);

let add = (x, y) => operator((a, b) => a + b, x, y);

let subtract = (x, y) => operator((a, b) => a - b, x, y);

let divide = (x, y) => operator((a, b) => a / b, x, y);

let multiply = (x, y) => operator((a, b) => a * b, x, y);

let negate = (x) => operator((y) => -y, x);

let floor = (x) => operator((y) => Math.floor(y), x);

let ceil = (x) => operator((y) => Math.ceil(y), x);

let cnst = (x) => () => x;

const one = cnst(1);
const two = cnst(2);

const constTokens = {
    "one" : one,
    "two" : two
}

const operatorTokens = {
    "*+" : madd,
    "madd" : madd,
    "+" : add,
    "-" : subtract,
    "/" : divide,
    "*" : multiply,
    "negate" : negate,
    "floor" : floor,
    "ceil" : ceil,
    "_" : floor,
    "^" : ceil
}

const variableTokens = {
    "x" : 0,
    "y" : 1,
    "z" : 2
}

let variable = (str) => {
    let index = variableTokens[str];
    return (...args) => args[index];
}

let res =
    madd(
        subtract(
            variable('x'),
            variable('y'),
        ),
        variable('z'),
        one
    )

for (let i = 0; i < 10; i++) {
    println(i + ': ' + res(i, i, i))
}

function parse(str) {
    let exp = [];
    let stack = str.split(' ');

    function apply(i) {
        if (i in operatorTokens) {
            let args = exp.splice(-operatorTokens[i].length);
            return operatorTokens[i](...args)
        } else if (i in variableTokens) {
            return variable(i)
        } else if (i in constTokens) {
            return constTokens[i]
        } else {
            return cnst(Number(i))
        }
    }

    for (const i of stack) {
        // :NOTE: !i
        if (!i) {
            continue;
        }
        exp.push(apply(i));
    }
    return exp[0];
}