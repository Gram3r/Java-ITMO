package expression.exceptions;

public class OverflowParsingException extends ArythmeticException {
    public OverflowParsingException(String str) {
        super("Overflow", str);
    }
}
