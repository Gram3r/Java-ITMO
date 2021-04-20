package queue;

import javax.swing.*;
import java.util.Objects;

public class LinkedQueue extends AbstractQueue {
    private Node head, tail;

    protected void enqueueImpl(Object el) {
        tail = new Node(el, tail, null);

        if (head == null) {
            head = tail;
        } else {
            tail.next.prev = tail;
        }
    }

    protected Object dequeueImpl() {
        Object res = head.value;
        head = head.prev;
        return res;
    }

    protected void pushImpl(Object el) {
        head = new Node(el, null, head);

        if (tail == null) {
            tail = head;
        } else {
            head.prev.next = head;
        }
    }

    protected Object popImpl() {
        Object res = tail.value;
        tail = tail.next;
        return res;
    }


    public Object element() {
        return head.value;
    }

    public Object peek() {
        return tail.value;
    }


    protected void clearImpl() {
        head = null;
        tail = null;
    }


    public Object[] toArray(){
        Object[] array = new Object[size];
        Node temp = head;
        for (int i = 0; i < size; i++) {
            array[i] = temp.value;
            temp = temp.prev;
        }
        return array;
    }

    protected void toStrImpl(StringBuilder str){
        Node temp = head;
        for (int i = 0; i < size; i++) {
            if (i != 0) {
                str.append(", ");
            }
            str.append(temp.value.toString());
            temp = temp.prev;
        }
    }

    protected Queue createNew() {
        return new LinkedQueue();
    }

    private static class Node {
        private final Object value;
        private Node next;
        private Node prev;

        public Node(Object value, Node next, Node prev) {
            Objects.requireNonNull(value);

            this.value = value;
            this.next = next;
            this.prev = prev;
        }
    }
}
