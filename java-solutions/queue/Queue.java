package queue;

/*Model:
    [a[1], a[2], a[3], .... , a[n]]
    n - размер очереди

  Inv:
    n >= 0
    forall i == 1..n : a[i] != null
*/

public interface Queue {

    /*
            Pred: el != null
            Post: n == n' + 1 && a[n] == e && forall i == 1..n' : a[i] == a'[i]
    */
    void enqueue(Object el);



    /*
            Pred: n > 0
            Post: n == n' - 1 && forall i == 1..n' : a[i] == a'[i + 1] && R == a'[1]
    */
    Object dequeue();



    /*
            Pred: n > 0
            Post: R == a[1] &&  && forall i == 1..n : a[i] == a'[i]
    */
    Object element();



    /*
            Pred: true
            Post: head == 0 && tail == 0 && size == 0 && n == 2 && forall i = 1, 2 : a[i] == null
    */
    void clear();



    /*
            Pred: true
            Post: R == size &&  && forall i == 1..n : a[i] == a'[i]
    */
    int size();



    /*
            Pred: true
            Post: R == (size == 0) &&  && forall i == 1..n : a[i] == a'[i]
    */
    boolean isEmpty();
}
