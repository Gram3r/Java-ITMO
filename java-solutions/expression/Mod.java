package expression;

import expression.GenOperations.AllGenericOperations;

public class Mod<T> extends BinaryOperation<T> {
    public Mod(TripleExpression<T> left, TripleExpression<T> right, AllGenericOperations<T> calc) {
        super(left, right, calc);
    }


    @Override
    protected T calc(T a, T b) {
        return calc.mod(a, b);
    }

    @Override
    protected String getSymbol() {
        return "mod";
    }
}
