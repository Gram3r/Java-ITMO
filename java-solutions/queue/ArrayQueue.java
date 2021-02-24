package queue;

/*Model:
    [null,...,null,a[head],...,a[tail],null,...,null]
    n - размер очереди

  Inv:
    n >= 0
    forall i == (head..tail + n) % n : a[i] != null
*/

public class ArrayQueue {
    private int head, tail, size = 0;
    private Object[] elements = new Object[2];

    /*
        Pred: el != null
        Post: tail == tail' + 1 && a[tail] == e && forall i = (head.. tail' + n) % n : a[i] == a'[i]
    */
    public void enqueue(ArrayQueue this, Object el) {
        assert el != null;
        ensureCapacity(size + 1);
        elements[tail] = el;
        tail = (tail + 1) % elements.length;
        size++;
    }

    private void ensureCapacity(int capacity) {
        if (capacity > elements.length) {
            Object[] elements2 = new Object[capacity*2];
            for (int i = 0; i < size; i++) {
                elements2[i] = elements[head];
                head = (head + 1) % elements.length;
            }
            head = 0;
            tail = size;
            elements = elements2;
        }
    }
    /*
            Pred: n > 0
            Post: n == n' - 1 && a[n] == e && forall i = (head.. tail + n) % n : a[i] == a'[i] && R == a'[n']
    */
    public Object dequeue() {
        assert size > 0;
        Object res = elements[head];
        elements[head] = null;
        head = (head + 1) % elements.length;
        size--;
        return res;
    }
    /*
            Pred: n > 0
            Post: R == a[head] &&  && forall i = (head.. tail + n) % n : a[i] == a'[i]
    */
    public Object element() {
        assert size > 0;

        return elements[head];
    }
    /*
            Pred: true
            Post: R == size &&  && forall i = (head.. tail + n) % n : a[i] == a'[i]
    */
    public int size() {
        return size;
    }
    /*
            Pred: true
            Post: R == (size == 0) &&  && forall i = (head.. tail + n) % n : a[i] == a'[i]
    */
    public boolean isEmpty() {
        return size == 0;
    }
    /*
            Pred: true
            Post: head == 0 && tail == 0 && size == 0 && n == 2 && forall i = 1..2 : a[i] == null
    */
    public void clear() {
        head = 0;
        tail = 0;
        size = 0;
        elements = new Object[2];
    }
}
