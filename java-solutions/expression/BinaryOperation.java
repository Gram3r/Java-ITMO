package expression;

import expression.GenOperations.AllGenericOperations;

import java.util.Objects;

public abstract class BinaryOperation<T> extends AbstractAllOperation<T> {
    private final GenericTripleExpression<T> left, right;

    public BinaryOperation(GenericTripleExpression<T> left, GenericTripleExpression<T> right, AllGenericOperations<T> calc){
        super(calc);
        this.left = left;
        this.right = right;
    }


    @Override
    public T evaluate(int x, int y, int z) {
        return calc(left.evaluate(x, y, z), right.evaluate(x, y, z));
    }


    @Override
    public String toString() {
        return "(" + left + " " + getSymbol() + " " + right + ")";
    }


    public boolean equals(Object second) {
        if (second != null && second.getClass() == getClass()) {
            final BinaryOperation<?> that = (BinaryOperation<?>) second;
            return that.left.equals(this.left) && that.right.equals(this.right);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right, this.getClass());
    }

    protected abstract String getSymbol();

    protected abstract T calc(T a, T b);
}
