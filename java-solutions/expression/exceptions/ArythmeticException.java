package expression.exceptions;

public class ArythmeticException extends ArithmeticException {
    public ArythmeticException(String str, String err) {
        super(str + "in that operation: " + err);
    }
}
