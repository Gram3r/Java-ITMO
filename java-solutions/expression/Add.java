package expression;

import expression.GenOperations.AllGenericOperations;

public class Add<T> extends BinaryOperation<T> {
    public Add(GenericTripleExpression<T> left, GenericTripleExpression<T> right, AllGenericOperations<T> calc) {
        super(left, right, calc);
    }


    @Override
    protected T calc(T a, T b) {
        return calc.add(a, b);
    }

    @Override
    protected String getSymbol() {
        return "+";
    }
}
