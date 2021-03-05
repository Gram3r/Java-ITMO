package queue;

/*Model:
    [a[1], a[2], a[3], .... , a[n]]
    n - размер очереди

  Inv:
    n >= 0
    forall i == 1..n : a[i] != null
*/

import java.util.Arrays;

public class ArrayQueueModule {
    private static int head, size = 0;
    private static Object[] elements = new Object[2];

/*
    Pred: el != null
    Post: n == n' + 1 && a[n] == e && forall i == 1..n' : a[i] == a'[i]
*/
    public static void enqueue(Object el) {
        assert el != null;
        ensureCapacity(size + 1);
        elements[tail()] = el;
        size++;
    }

    private static void ensureCapacity(int capacity) {
        if (capacity > elements.length) {
            elements = Arrays.copyOf(toArray(), capacity * 2);
            head = 0;
        }
    }

    private static int tail() {
        return (head + size) % elements.length;
    }
/*
        Pred: n > 0
        Post: n == n' - 1 && forall i == 1..n' : a[i] == a'[i + 1] && R == a'[1]
*/
    public static Object dequeue() {
        assert size > 0;
        Object res = elements[head];
        elements[head] = null;
        head = (head + 1) % elements.length;
        size--;
        return res;
    }
/*
        Pred: n > 0
        Post: R == a[1] &&  && forall i == 1..n : a[i] == a'[i]
*/
    public static Object element() {
        assert size > 0;

        return elements[head];
    }
/*
        Pred: true
        Post: R == size &&  && forall i == 1..n : a[i] == a'[i]
*/
    public static int size() {
        return size;
    }
/*
        Pred: true
        Post: R == (size == 0) &&  && forall i == 1..n : a[i] == a'[i]
*/
    public static boolean isEmpty() {
        return size == 0;
    }
/*
        Pred: true
        Post: head == 0 && tail == 0 && size == 0 && n == 2 && forall i = 1, 2 : a[i] == null
*/
    public static void clear() {
        head = 0;
        size = 0;
        elements = new Object[2];
    }
        return elementsnew;
}
