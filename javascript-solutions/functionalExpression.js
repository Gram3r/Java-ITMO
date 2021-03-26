"use strict";

let operator = f => (...inner_oper) => (...args) => f(...inner_oper.map(value => value(...args)));

let madd = operator((a, b, c) => a * b + c);

let add = operator((a, b) => a + b);

let subtract = operator((a, b) => a - b);

let divide = operator((a, b) => a / b);

let multiply = operator((a, b) => a * b);

let negate = operator((y) => -y);

let floor = operator((y) => Math.floor(y));

let ceil = operator((y) => Math.ceil(y));

let cnst = x => () => x;

const one = cnst(1);
const two = cnst(2);

const constTokens = {
    "one" : one,
    "two" : two
}

const operatorTokens = {
    "*+" : [madd, 3],
    "madd" : [madd, 3],
    "+" : [add, 2],
    "-" : [subtract, 2],
    "/" : [divide, 2],
    "*" : [multiply, 2],
    "negate" : [negate, 1],
    "floor" : [floor, 1],
    "ceil" : [ceil, 1],
    "_" : [floor, 1],
    "^" : [ceil, 1]
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
    let stack = str.split(' ').filter(c => c !== "");

    function apply(i) {
        if (i in operatorTokens) {
            let operator = operatorTokens[i]
            return operator[0](... exp.splice(-operator[1]))
        } else if (i in variableTokens) {
            return variable(i)
        } else if (i in constTokens) {
            return constTokens[i]
        } else {
            return cnst(Number(i))
        }
    }

    for (const i of stack) {
        exp.push(apply(i));
    }
    return exp[0];
}