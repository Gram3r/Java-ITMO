package expression;

public class Const<T> implements TripleExpression<T> {
    private final T num;

    public Const(T num) {
        this.num = num;
    }

    @Override
    public T evaluate(int x, int y, int z) {
        return num;
    }

    @Override
    public boolean equals(Object second) {
        return second != null
                && second.getClass() == getClass()
                && this.num.equals(((Const<?>) second).num);
    }

    @Override
    public String toString() {
        return num.toString();
    }

    @Override
    public int hashCode() {
        return num.hashCode();
    }
}
