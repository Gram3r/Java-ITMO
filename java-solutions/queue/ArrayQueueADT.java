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
    private int head, tail, size = 0;
    private Object[] elements = new Object[2];

    /*
        Pred: el != null && queue != null
        Post: n == n' + 1 && a[n] == e && forall i == 1..n' : a[i] == a'[i]
    */
    public static void enqueue(ArrayQueueADT queue, Object el) {
        Objects.requireNonNull(queue);
        assert el != null;
        ensureCapacity(queue, queue.size + 1);
        queue.elements[queue.tail] = el;
        queue.tail = (queue.tail + 1) % queue.elements.length;
        queue.size++;
    }

    private static void ensureCapacity(ArrayQueueADT queue, int capacity) {
        Objects.requireNonNull(queue);
        if (capacity > queue.elements.length) {
            Object[] elements2 = new Object[capacity*2];
            for (int i = 0; i < queue.size; i++) {
                elements2[i] = queue.elements[queue.head];
                queue.head = (queue.head + 1) % queue.elements.length;
            }
            queue.head = 0;
            queue.tail = queue.size;
            queue.elements = elements2;
        }
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
        queue.tail = 0;
        queue.size = 0;
        queue.elements = new Object[2];
    }
    /*
            Pred: true && queue != null
            Post: R == [a[1], a[2], ... , a[n]] && forall i == 1..n : a[i] == a'[i]
    */
    public static Object[] toArray(ArrayQueueADT queue){
        Objects.requireNonNull(queue);
        Object[] arr = new Object[queue.size];
        for (int i = 0; i < queue.size; i++) {
            arr[i] = queue.elements[(queue.head + i) % queue.elements.length];
        }
        return arr;
    }
    /*
            Pred: true && queue != null
            Post: R == String([a[1], a[2], ... , a[n]) && forall i == 1..n : a[i] == a'[i]
    */
    public static String toStr(ArrayQueueADT queue){
        Objects.requireNonNull(queue);
        StringBuilder str = new StringBuilder();
        str.append('[');
        for (int i = 0; i < queue.size; i++) {
            if (i != 0) {
                str.append(", ");
            }
            str.append(queue.elements[(queue.head + i) % queue.elements.length].toString());
        }
        str.append(']');
        return str.toString();
    }
    /*
        Pred: el != null && queue != null
        Post: n == n' + 1 && a[1] == e && forall i == 1..n' : a[i + 1] == a'[i]
    */
    public static void push(ArrayQueueADT queue, Object el) {
        Objects.requireNonNull(queue);
        assert el != null;
        ensureCapacity(queue, queue.size + 1);
        queue.head = (queue.head - 1 + queue.elements.length) % queue.elements.length;
        queue.elements[queue.head] = el;
        queue.size++;
    }
    /*
            Pred: n > 0 && queue != null
            Post: R == a[n] && forall i == 1..n : a[i] == a'[i]
    */
    public static Object peek(ArrayQueueADT queue) {
        Objects.requireNonNull(queue);
        assert queue.size > 0;

        return queue.elements[(queue.tail - 1 + queue.elements.length) % queue.elements.length];
    }
    /*
            Pred: n > 0 && queue != null
            Post: n == n' - 1 && forall i == 1..n : a[i] == a'[i] && R == a'[n']
    */
    public static Object remove(ArrayQueueADT queue) {
        Objects.requireNonNull(queue);
        assert queue.size > 0;
        int newtail = (queue.tail - 1 + queue.elements.length) % queue.elements.length;
        Object res = queue.elements[newtail];
        queue.elements[newtail] = null;
        queue.tail = newtail;
        queue.size--;
        return res;
    }
}
