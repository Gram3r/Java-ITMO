package queue;

public class ArrayQueue extends AbstractQueue {
    private int head = 0;
    private Object[] elements = new Object[2];

    protected void enqueueImpl(Object el) {
        ensureCapacity(size);
        elements[getTail()] = el;
    }

    protected Object dequeueImpl() {
        Object res = elements[head];
        elements[head] = null;
        head = (head + 1) % elements.length;
        return res;
    }

    public Object element() {
        assert size > 0;

        return elements[head];
    }

    public void clear() {
        head = 0;
        size = 0;
        elements = new Object[2];
    }


    private void ensureCapacity(int capacity) {
        if (capacity > elements.length) {
            elements = copy(capacity * 2);
            head = 0;
        }
    }

    private int getTail() {
        return (head + size - 1 + elements.length) % elements.length;
    }

    private Object[] copy(int length) {
        Object[] elementsnew = new Object[length];
        if (head < getTail() || size == 0) {
            System.arraycopy(elements, head, elementsnew, 0, size - 1);
        } else {
            System.arraycopy(elements, head, elementsnew, 0, elements.length - head);
            System.arraycopy(elements, 0, elementsnew, elements.length - head, getTail());
        }
        return elementsnew;
    }
}
