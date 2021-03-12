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
        Pred: el != null
        Post: n == n' + 1 && a[1] == e && forall i == 1..n' : a[i + 1] == a'[i]
    */
    void push(Object el);


    /*
            Pred: n > 0
            Post: R == a[n] && forall i == 1..n : a[i] == a'[i]
    */
    Object peek();


    /*
            Pred: n > 0
            Post: n == n' - 1 && forall i == 1..n : a[i] == a'[i] && R == a'[n']
    */
    Object pop();


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


    /*
        Pred: true
        Post: R == [a[1], a[2], ... , a[n]] && forall i == 1..n : a[i] == a'[i]
    */
    Object[] toArray();


    /*
            Pred: true
            Post: R == String(a[1], a[2], ... , a[n]) && forall i == 1..n : a[i] == a'[i]
    */
    String toStr();
}
