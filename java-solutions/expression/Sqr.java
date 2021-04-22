package expression;

import expression.GenOperations.AllGenericOperations;

public class Sqr<T> extends UnaryOperation<T> {
    public Sqr(GenericTripleExpression<T> inner, AllGenericOperations<T> calc) {
        super(inner, calc);
    }

    @Override
    public T calc(T x) {
        return calc.square(x);
    }

    @Override
    public String getSymbol() {
        return "square";
    }
}