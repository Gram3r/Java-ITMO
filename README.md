## Expression parser and evaluating in different types

Add to the program parsing and calculating expressions of three variables support for calculations in various types.
1. Create the class `expression.class.generic.GenericTabulator`, which implements the `expression.interface.generic.Tabulator`:
   Arguments

   * 'mode` — operating mode
   * 'expression` — calculated expression;
   * `x1` `x2` `y1` `y2` `z1` `z2` - ranges of variable changes  (inclusive).
     The return value is a table of function values, where `R[i][j][k]` corresponds to `x = x1 + i` `y = y1 + j` `z = z1 +k`. If the calculation failed, there should be `null` in the corresponding cell.

2. Refine the command line interface:
* The first command-line argument of the program must accept an indication of the type in which calculations will be performed:


   `i` – calculations in `int` with overflow check;

   `d` – calculations in `double` without overflow check;

   `bi` – calculations in `BigInteger`.

* The second command-line argument of the program must accept an expression for calculation.
* The program should output the calculation results for all integer values of variables from the range -2..2.
3. The implementation should not contain unverifiable type conversions
4. When performing a task, pay attention to the ease of adding new types and operations.
5. __Modification of__ *AsmUpb*

   * Additionally implement unary operations:
     * `abs` is the modulus of a number `abs -5` is equal to `5`;
     * `square` - squaring `square 5` is `25`.
   * Additionally implement binary operation (maximum priority):
     * `mod` - taking modulo, priority as multiplication (`1 + 5 mod 3` equals `1 + (5 mod 3)` equals `3`).
   * Additionally implement support for the following modes:
     * `u` - calculations in `int` without checking for overflow;
     * `p` - calculations in integers modulo `1009`;
     * `b` - calculations in `byte` without checking for overflow.
