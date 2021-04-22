package expression;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public interface GenericTripleExpression<T> {
    T evaluate(int x, int y, int z);

    String toString();
}
