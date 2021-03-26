"use strict";

let operator = (f, ...inner_oper) => (...args) => (f(...inner_oper.map(value => value(...args))));

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
    let index = variableTokens[str]
    return (...args) => args[index]
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
    for (const i of stack) {
        if (!i) {
            continue;
        }
        let pushed;
        if (i in operatorTokens) {
            let temp = exp.splice(exp.length - operatorTokens[i].length);
            pushed = operatorTokens[i](...temp)
        } else if (i in variableTokens) {
            pushed = variable(i)
        } else if (i in constTokens) {
            pushed = constTokens[i]
        } else {
            pushed = cnst(Number(i))
        }
        exp.push(pushed);
    }
    return exp[0];
}