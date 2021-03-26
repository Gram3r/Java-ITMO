package expression.GenOperations;

import expression.exceptions.DivisionByZeroParsingException;
import expression.exceptions.OverflowParsingException;

public class IntOperations implements AllGenericOperations<Integer> {
    @Override
    public Integer add(Integer a, Integer b) {
        if (b > 0 && Integer.MAX_VALUE - b < a || b < 0 && Integer.MIN_VALUE - b > a) {
            throw new OverflowParsingException(a + "+" + b);
        }
        return a + b;
    }

    @Override
    public Integer mul(Integer a, Integer b) {
        if (a == 0 || b == 0) {
            return 0;
        }
        if (a > 0 && (b > 0 && a > Integer.MAX_VALUE / b || b < 0 && b < (Integer.MIN_VALUE / a)) ||
                a < 0 && (b > 0 && a < Integer.MIN_VALUE / b || b < 0 && b < (Integer.MAX_VALUE / a))) {
            throw new OverflowParsingException(a + "*" + b);
        }
        return a * b;
    }

    @Override
    public Integer sub(Integer a, Integer b) {
        if (b < 0 && a > Integer.MAX_VALUE + b || b > 0 && a < Integer.MIN_VALUE + b) {
            throw new OverflowParsingException(a + "-" + b);
        }
        return a - b;
    }

    @Override
    public Integer div(Integer a, Integer b) {
        if (a == Integer.MIN_VALUE && b == -1) {
            throw new OverflowParsingException(a + "/" + b);
        }
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
        if (a == Integer.MIN_VALUE) {
            throw new OverflowParsingException("-" + a);
        }
        return -a;
    }

    @Override
    public Integer abs(Integer a) {
        if (a == Integer.MIN_VALUE) {
            throw new OverflowParsingException("-" + a);
        }
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
