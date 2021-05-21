composite(0).
composite(1).
prime(N) :- not composite(N).

initial(I, N) :- I < sqrt(N), I1 is I + 1, not initial(I1, N), not composite(I), I2 is I * I, add_composite(I2, N, I).
add_composite(I, N, S) :- I < N, assert(composite(I)), I1 is I + S, add_composite(I1, N, S).

init(N) :- N1 is N + 1, initial(2, N1).

min_divisor(N, H, D) :- M is N mod D, M = 0, prime(D), H = D.
min_divisor(N, H, D) :- D1 is D + 1, D1 =< sqrt(N), min_divisor(N, H, D1).

find_divisors(1, [], _).
find_divisors(N, [N], _) :- prime(N).
find_divisors(N, [H | T], D) :- min_divisor(N, H, D), N1 is N / H, find_divisors(N1, T, D).

prime_divisors(N, L) :- not is_list(L), find_divisors(N, L, 2), !.

in_order([]).
in_order([A]).
in_order([A, B | T]) :- A =< B, in_order([B | T]), !.

mul(R, []) :- R is 1.
mul(R, [H | T]) :- mul(R1, T), R is R1 * H.
prime_divisors(N, L) :- is_list(L), in_order(L), mul(N, L), !.

find_gcd([], _, [1]) :- !.
find_gcd(_, [], [1]) :- !.
find_gcd([H1 | T1], [H2 | T2], [RH | RT]) :- H1 = H2, !, RH = H1, find_gcd(T1, T2, RT).
find_gcd([H1 | T1], [H2 | T2], T) :- H1 < H2, !, find_gcd(T1, [H2 | T2], T).
find_gcd([H1 | T1], [H2 | T2], T) :- H1 > H2, !, find_gcd([H1 | T1], T2, T).

gcd(A, B, GCD) :- prime_divisors(A, R1), prime_divisors(B, R2), find_gcd(R1, R2, Res), mul(GCD, Res).

find_lcm([], R, R) :- !.
find_lcm(R, [], R) :- !.
find_lcm([H1 | T1], [H2 | T2], [RH | RT]) :- H1 = H2, !, RH = H1, find_lcm(T1, T2, RT).
find_lcm([H1 | T1], [H2 | T2], [RH | RT]) :- H1 < H2, !, RH = H1, find_lcm(T1, [H2 | T2], RT).
find_lcm([H1 | T1], [H2 | T2], [RH | RT]) :- H1 > H2, !, RH = H2, find_lcm([H1 | T1], T2, RT).

lcm_fast(A, B, LCM) :- prime_divisors(A, R1), prime_divisors(B, R2), find_lcm(R1, R2, Res), mul(LCM, Res).

lcm(A, B, LCM) :- prime_divisors(A, R1), prime_divisors(B, R2), gcd(A, B, GCD), mul(Res1, R1), mul(Res2, R2), MUL is Res1 * Res2, LCM is MUL / GCD.