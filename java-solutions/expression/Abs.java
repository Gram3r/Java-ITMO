package expression;


import expression.GenOperations.AllGenericOperations;

public class Abs<T> extends UnaryOperation<T> {
    public Abs(TripleExpression<T> inner, AllGenericOperations<T> calc) {
        super(inner, calc);
    }

    @Override
    public T calc(T x) {
        return calc.abs(x);
    }

    @Override
    public String getSymbol() {
        return "abs";
    }
}