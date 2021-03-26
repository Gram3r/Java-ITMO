"use strict";

let operator = (f, ...inner_oper) => (...args) => (f(...inner_oper.map(value => value(...args))));


let add = (x, y) => operator((a, b) => a + b, x, y);

let subtract = (x, y) => operator((a, b) => a - b, x, y);

let divide = (x, y) => operator((a, b) => a / b, x, y);

let multiply = (x, y) => operator((a, b) => a * b, x, y);

let negate = (x) => operator((y) => -y, x);

let cnst = (x) => () => x;

const one = cnst(1);
const two = cnst(2);

const constTokens = {
    "one" : one,
    "two" : two
}

const operatorTokens = {
    "+" : add,
    "-" : subtract,
    "/" : divide,
    "*" : multiply,
    "negate" : negate
}

const variableTokens = {
    "x" : 0,
    "y" : 1,
    "z" : 2
}

let variable = (str) => (...args) => {return args[variableTokens[str]]};

let res =
    add(
        subtract(
            multiply(
                variable('x'),
                variable('x')
            ),
            multiply(
                cnst(2),
                variable('x')
            )
        ),
        cnst(1))

for (let i = 0; i < 10; i++) {
    println(i + ': ' + res(i))
}

function parse(str) {
    let exp = [];
    let stack = str.split(' ');
    for (const i of stack) {
        if (!i) {
            continue;
        }
        if (i in operatorTokens) {
            let temp = exp.splice(exp.length - operatorTokens[i].length);
            exp.push(operatorTokens[i](...temp));
        } else if (i in variableTokens) {
            exp.push(variable(i));
        } else if (i in constTokens) {
            exp.push(constTokens[i]);
        } else {
            exp.push(cnst(Number(i)))
        }
    }
    return exp[0];
}