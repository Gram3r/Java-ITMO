package expression.exceptions;

public class DivisionByZeroParsingException extends ArythmeticException{
    public DivisionByZeroParsingException(String err) {
        super("Division by zero", err);
    }
}
