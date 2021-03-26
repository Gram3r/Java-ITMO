package expression;

import expression.GenOperations.AllGenericOperations;

public class Subtract<T> extends BinaryOperation<T> {

    public Subtract(TripleExpression<T> left, TripleExpression<T> right, AllGenericOperations<T> calc) {
        super(left, right, calc);
    }

    @Override
    protected T calc(T a, T b) {
        return calc.sub(a, b);
    }

    @Override
    protected String getSymbol() {
        return "-";
    }
}
