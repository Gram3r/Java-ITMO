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


down_prime(2, 1) :- !.
down_prime(P, N) :- prime(P), !, P1 is P - 1, down_prime(P1, N1), N is N1 + 1.
down_prime(P, N) :- P1 is P - 1, down_prime(P1, N).

up_prime(P, 0, I) :- P is I - 1, !.
up_prime(P, N, I) :- prime(I), !, I1 is I + 1, N1 is N - 1, up_prime(P, N1, I1).
up_prime(P, N, I) :- I1 is I + 1, up_prime(P, N, I1).

prime_index(P, N) :- number(P), !, prime(P), down_prime(P, N).
prime_index(P, N) :- up_prime(P, N, 2).




