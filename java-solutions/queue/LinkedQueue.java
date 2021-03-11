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


    public Object element() {
        assert size > 0;

        return head.value;
    }


    public void clear() {
        size = 0;
        head = null;
        tail = null;
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
