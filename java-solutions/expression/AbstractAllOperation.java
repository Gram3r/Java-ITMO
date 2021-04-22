package expression;

import expression.GenOperations.AllGenericOperations;

public abstract class AbstractAllOperation<T> implements GenericTripleExpression<T> {
    protected final AllGenericOperations<T> calc;

    protected AbstractAllOperation(AllGenericOperations<T> calc) {
        this.calc = calc;
    }
}
