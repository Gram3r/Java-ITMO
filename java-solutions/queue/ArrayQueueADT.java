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


    private static void ensureCapacity(ArrayQueueADT queue, int capacity) {
        Objects.requireNonNull(queue);
        if (capacity > queue.elements.length) {
            queue.elements = copy(queue, capacity * 2);
            queue.head = 0;
        }
    }

    private static int getTail(ArrayQueueADT queue) {
        return (queue.head + queue.size) % queue.elements.length;
    }

    private static Object[] copy(ArrayQueueADT queue, int length){
        Objects.requireNonNull(queue);
        Object[] elementsnew = new Object[length];
        if (queue.head < queue.getTail(queue) || queue.size == 0) {
            System.arraycopy(queue.elements, queue.head, elementsnew, 0, queue.size);
        } else {
            System.arraycopy(queue.elements, queue.head, elementsnew, 0, queue.elements.length - queue.head);
            System.arraycopy(queue.elements, 0, elementsnew, queue.elements.length - queue.head, queue.getTail(queue));
        }
        return elementsnew;
    }

    /*
    Pred: el != null && queue != null
    Post: n == n' + 1 && a[n] == e && forall i == 1..n' : a[i] == a'[i]
    */
    public static void enqueue(ArrayQueueADT queue, Object el) {
        Objects.requireNonNull(queue);
        assert el != null;
        ensureCapacity(queue, queue.size + 1);
        queue.elements[queue.getTail(queue)] = el;
        queue.size++;
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
    /*
            Pred: true && queue != null
            Post: R == [a[1], a[2], ... , a[n]] && forall i == 1..n : a[i] == a'[i]
    */
    public static Object[] toArray(ArrayQueueADT queue){
        return copy(queue, queue.size);
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

        return queue.elements[(queue.getTail(queue) - 1 + queue.elements.length) % queue.elements.length];
    }
    /*
            Pred: n > 0 && queue != null
            Post: n == n' - 1 && forall i == 1..n : a[i] == a'[i] && R == a'[n']
    */
    public static Object remove(ArrayQueueADT queue) {
        Objects.requireNonNull(queue);
        assert queue.size > 0;
        int newtail = (queue.getTail(queue) - 1 + queue.elements.length) % queue.elements.length;
        Object res = queue.elements[newtail];
        queue.elements[newtail] = null;
        queue.size--;
        return res;
    }
}