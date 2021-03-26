package expression.GenOperations;

import expression.exceptions.DivisionByZeroParsingException;

public class ByteOperations implements AllGenericOperations<Byte> {
    @Override
    public Byte add(Byte a, Byte b) {
        return (byte) (a + b);
    }

    @Override
    public Byte mul(Byte a, Byte b) {
        return (byte) (a * b);
    }

    @Override
    public Byte sub(Byte a, Byte b) {
        return (byte) (a - b);
    }

    @Override
    public Byte div(Byte a, Byte b) {
        if (b == 0) {
            throw new DivisionByZeroParsingException(a + "/" + b);
        }
        return (byte) (a / b);
    }

    @Override
    public Byte mod(Byte a, Byte b) {
        if (b == 0) {
            throw new DivisionByZeroParsingException(a + "/" + b);
        }
        return (byte) (a % b);
    }

    @Override
    public Byte neg(Byte a) {
        return (byte) -a;
    }

    @Override
    public Byte abs(Byte a) {
        return (byte) Math.abs(a);
    }

    @Override
    public Byte square(Byte a) {
        return mul(a, a);
    }

    @Override
    public Byte valueOf(int a) {
        return (byte) a;
    }
}
