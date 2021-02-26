package queue;

public class ArrayQueueModuleTest {
    public static void fill() {
        for (int i = 10; i >= 1; i--) {
            ArrayQueueModule.enqueue(i*10);
            ArrayQueueModule.push(i*10);
        }
    }

    public static void dump() {
        while (!ArrayQueueModule.isEmpty()) {
            System.out.println(
                ArrayQueueModule.size() + " " +
                ArrayQueueModule.element() + " " +
                ArrayQueueModule.dequeue()
            );
        }
    }

    public static void main(String[] args) {
        fill();
        fill();
        //dump();
        Object[] arr = ArrayQueueModule.toArray();
        for (int i = 0; i < arr.length; i++) {
            System.out.println(arr[i].toString());
        }
        System.out.println(ArrayQueueModule.toStr());
    }
}
