package greedy;

import java.util.Arrays;

import static utils.Assertions.assertEquals;

public class ParallelProcessing {
    public static void main(String[] args) {
        {
            int n = 5;
            int m = 2;
            int[] deadlines = {0, 3, 1, 1, 1, 2};
            assertEquals(1, solve(n, m, deadlines));
        }
        {
            int n = 3;
            int m = 1;
            int[] deadlines = { 0, 1, 1, 1 };
            assertEquals(2, solve(n, m, deadlines));
        }
        {
            int n = 3;
            int m = 1;
            int[] deadlines = { 0, 0, 0, 0 };
            assertEquals(3, solve(n, m, deadlines));
        }
        {
            int n = 3;
            int m = 1;
            int[] deadlines = { 0, -1, 0, 0 };
            assertEquals(3, solve(n, m, deadlines));
        }
    }


    /**
     * @param n the number of jobs
     * @param m the number of processors
     * @param deadlines the deadlines of the jobs 1 through n. NB: you should ignore deadlines[0]
     * @return the minimised maximum lateness.
     */
    public static int solve(int n, int m, int[] deadlines) {
        Arrays.sort(deadlines);

        int maxLateness = 0;
        int[] processors = new int[m];
        int currentProcessor = 0;
        for (int i = 1; i <= n; i++) {
            int deadline = deadlines[i];
            int processorIndex = currentProcessor++;
            if (currentProcessor >= m) currentProcessor = 0;

            processors[processorIndex] += 1;

            int lateness = Math.max(0, processors[processorIndex] - deadline);
            if (lateness > maxLateness) {
                maxLateness = lateness;
            }
        }
        return maxLateness;
    }
}
