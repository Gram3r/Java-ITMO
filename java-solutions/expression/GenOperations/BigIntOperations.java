package expression.GenOperations;

import expression.exceptions.DivisionByZeroParsingException;

import java.math.BigInteger;

public class BigIntOperations implements AllGenericOperations<BigInteger> {
    @Override
    public BigInteger add(BigInteger a, BigInteger b) {
        return a.add(b);
    }

    @Override
    public BigInteger mul(BigInteger a, BigInteger b) {
        return a.multiply(b);
    }

    @Override
    public BigInteger sub(BigInteger a, BigInteger b) {
        return a.subtract(b);
    }

    @Override
    public BigInteger div(BigInteger a, BigInteger b) {
        if (b.equals(BigInteger.ZERO)) {
            throw new DivisionByZeroParsingException(a + "/" + b);
        }
        return a.divide(b);
    }

    @Override
    public BigInteger neg(BigInteger a) {
        return a.negate();
    }

    @Override
    public BigInteger abs(BigInteger a) {
        return a.abs();
    }

    @Override
    public BigInteger mod(BigInteger a, BigInteger b) {
        if (b.equals(BigInteger.ZERO)) {
            throw new DivisionByZeroParsingException(a + "/" + b);
        }
        return a.mod(b);
    }

    @Override
    public BigInteger square(BigInteger a) {
        return a.multiply(a);
    }

    @Override
    public BigInteger valueOf(int a) {
        return BigInteger.valueOf(a);
    }
}
