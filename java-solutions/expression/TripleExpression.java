package expression;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public interface TripleExpression<T> {
    T evaluate(int x, int y, int z);

    String toString();
}
