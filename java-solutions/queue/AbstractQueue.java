package queue;

import java.util.Arrays;
import java.util.Objects;

public abstract class AbstractQueue implements Queue {
    protected int size = 0;

    public int size(){
        return size;
    }

    public boolean isEmpty(){
        return size == 0;
    }

    public void enqueue(Object el){
        Objects.requireNonNull(el);
        enqueueImpl(el);
        size++;
    }

    public void push(Object el){
        Objects.requireNonNull(el);
        pushImpl(el);
        size++;
    }

    public Object dequeue(){
        assert size > 0;
        Object res = dequeueImpl();
        size--;
        return res;
    }

    public Object pop(){
        assert size > 0;
        Object res = popImpl();
        size--;
        return res;
    }


    public String toStr(){
        StringBuilder str = new StringBuilder();
        str.append('[');
        toStrImpl(str);
        str.append(']');
        return str.toString();
        //return Arrays.toString(toArray());
    }


    protected abstract void enqueueImpl(final Object el);

    protected abstract Object dequeueImpl();

    protected abstract void pushImpl(final Object el);

    protected abstract Object popImpl();

    protected abstract String toStrImpl(StringBuilder str);
}
