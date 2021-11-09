package dynamic_programming.level4;

import java.util.Arrays;

public class ShortestPath {

    public static void main(String[] args) {
        int n = 2;
        int m = 3;
        int[][] graph = { { 3, 5, 6 }, { 4, 2, 1 } };
        System.out.println(solve(n, m, graph));
    }

    public static int solve(int n, int m, int[][] graph) {
        int[][] mem = new int[n][m];
        for (int[] row : mem) {
            Arrays.fill(row, Integer.MAX_VALUE);
        }
        mem[0][0] = 0;

        for (int k = 0; k < n * m; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    if (i == 0 && j == 0) continue;

                    if (j > 0) mem[i][j] = Math.max(
                            mem[i][j - 1],
                            graph[i][j] - graph[i][j - 1]
                    );
                    if (i > 0) mem[i][j] = Math.min(mem[i][j], Math.max(
                            mem[i - 1][j],
                            graph[i][j] - graph[i - 1][j])
                    );
                    if (j + 1 < m) mem[i][j] = Math.min(mem[i][j], Math.max(
                            mem[i][j + 1],
                            graph[i][j] - graph[i][j + 1])
                    );
                    if (i + 1 < n) mem[i][j] = Math.min(mem[i][j], Math.max(
                            mem[i + 1][j],
                            graph[i][j] - graph[i + 1][j])
                    );
                }
            }
        }

        return mem[n - 1][m - 1];
    }
}
