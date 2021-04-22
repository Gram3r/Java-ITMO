package expression;


import expression.GenOperations.AllGenericOperations;

public class NegativeOperator<T> extends UnaryOperation<T> {

    public NegativeOperator(GenericTripleExpression<T> inner, AllGenericOperations<T> calc) {
        super(inner, calc);
    }

    @Override
    public T calc(T x) {
        return calc.neg(x);
    }

    @Override
    public String getSymbol() {
        return "-";
    }
}