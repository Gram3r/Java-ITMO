package expression.parser;

import expression.GenericTripleExpression;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public interface GenericParser<T> {
    GenericTripleExpression<T> parse(String expression);
}
