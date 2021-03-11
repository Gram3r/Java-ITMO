package queue;

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
        size++;

        enqueueImpl(el);
    }

    public Object dequeue(){
        assert size > 0;
        size--;

        return dequeueImpl();
    }


    protected abstract void enqueueImpl(final Object el);

    protected abstract Object dequeueImpl();
}
