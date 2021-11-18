package divide_and_conquer;

import java.util.Arrays;

import static utils.Assertions.assertEquals;

public class SortingAndCounting {
    public static void main(String[] args) {
        {
            int[] input = { 2, 1, 0, 8 };
            assertEquals(3, countInversions(input));
        }
        {
            int[] input = { 2, 1 };
            assertEquals(1, countInversions(input));
        }
        {
            int[] input = { 2, 1, 0 };
            assertEquals(3, countInversions(input));
        }
        {
            int[] input = { 2, 1, 0, 4, 1 };
            assertEquals(5, countInversions(input));
        }
        {
            int[] input = { 2, 1, 4, 11, 7, 9, 3, 10, 12 };
            assertEquals(8, countInversions(input));
        }
    }


    public static int countInversions(int[] arr) {
        return mergeSort(arr).inv;
    }

    public static MergeResult mergeSort(int[] arr) {
        if (arr.length <= 1) return new MergeResult(arr, 0);
        int mid = arr.length/2;
        int[] a = Arrays.copyOfRange(arr, 0, mid);
        int[] b = Arrays.copyOfRange(arr, mid, arr.length);
        return merge(mergeSort(a), mergeSort(b));
    }

    public static MergeResult merge(MergeResult mA, MergeResult mB) {
        int[] a = mA.arr;
        int[] b = mB.arr;
        int[] ab = new int[a.length + b.length];
        int i = 0, ai = 0, bi = 0, inv = 0;

        while (ai < a.length && bi < b.length) {
            if (a[ai] <= b[bi]) ab[i++] = a[ai++];
            else {
                ab[i++] = b[bi++];
                inv += a.length - ai;
            }
        }

        while (ai < a.length) ab[i++] = a[ai++];
        while (bi < b.length) ab[i++] = b[bi++];

        return new MergeResult(ab, inv + mA.inv + mB.inv);
    }

    /**
     * Library
     */
    static class MergeResult {
        int[] arr;
        int inv;

        public MergeResult(int[] arr, int inv) {
            this.arr = arr;
            this.inv = inv;
        }
    }
}
