package queue;

public class Main {
    public static void fill(ArrayQueue stack) {
        for (int i = 5; i < 20; i++) {
            //stack.push(i);
            stack.enqueue(i + 10);
        }
    }

    public static void dump(ArrayQueue stack) {
        while (!stack.isEmpty()) {
            System.out.println(stack.size() + " " +
                    stack.element() + " " + stack.dequeue());
        }
    }

    public static void main(String[] args) {
        ArrayQueue q = new ArrayQueue();
        q.enqueue(5);
        q.enqueue(4);
        q.enqueue(3);
        q.enqueue(2);
        q.enqueue(1);
        System.out.println(q.dequeue());

        System.out.println(q.element());

        System.out.println(q.dequeue());
        System.out.println(q.dequeue());
    }
}
