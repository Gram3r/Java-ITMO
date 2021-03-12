package queue;

import javax.management.QueryEval;

public class Main {
    public static void fill(Queue stack) {
        for (int i = 0; i < 20; i+=1) {
            stack.enqueue(i);
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
        Queue que = q.getNth(200);
        dump(que);
        System.out.println();
        //dump(q);
    }
}
