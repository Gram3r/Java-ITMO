package expression.parser;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public interface Source {
    boolean hasNext();
    char next();
    boolean hasNext(int i);
    char next(int i);
}
