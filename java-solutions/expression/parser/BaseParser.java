package expression.parser;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class BaseParser {
    private Source source;
    protected char ch = 0xffff;

    protected void setSource(final Source source) {
        this.source = source;
        nextChar();
    }

    protected void nextChar() {
        ch = source.hasNext() ? source.next() : '\0';
    }

    protected boolean test(char expected) {
        if (ch == expected) {
            nextChar();
            return true;
        }
        return false;
    }

    protected boolean test(String expected) {
        for (int i = 0; i < expected.length(); i++) {
            if (!source.hasNext(i) || source.next(i) != expected.charAt(i)) {
                return false;
            }
        }
        expect(expected);
        return true;
    }

    protected void expect(final char c) {
        if (ch != c) {
            //throw new InvalidOperationParsingException();
        }
        nextChar();
    }

    protected void expect(final String value) {
        for (char c : value.toCharArray()) {
            expect(c);
        }
    }

    protected boolean between(final char from, final char to) {
        return from <= ch && ch <= to;
    }

    protected void skipWhitespace() {
        while (test(' ') || test('\r') || test('\n') || test('\t')) {
        }
    }
}
