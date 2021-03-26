package expression.GenOperations;

import expression.exceptions.DivisionByZeroParsingException;

public class UIntOperations implements AllGenericOperations<Integer> {
    @Override
    public Integer add(Integer a, Integer b) {
        return a + b;
    }

    @Override
    public Integer mul(Integer a, Integer b) {
        return a * b;
    }

    @Override
    public Integer sub(Integer a, Integer b) {
        return a - b;
    }

    @Override
    public Integer div(Integer a, Integer b) {
        if (b == 0) {
            throw new DivisionByZeroParsingException(a + "/" + b);
        }
        return a / b;
    }

    @Override
    public Integer mod(Integer a, Integer b) {
        if (b == 0) {
            throw new DivisionByZeroParsingException(a + "/" + b);
        }
        return a % b;
    }

    @Override
    public Integer neg(Integer a) {
        return -a;
    }

    @Override
    public Integer abs(Integer a) {
        return Math.abs(a);
    }

    @Override
    public Integer square(Integer a) {
        return mul(a, a);
    }

    @Override
    public Integer valueOf(int a) {
        return a;
    }
}
