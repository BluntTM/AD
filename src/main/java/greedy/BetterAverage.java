package greedy;

import java.util.Arrays;

import static utils.Assertions.assertEquals;

public class BetterAverage {

    public static void main(String[] args) {
        int n = 4;
        double[] list = { 4, 2, 1, 3 };
        assertEquals(2.5, solve(n, list), 1e-3);
    }

    public static double solve(int n, double[] list) {
        if (n == 0) return 0;

        double[] sorted = mergeSort(list);
        int mid = n / 2;
        if (n % 2 == 0) {
            return (sorted[mid] + sorted[mid - 1]) / 2;
        } else {
            return sorted[mid];
        }
    }

    public static double[] mergeSort(double[] list) {
        if (list.length <= 1) return list;
        int mid = list.length/2;
        double[] left = mergeSort(Arrays.copyOfRange(list, 0, mid));
        double[] right = mergeSort(Arrays.copyOfRange(list, mid, list.length));
        return merge(left, right);
    }

    public static double[] merge(double[] a, double[] b) {
        double[] merged = new double[a.length + b.length];
        int ai, bi, mi;
        ai = bi = mi = 0;
        while (ai < a.length && bi < b.length) {
            if (a[ai] < b[bi]) {
                merged[mi++] = a[ai++];
            } else {
                merged[mi++] = b[bi++];
            }
        }

        // Append leftovers
        while (ai < a.length) merged[mi++] = a[ai++];
        while (bi < b.length) merged[mi++] = b[bi++];
        return merged;
    }
}
