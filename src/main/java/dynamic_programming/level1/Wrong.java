package dynamic_programming.level1;

import static utils.Assertions.assertEquals;

public class Wrong {
    public static void main(String[] args) {
        int n = 5;
        int[] nodes = { 0, 2, 1, 6, 8, 9 };
        assertEquals(17, weight(n, nodes));
    }

    /*
     * Note that entry node[0] should be avoided, as nodes are labelled node[1] through node[n].
     */
    public static int weight(int n, int[] nodes) {
        int[] mem = new int[n + 1];
        mem[0] = 0;
        mem[1] = nodes[1];
        for (int i = 2; i <= n; i++) {
            int result = Integer.max(mem[i - 1], mem[i - 2] + nodes[i]);
            mem[i] = result;
        }
        return mem[n];
    }
}
