package expression.GenOperations;

public class DoubleOperations implements AllGenericOperations<Double> {
    @Override
    public Double add(Double a, Double b) {
        return a + b;
    }

    @Override
    public Double mul(Double a, Double b) {
        return a * b;
    }

    @Override
    public Double sub(Double a, Double b) {
        return a - b;
    }

    @Override
    public Double div(Double a, Double b) {
        return a / b;
    }

    @Override
    public Double mod(Double a, Double b) {
        return a % b;
    }

    @Override
    public Double neg(Double a) {
        return -a;
    }

    @Override
    public Double abs(Double a) {
        return Math.abs(a);
    }

    @Override
    public Double square(Double a) {
        return a * a;
    }

    @Override
    public Double valueOf(int a) {
        return (double) a;
    }
}
