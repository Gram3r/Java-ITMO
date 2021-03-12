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

    public Queue getNth(int n){
        Queue new_que = create_new();
        int temp_size = size;
        for (int i = 0; i < temp_size; i++) {
            Object obj = dequeue();
            if ((i + 1) % n == 0) {
                new_que.enqueue(obj);
            }
            enqueue(obj);
        }
        return new_que;
    }

    public void dropNth(int n){
        int temp_size = size;
        for (int i = 0; i < temp_size; i++) {
            Object obj = dequeue();
            if ((i + 1) % n != 0) {
                enqueue(obj);
            }
        }
    }

    public Queue removeNth(int n){
        Queue que = getNth(n);
        dropNth(n);
        return que;
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

    public void clear(){
        size = 0;
        clearImpl();
    }


    protected abstract void enqueueImpl(final Object el);

    protected abstract Object dequeueImpl();

    protected abstract void pushImpl(final Object el);

    protected abstract Object popImpl();

    protected abstract void clearImpl();

    protected abstract Queue create_new();
}
