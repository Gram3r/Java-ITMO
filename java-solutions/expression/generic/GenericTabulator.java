package expression.generic;

import expression.GenOperations.*;
import expression.TripleExpression;
import expression.parser.ExpressionParser;
import expression.parser.Parser;

import java.util.Map;

public class GenericTabulator implements Tabulator {
    public static void main(String[] args) {
        GenericTabulator tabul = new GenericTabulator();
        Object[][][] ans;
        try {
            ans = tabul.tabulate(args[0].substring(1), args[1], -2, 2, -2, 2, -2, 2);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                for (int k = 0; k < 5; k++) {
                    System.out.println("i: " + (i - 2) + "; j: " + (j - 2) + "; k: " + (k - 2));
                    System.out.println("Result: " + ans[i][j][k]);
                    System.out.println();
                }
            }
        }

    }


    Map<String, AllGenericOperations<?>> modes = Map.of(
            "i", new IntOperations(),
            "d", new DoubleOperations(),
            "bi", new BigIntOperations(),
            "u", new UIntOperations(),
            "p", new PIntOperations(),
            "b", new ByteOperations()
    );

    @Override
    public Object[][][] tabulate(String mode, String expression, int x1, int x2, int y1, int y2, int z1, int z2) throws Exception {
        Object[][][] res = new Object[x2 - x1 + 1][y2 - y1 + 1][z2 - z1 + 1];
        AllGenericOperations<?> myGeneric;
        if (modes.containsKey(mode)) {
            myGeneric = modes.get(mode);
        } else {
            throw new Exception("Unexpected value: " + mode);
        }

        Parser<?> parser = new ExpressionParser<>(myGeneric);
        TripleExpression<?> exp = parser.parse(expression);
        System.out.println(exp.toString());
        for (int i = x1; i <= x2; i++) {
            for (int j = y1; j <= y2; j++) {
                for (int k = z1; k <= z2; k++) {
                    try {
                        res[i - x1][j - y1][k - z1] = exp.evaluate(i, j, k);
                    } catch (ArithmeticException e) {}
                }
            }
        }
        return res;
    }
}
