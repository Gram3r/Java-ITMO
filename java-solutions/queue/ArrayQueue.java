package queue;

public class ArrayQueue extends AbstractQueue {
    private int head = 0;
    private Object[] elements = new Object[2];

    protected void enqueueImpl(Object el) {
        ensureCapacity(size + 1);
        elements[getTail()] = el;
    }

    protected void pushImpl(Object el) {
        ensureCapacity(size + 1);
        head = (head - 1 + elements.length) % elements.length;
        elements[head] = el;
    }


    protected Object dequeueImpl() {
        Object res = elements[head];
        elements[head] = null;
        head = (head + 1) % elements.length;
        return res;
    }

    protected Object popImpl() {
        int newtail = (head + size - 1 + elements.length) % elements.length;
        Object res = elements[newtail];
        elements[newtail] = null;
        return res;
    }

    public Object element() {
        return elements[head];
    }

    public Object peek() {
        return elements[(head + size - 1 + elements.length) % elements.length];
    }


    protected void clearImpl() {
        head = 0;
        elements = new Object[2];
    }

    protected Queue createNew() {
        return new ArrayQueue();
    }


    private void ensureCapacity(int capacity) {
        if (capacity > elements.length) {
            elements = copy(capacity * 2);
            head = 0;
        }
    }

    private int getTail() {
        return (head + size) % elements.length;
    }

    private Object[] copy(int length) {
        Object[] elementsnew = new Object[length];
        if (head < getTail() || size == 0) {
            System.arraycopy(elements, head, elementsnew, 0, size);
        } else {
            System.arraycopy(elements, head, elementsnew, 0, elements.length - head);
            System.arraycopy(elements, 0, elementsnew, elements.length - head, getTail());
        }
        return elementsnew;
    }
}
