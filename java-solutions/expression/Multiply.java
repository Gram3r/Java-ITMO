package expression;

import expression.GenOperations.AllGenericOperations;

public class Multiply<T> extends BinaryOperation<T> {

    public Multiply(GenericTripleExpression<T> left, GenericTripleExpression<T> right, AllGenericOperations<T> calc) {
        super(left, right, calc);
    }

    @Override
    protected T calc(T a, T b) {
        return calc.mul(a, b);
    }

    @Override
    protected String getSymbol() {
        return "*";
    }
}
