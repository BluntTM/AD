package dynamic_programming.level4;

import java.util.Arrays;

import static utils.Assertions.assertEquals;

public class SteroidDijkstra {
    public static void main(String[] args) {
        {
            int n = 2;
            int m = 3;
            int[][] graph = { { 3, 5, 6 }, { 4, 2, 1 } };
            assertEquals(1, solve(n, m, graph));
        }
    }

    public static int solve(int n, int m, int[][] graph) {
        int[][] mem = new int[n][m];
        for (int[] row : mem) Arrays.fill(row, Integer.MAX_VALUE);
        // Base case
        mem[0][0] = 0;
        // Iterate over the entire grid n*m times, since it can take that long for the result to propagate to the end.
        for (int k = 0; k < n * m; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    if (i == 0 && j == 0)
                        continue;
                    // Try all 4 cases: left, right, up and down.
                    // Do an out-of-bounds check before indexing the array.
                    // Check whether we found a shorter path to our current node, by taking the min of the current value
                    // And the neighboring nodes. Keep in mind we might have to jump up from this node
                    // and therefore we compare the graph values.
                    mem[i][j] = j - 1 < 0 ? mem[i][j] : Integer.min(mem[i][j], Integer.max(mem[i][j - 1], graph[i][j] - graph[i][j - 1]));
                    mem[i][j] = i - 1 < 0 ? mem[i][j] : Integer.min(mem[i][j], Integer.max(mem[i - 1][j], graph[i][j] - graph[i - 1][j]));
                    mem[i][j] = j + 1 >= m ? mem[i][j] : Integer.min(mem[i][j], Integer.max(mem[i][j + 1], graph[i][j] - graph[i][j + 1]));
                    mem[i][j] = i + 1 >= n ? mem[i][j] : Integer.min(mem[i][j], Integer.max(mem[i + 1][j], graph[i][j] - graph[i + 1][j]));
                }
            }
        }
        return mem[n - 1][m - 1];
    }
}
