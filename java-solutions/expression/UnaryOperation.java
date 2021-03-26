package expression;

import expression.GenOperations.AllGenericOperations;

public abstract class UnaryOperation<T> extends AbstractAllOperation<T> {
    protected final TripleExpression<T> expression;

    public UnaryOperation(TripleExpression<T> expression, AllGenericOperations<T> calc) {
        super(calc);
        this.expression = expression;
    }

    protected abstract String getSymbol();

    @Override
    public T evaluate(int x, int y, int z) {
        return calc(expression.evaluate(x, y, z));
    }

    @Override
    public String toString() {
        return getSymbol() + "(" + expression.toString() + ")";
    }

    public abstract T calc(T x);
}