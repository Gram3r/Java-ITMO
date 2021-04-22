package expression;

import expression.GenOperations.AllGenericOperations;

public class Variable<T> implements GenericTripleExpression<T> {
    private final String name;

    final AllGenericOperations<T> calc;

    public Variable(String name, AllGenericOperations<T> calc) {
        this.name = name;
        this.calc = calc;
    }

    @Override
    public T evaluate(int x, int y, int z) {
        return switch (name) {
            case "x" -> calc.valueOf(x);
            case "y" -> calc.valueOf(y);
            case "z" -> calc.valueOf(z);
            default -> throw new IllegalStateException("Unknown variable");
        };
    }

    @Override
    public boolean equals(Object second) {
        return second != null
                && second.getClass() == getClass()
                && this.name.equals(((Variable <?>)second).name);
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }
}
