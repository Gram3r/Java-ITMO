package expression.GenOperations;

import expression.exceptions.DivisionByZeroParsingException;

public class PIntOperations implements AllGenericOperations<Integer>{
    private final int[] revMod = new int[1009];

    public PIntOperations() {
        revMod[1] = 1;
        for (int i = 2; i < 1009; ++i) {
            revMod[i] = (1009 - (1009 / i) * revMod[1009 % i] % 1009) % 1009;
        }
    }

    @Override
    public Integer add(Integer a, Integer b) {
        return mod(a + b);
    }

    @Override
    public Integer mul(Integer a, Integer b) {
        return mod(a * b);
    }

    @Override
    public Integer sub(Integer a, Integer b) {
        return mod(a - b);
    }

    @Override
    public Integer div(Integer a, Integer b) {
        if (b == 0) {
            throw new DivisionByZeroParsingException(a + "/" + b);
        }
        return mod(a * revMod[mod(b)]);
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
        return mod(-a);
    }

    @Override
    public Integer abs(Integer a) {
        return mod(Math.abs(a));
    }

    @Override
    public Integer square(Integer a) {
        return mod(mul(a, a));
    }

    @Override
    public Integer valueOf(int a) {
        return mod(a);
    }

    private Integer mod(int a) {
        return (a % 1009 + 1009) % 1009;
    }
}
