package queue;

import javax.management.QueryEval;

public class Main {
    public static void fill(Queue stack) {
        for (int i = 1; i < 9; i+=1) {
            stack.enqueue(i);
        }
    }

    public static void dump(Queue stack) {
        while (!stack.isEmpty()) {
            System.out.println(stack.dequeue());
        }
    }

    public static void main(String[] args) {
        Queue q = new LinkedQueue();
        fill(q);
        Queue que = q.getNth(3);
        dump(que);
        //System.out.println();
        //dump(q);
    }
}
