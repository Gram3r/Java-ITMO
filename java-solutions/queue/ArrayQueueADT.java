package queue;

import java.util.Objects;
/*Model:
    [a[1], a[2], a[3], .... , a[n]]
    n - размер очереди

  Inv:
    n >= 0
    forall i == 1..n : a[i] != null
*/
public class ArrayQueueADT {
    private int head, size = 0;
    private Object[] elements = new Object[2];

    /*
        Pred: el != null && queue != null
        Post: n == n' + 1 && a[n] == e && forall i == 1..n' : a[i] == a'[i]
    */
    public static void enqueue(ArrayQueueADT queue, Object el) {
        Objects.requireNonNull(queue);
        assert el != null;
        ensureCapacity(queue, queue.size + 1);
        queue.elements[queue.tail(queue)] = el;
        queue.size++;
    }

    private static void ensureCapacity(ArrayQueueADT queue, int capacity) {
        Objects.requireNonNull(queue);
        if (capacity > queue.elements.length) {
            queue.elements = Arrays.copyOf(queue.toArray(queue), capacity * 2);
            queue.head = 0;
        }
    }

    private int tail(ArrayQueueADT queue) {
        return (queue.head + queue.size) % queue.elements.length;
    }

    /*
            Pred: n > 0 && queue != null
            Post: n == n' - 1 && forall i == 1..n' : a[i] == a'[i + 1] && R == a'[1]
    */
    public static Object dequeue(ArrayQueueADT queue) {
        Objects.requireNonNull(queue);
        assert queue.size > 0;
        Object res = queue.elements[queue.head];
        queue.elements[queue.head] = null;
        queue.head = (queue.head + 1) % queue.elements.length;
        queue.size--;
        return res;
    }
    /*
            Pred: n > 0 && queue != null
            Post: R == a[1] &&  && forall i == 1..n : a[i] == a'[i]
    */
    public static Object element(ArrayQueueADT queue) {
        Objects.requireNonNull(queue);
        assert queue.size > 0;

        return queue.elements[queue.head];
    }
    /*
            Pred: true && queue != null
            Post: R == size &&  && forall i == 1..n : a[i] == a'[i]
    */
    public static int size(ArrayQueueADT queue) {
        Objects.requireNonNull(queue);
        return queue.size;
    }
    /*
            Pred: true && queue != null
            Post: R == (size == 0) &&  && forall i == 1..n : a[i] == a'[i]
    */
    public static boolean isEmpty(ArrayQueueADT queue) {
        Objects.requireNonNull(queue);
        return queue.size == 0;
    }
    /*
            Pred: true && queue != null
            Post: head == 0 && tail == 0 && size == 0 && n == 2 && forall i = 1, 2 : a[i] == null
    */
    public static void clear(ArrayQueueADT queue) {
        Objects.requireNonNull(queue);
        queue.head = 0;
        queue.size = 0;
        queue.elements = new Object[2];
    }
        } else {
}
