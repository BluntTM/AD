package divide_and_conquer;

import java.util.Arrays;

import static utils.Assertions.assertArrayEquals;

public class SortingThroughMerging {
    public static void main(String[] args) {
        int[] input = { 4, 2, 5, 1, 3 };
        new SortingThroughMerging().sort(input);
        assertArrayEquals(new int[] { 1, 2, 3, 4, 5 }, input);
    }

    /**
     * Takes an array and sorts it in an ascending order.
     *
     * @param arr
     *     - the array that needs to be sorted.
     */
    public void sort(int[] arr) {
        int[] sorted = mergeSort(arr);
        System.arraycopy(sorted, 0, arr, 0, arr.length);
    }

    public int[] mergeSort(int[] arr) {
        if (arr.length <= 1) return arr;
        int mid = arr.length/2;
        int[] a = Arrays.copyOfRange(arr, 0, mid);
        int[] b = Arrays.copyOfRange(arr, mid, arr.length);
        return merge(mergeSort(a), mergeSort(b));
    }

    public int[] merge(int[] a, int[] b) {
        int[] ab = new int[a.length + b.length];
        int i = 0, ai = 0, bi = 0;

        while (ai < a.length && bi < b.length) {
            if (a[ai] < b[bi]) ab[i++] = a[ai++];
            else ab[i++] = b[bi++];
        }

        while (ai < a.length) ab[i++] = a[ai++];
        while (bi < b.length) ab[i++] = b[bi++];

        return ab;
    }
}
