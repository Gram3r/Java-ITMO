package queue;

public class Main {
    public static void fill(ArrayQueue stack) {
        for (int i = 5; i < 20; i++) {
            stack.enqueue(i);
        }
    }

    public static void dump(ArrayQueue stack) {
        while (!stack.isEmpty()) {
            System.out.println(stack.size() + " " +
                    stack.element() + " " + stack.dequeue());
        }
    }

    public static void main(String[] args) {
        ArrayQueue stack = new ArrayQueue();
        fill(stack);
        fill(stack);
        dump(stack);
    }
}
