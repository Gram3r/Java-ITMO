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

    protected Queue create_new() {
        Queue queue = new LinkedQueue();
        return queue;
    }

    private class Node {
        private Object value;
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
