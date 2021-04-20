package expression.parser;

import expression.*;
import expression.GenOperations.AllGenericOperations;

import java.util.List;

public class ExpressionParser<T, C extends AllGenericOperations<T>> extends BaseParser implements Parser {
    private final C calc;

    private static final List<List<String>> PRIORITIESBIN = List.of(
            List.of("/", "*", "mod"),
            List.of("+", "-")
    );

    private static final List<String> PRIORITIESUN = List.of(
            "abs", "square"
    );

    private static final int maxlev = PRIORITIESBIN.size() - 1, minlev = -1;

    public ExpressionParser(C calc) {
        this.calc = calc;
    }


    @Override
    public TripleExpression<T> parse(String expression) {
        setSource(new StringSource(expression));
        skipWhitespace();
        return parse(maxlev);
    }

    private TripleExpression parse(int curentlev) {
        if (curentlev == minlev) {
            return getOperatorOrUnaryExpression();
        }
        skipWhitespace();
        boolean existNext = true;
        TripleExpression<T> current = parse(curentlev - 1);
        while (existNext) {
            existNext = false;
            skipWhitespace();
            for (String operation : PRIORITIESBIN.get(curentlev)) {
                if (test(operation)) {
                    current = getBinaryExpression(operation, current, parse(curentlev - 1));
                    existNext = true;
                }
            }
        }
        return current;
    }



    private TripleExpression getOperatorOrUnaryExpression() {
        skipWhitespace();
        if (test('-')) {
            skipWhitespace();
            if (isDigit()) {
                return getConst(true);
            } else {
                return new NegativeOperator<>(getOperatorOrUnaryExpression(), calc);
            }
        } else if (test('(')) {
            TripleExpression exp = parse(maxlev);
            expect(')');
            return exp;
        } else if (isDigit()) {
            return getConst(false);
        } else {
            for (String operation : PRIORITIESUN) {
                if (test(operation)) {
                    return getUnaryExpression(operation, getOperatorOrUnaryExpression());
                }
            }
            String str = getVariable();
            return new Variable<>(str, calc);
        }
    }

    private Const<T> getConst(boolean withMinus) {
        StringBuilder builder = new StringBuilder();
        if (withMinus)
            builder.append("-");
        while (isDigit()) {
            builder.append(ch);
            nextChar();
        }
        skipWhitespace();
        return new Const<>(calc.valueOf(Integer.parseInt(builder.toString())));
    }

    private String getVariable() {
        StringBuilder builder = new StringBuilder();
        skipWhitespace();
        while (isCharVar() && !Character.isWhitespace(ch)) {
            builder.append(ch);
            nextChar();
        }
        skipWhitespace();
        return builder.toString();
    }

    private BinaryOperation<T> getBinaryExpression(String lastop, TripleExpression left, TripleExpression right) {
        switch (lastop) {
            case "+" :
                return new Add<>(left, right, calc);
            case "-" :
                return new Subtract<>(left, right, calc);
            case "*" :
                return new Multiply<>(left, right, calc);
            case "/" :
                return new Divide<>(left, right, calc);
            case "mod" :
                return new Mod<>(left, right, calc);
            default :
                return null;
        }
    }

    private UnaryOperation<T> getUnaryExpression(String lastop, TripleExpression exp) {
        switch (lastop) {
            case "abs" :
                return new Abs<>(exp, calc);
            case "square" :
                return new Sqr<>(exp, calc);
            default :
                return null;
        }
    }

    private boolean isDigit() {
        return between('0', '9');
    }

    private boolean isCharVar() {
        return Character.isDigit(ch) || Character.isLetter(ch) || ch == '$' || ch == '_';
    }
}
