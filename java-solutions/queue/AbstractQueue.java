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

    public Queue getNth(int n){
        return changeNth(n, true);
    }

    public void dropNth(int n){
        // :NOTE: можно вынести больше общего кода
        changeNth(n, false);
    }

    public Queue removeNth(int n){
        return changeNth(n, false);
    }


    private Queue changeNth(int n, boolean first) {
        Queue newQue = createNew();
        int tempSize = size;
        for (int i = 0; i < tempSize; i++) {
            Object obj = dequeue();
            if ((i + 1) % n == 0) {
                newQue.enqueue(obj);
            }
            if (first || (i + 1) % n != 0) {
                enqueue(obj);
            }
        }
        return newQue;
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

    protected abstract Queue createNew();
}
