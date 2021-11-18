package dynamic_programming.level3;

import java.util.Arrays;

public class TravellingSalesman {

    public static void main(String[] args) {
        int n = 5;
        int[] P = { 0, 80, 30, 30, 70, 80 };
        int[] Q = { 0, 90, 60, 60, 50, 20 };
        System.out.println(solve(n, P, Q));
    }

    /**
     * @param n the number of days
     * @param P the profits that can be made on day 1 through n on location P are stored in P[1] through P[n].
     * @param Q the profits that can be made on day 1 through n on location Q are stored in Q[1] through Q[n].
     * @return the maximum obtainable profit.
     */
    public static int solve(int n, int[] P, int[] Q) {
        int[][] mem = new int[2][n + 1];

        for (int i = 1; i <= n; i++) {
            int prevP = mem[0][i - 1];
            int prevQ = mem[1][i - 1];
            mem[0][i] = Math.max(prevP + P[i], prevQ);
            mem[1][i] = Math.max(prevQ + Q[i], prevP);
        }

        System.out.println("P " + Arrays.toString(mem[0]));
        System.out.println("Q " + Arrays.toString(mem[1]));
        return Math.max(mem[0][n], mem[1][n]);
    }
}
