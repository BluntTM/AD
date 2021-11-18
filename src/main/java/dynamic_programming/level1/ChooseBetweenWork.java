package dynamic_programming.level1;

import static utils.Assertions.assertEquals;

public class ChooseBetweenWork {
    public static void main(String[] args) {
        {
            int[] s = { 0, 0, 1, 3 };
            int[] f = { 0, 3, 4, 8 };
            int[] v = { 0, 3, 5, 7 };
            int[] p = { 0, -1, -1, 1 };
            assertEquals(10, solve(3, s, f, v, p));
        }
    }


    /**
     * Come up with an iterative dynamic programming solution to the weighted interval scheduling problem.
     * NB: You may assume the jobs are sorted by ascending finishing time.
     * @param n the number of jobs
     * @param s the start times of the jobs for jobs 1 through n. Note that s[0] should be ignored.
     * @param f the finish times of the jobs for jobs 1 through n. Note that f[0] should be ignored.
     * @param v the values of the jobs for jobs 1 through n. Note that v[0] should be ignored.
     * @param p the predecessors of the jobs for jobs 1 through n. Note that p[0] should be ignored and that -1 represents there being no predecessor.
     * @return the weight of the maximum weight schedule.
     */
    public static int solve(int n, int[] s, int[] f, int[] v, int[] p) {
        int[] mem = new int[n + 1];
        mem[0] = 0;

        for (int j = 1; j <= n; j++) {
            int pv = (p[j] >= 0) ? mem[p[j]] : 0;
            mem[j] = Math.max(v[j] + pv, mem[j - 1]);
        }

        // Returning the obtained obtained value at index n.
        return mem[n];
    }
}
