package search;

public class BinarySearchMax {
    //Pred: exists ind : forall i = 0 .. ind - 1 : args[i] < args[i+1] && forall i = ind .. args.length - 1 : args[i] > args[i+1]
    // arr[ind] = Max;
    //Inv: l < ind <= r
    //Post: R = arr[ind]
    private static int recbinsearch(int arr[], int l, int r) {
        //l < ind <= r
        int m = (r + l) / 2;
        // l < ind <= r && m == (r + l) / 2
        if (r - l > 1) {
            // l < ind <= r && m == (r + l) / 2 && r - l > 1
            if (arr[m] < arr[m+1]) {
                // l < ind <= r && r - l > 1 && m == (r + l) / 2 && arr[m] < arr[m + 1]
                //forall i = 0 .. m : arr[i] < arr[ind] -> m < ind
                // l < m < ind <= r
                return recbinsearch(arr, m, r);
            } else {
                // l < ind <= r && r - l > 1 && m == (r + l) / 2 && arr[m] >= arr[m+1]
                //forall i = m + 1 .. arr.length - 1 : arr[i] >= arr[ind] -> ind <= m
                // l < ind <= m < r
                return recbinsearch(arr, l, m);
            }
        }
        // l < ind <= r && r <= l + 1
        // l < ind <= r <= l + 1
        // ind == l + 1 <= r
        // ind == r -> arr[r] = max
        return arr[r];
    }

    //Pred: exists ind : forall i = 0 .. ind - 1 : args[i] < args[i+1] && forall i = ind .. args.length - 1 : args[i] > args[i+1]
    // arr[ind] = Max;
    // Post: R = arr[ind]
    private static int binsearch(int arr[]) {
        int l = -1, r = arr.length - 1;
        // l == -1 && r == arr.length - 1
        //Inv: l < ind <= r
        while (r - l > 1) {
            // l == -1 && r == arr.length - 1 && r - l > 1 && l < ind <= r
            int m = (r + l) / 2;
            // l == -1 && r == arr.length - 1 && r - l > 1 && m == (r + l) / 2 && l < ind <= r
            if (arr[m] < arr[m + 1]) {
                // l == -1 && r == arr.length - 1 && r - l > 1 && m == (r + l) / 2 && arr[m] < arr[m + 1] && l < ind <= r
                //forall i = 0 .. m : arr[i] < arr[ind] -> m < ind
                // l < m < ind <= r
                l = m;
                // r == arr.length - 1 && arr[m] < arr[m + 1]
                // l == m < ind <= r &&
            } else {
                // l == -1 && r == arr.length - 1 && r - l > 1 && m == (r + l) / 2 && arr[m] >= arr[m + 1] && l < ind <= r
                //forall i = m + 1 .. arr.length - 1 : arr[i] >= arr[ind] -> ind <= m
                // l < ind <= m < r
                r = m;
                // l == l' && arr[m] >= arr[m + 1]
                // l < ind <= m == r
            }
            //Inv: l < ind <= r
        }
        // l < ind <= r && r <= l + 1
        // l < ind <= r <= l + 1
        // ind == l + 1 <= r
        // ind == r -> arr[r] = Max
        return arr[r];
    }


    //Pred: args.length > 0 && forall i = 0 .. args.length - 1: (Integer) args[i] && args[i] != null
    //exists ind : forall i = 0 .. ind - 1 : args[i] < args[i+1] && forall i = ind .. args.length - 1 : args[i] > args[i+1]
    //Post: R = ind
    public static void main(String[] args) {
        int arr[] = new int[args.length];
        // arr.length = args.length
        //Post: forall i = 0 .. args.length - 1 : arr[i] = (int) args[i]
        for (int i = 0; i < args.length; i++) {
            arr[i] = Integer.parseInt(args[i]);
        }
        // arr.length = args.length && forall i = 0 .. args.length - 1 : arr[i] = (int) args[i]
        //System.out.println(recbinsearch(arr,-1, arr.length - 1));
        System.out.println(binsearch(arr));
    }
}
