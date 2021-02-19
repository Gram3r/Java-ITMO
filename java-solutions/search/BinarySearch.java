package search;

public class BinarySearch {
    //Pred: forall i = 0 .. arr.length-2 : a[i] >= a[i+1] && exists min index, that arr[ind] >= x
    //Inv: l < arr[x] <= r && arr[i] >= arr[i+1]
    //Post: R = ind
    private static int recbinsearch(int arr[], int x, int l, int r) {
        //l < ind <= r
        int m = (r + l) / 2;
        // l < ind <= r && m == (r + l) / 2
        if (r - 1 > l) {
            // l < ind <= r && m == (r + l) / 2 && r - 1 > l
            if (arr[m] > x) {
                // l < ind <= r && r - 1 > l && m == (r + l) / 2 && x < arr[m] -> m < ind (т.к. по убыванию)
                return recbinsearch(arr, x, m, r);
            } else {
                // l < ind <= r && r - 1 > l && m == (r + l) / 2 && arr[m] <= x -> ind <= m (т.к. по убыванию)
                return recbinsearch(arr, x, l, m);
            }
        }
        // l < ind <= r && r <= l + 1
        // l < ind <= r <= l + 1
        // ind == l + 1 <= r
        // ind == r
        return r;
    }

    //Pred: forall i = 0 .. arr.length-2 : a[i] >= a[i+1] && exists min index, that arr[ind] >= x && 0 < ind <= arr.length
    //Post: R = ind
    private static int binsearch(int arr[], int x) {
        int l = -1, r = arr.length;
        // l == -1 && r == arr.length
        //Inv: l < ind <= r
        while (r - 1 > l) {
            // l == -1 && r == arr.length && r - 1 > l && l < ind <= r
            int m = (r + l) / 2;
            // l == -1 && r == arr.length && r - 1 > l && m == (r + l) / 2 && l < ind <= r
            if (arr[m] > x) {
                // l == -1 && r == arr.length && r - 1 > l && m == (r + l) / 2 && arr[m] > x && l < ind <= r
                l = m;
                // r == arr.length && x < arr[m] -> m < ind && l == m < ind <= r &&
            } else {
                // l == -1 && r == arr.length && r - 1 > l && m == (r + l) / 2 && arr[m] <= x  && l < ind <= r
                r = m;
                // l == -1 && arr[m] <= x -> ind <= m && r == m && l < ind <= m == r
            }
            //Inv: l < ind <= r
        }
        // l < ind <= r && r <= l + 1
        // l < ind <= r <= l + 1 -> ind == l + 1 <= r -> ind == r
        return r;
    }


    //Pre: args.length > 0
    //Post: R = ind
    public static void main(String[] args) {
        int x = Integer.parseInt(args[0]);
        // x = (int) args[0]
        int arr[] = new int[args.length - 1];
        // x = (int) args[0] && arr.length = args.length - 1
        for (int i = 1; i < args.length; i++) {
            arr[i - 1] = Integer.parseInt(args[i]);
        }
        // x = (int) args[0] && arr.length = args.length - 1 && forall i = 0 .. args.length - 1 : arr[i] = (int) args[i+1]
        System.out.println(recbinsearch(arr,x, -1, arr.length));
        //System.out.println(binsearch(arr,x));
    }
}
