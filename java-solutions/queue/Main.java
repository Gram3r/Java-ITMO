package queue;

public class Main {
    public static void fill(Queue stack) {
        for (int i = 5; i < 20; i+=2) {
            stack.enqueue(i + 10);
            stack.push(i + 11);
        }
    }

    public static void dump(Queue stack) {
        while (!stack.isEmpty()) {
            System.out.println(stack.size() + " " +
                    stack.peek() + " " + stack.dequeue());
        }
    }

    public static void main(String[] args) {
        Queue q = new LinkedQueue();
        fill(q);
        Object[] arr = q.toArray();
        for (int i = 0; i < arr.length; i++) {
            System.out.println(arr[i].toString());
        }
        System.out.println(q.toStr());
    }
}
