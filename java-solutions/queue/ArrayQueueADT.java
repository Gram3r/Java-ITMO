package queue;

/*Model:
    [null,...,null,a[head],...,a[tail],null,...,null]
    n - размер очереди

  Inv:
    n >= 0
    forall i == (head..tail + n) % n : a[i] != null
*/

public class ArrayQueueADT {
    private int head, tail, size = 0;
    private Object[] elements = new Object[2];

    /*
        Pred: el != null
        Post: tail == tail' + 1 && a[tail] == e && forall i = (head.. tail' + n) % n : a[i] == a'[i]
    */
    public static void enqueue(ArrayQueueADT queue, Object el) {
        assert el != null;
        ensureCapacity(queue, queue.size + 1);
        queue.elements[queue.tail] = el;
        queue.tail = (queue.tail + 1) % queue.elements.length;
        queue.size++;
    }

    private static void ensureCapacity(ArrayQueueADT queue, int capacity) {
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
            Pred: n > 0
            Post: n == n' - 1 && a[n] == e && forall i = (head.. tail + n) % n : a[i] == a'[i] && R == a'[n']
    */
    public static Object dequeue(ArrayQueueADT queue) {
        assert queue.size > 0;
        Object res = queue.elements[queue.head];
        queue.elements[queue.head] = null;
        queue.head = (queue.head + 1) % queue.elements.length;
        queue.size--;
        return res;
    }
    /*
            Pred: n > 0
            Post: R == a[head] &&  && forall i = (head.. tail + n) % n : a[i] == a'[i]
    */
    public static Object element(ArrayQueueADT queue) {
        assert queue.size > 0;

        return queue.elements[queue.head];
    }
    /*
            Pred: true
            Post: R == size &&  && forall i = (head.. tail + n) % n : a[i] == a'[i]
    */
    public static int size(ArrayQueueADT queue) {
        return queue.size;
    }
    /*
            Pred: true
            Post: R == (size == 0) &&  && forall i = (head.. tail + n) % n : a[i] == a'[i]
    */
    public static boolean isEmpty(ArrayQueueADT queue) {
        return queue.size == 0;
    }
    /*
            Pred: true
            Post: head == 0 && tail == 0 && size == 0 && n == 2 && forall i = 1..2 : a[i] == null
    */
    public static void clear(ArrayQueueADT queue) {
        queue.head = 0;
        queue.tail = 0;
        queue.size = 0;
        queue.elements = new Object[2];
    }
}
