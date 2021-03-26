package expression.parser;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class StringSource implements Source {
    private final String data;
    private int pos;

    public StringSource(final String data) {
        this.data = data;
    }

    @Override
    public boolean hasNext() {
        return pos < data.length();
    }

    public boolean hasNext(int i) {
        return pos + i - 1 < data.length();
    }

    @Override
    public char next() {
        return data.charAt(pos++);
    }

    public char next(int i) {
        return data.charAt(pos + i - 1);
    }
}
