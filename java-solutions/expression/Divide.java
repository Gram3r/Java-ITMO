package expression;

import expression.GenOperations.AllGenericOperations;

public class Divide<T> extends BinaryOperation<T> {

    public Divide(GenericTripleExpression<T> left, GenericTripleExpression<T> right, AllGenericOperations<T> calc) {
        super(left, right, calc);
    }

    @Override
    protected T calc(T a, T b) {
        return calc.div(a, b);
    }

    @Override
    protected String getSymbol() {
        return "/";
    }
}
