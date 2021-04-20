package queue;

/*Model:
    [a[1], a[2], a[3], .... , a[n]]
    n - размер очереди

  Inv:
    n >= 0
    forall i == 1..n : a[i] != null
*/

public class ArrayQueueModule {
    private static int head, size = 0;
    private static Object[] elements = new Object[2];



    private static void ensureCapacity(int capacity) {
        if (capacity > elements.length) {
            elements = copy(capacity * 2);
            head = 0;
        }
    }

    private static int getTail() {
        return (head + size) % elements.length;
    }

    private static Object[] copy(int length){
        Object[] elementsnew = new Object[length];
        if (head < getTail() || size == 0) {
            System.arraycopy(elements, head, elementsnew, 0, size);
        } else {
            System.arraycopy(elements, head, elementsnew, 0, elements.length - head);
            System.arraycopy(elements, 0, elementsnew, elements.length - head, getTail());
        }
        return elementsnew;
    }
    /*
    Pred: el != null
    Post: n == n' + 1 && a[n] == e && forall i == 1..n' : a[i] == a'[i]
    */
    public static void enqueue(Object el) {
        assert el != null;
        ensureCapacity(size + 1);
        elements[getTail()] = el;
        size++;
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
    /*
            Pred: true
            Post: R == [a[1], a[2], ... , a[n]] && forall i == 1..n : a[i] == a'[i]
    */
    public static Object[] toArray(){
        return copy(size);
    }
    /*
            Pred: true
            Post: R == String(a[1], a[2], ... , a[n]) && forall i == 1..n : a[i] == a'[i]
    */
    public static String toStr(){
        StringBuilder str = new StringBuilder();
        str.append('[');
        for (int i = 0; i < size; i++) {
            if (i != 0) {
                str.append(", ");
            }
            str.append(elements[(head + i) % elements.length].toString());
        }
        str.append(']');
        return str.toString();
    }
    /*
        Pred: el != null
        Post: n == n' + 1 && a[1] == e && forall i == 1..n' : a[i + 1] == a'[i]
    */
    public static void push(Object el) {
        assert el != null;
        ensureCapacity(size + 1);
        head = (head - 1 + elements.length) % elements.length;
        elements[head] = el;
        size++;
    }
    /*
            Pred: n > 0
            Post: R == a[n] && forall i == 1..n : a[i] == a'[i]
    */
    public static Object peek() {
        assert size > 0;

        return elements[(head + size - 1 + elements.length) % elements.length];
    }
    /*
            Pred: n > 0
            Post: n == n' - 1 && forall i == 1..n : a[i] == a'[i] && R == a'[n']
    */
    public static Object remove() {
        assert size > 0;
        int newtail = (head + size - 1 + elements.length) % elements.length;
        Object res = elements[newtail];
        elements[newtail] = null;
        size--;
        return res;
    }
}
