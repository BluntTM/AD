package dynamic_programming.level2;

import java.io.InputStream;
import java.util.Scanner;

import static utils.Assertions.assertEquals;
import static utils.StreamUtils.getResourceAsStream;
import static utils.StreamUtils.read;

public class StudentBudget {
    public static void main(String[] args) {
        assertEquals(
                read("/dynamic_programming/student_budget/example.out").trim(),
                run(getResourceAsStream("/dynamic_programming/student_budget/example.in")).trim()
        );
    }

    // inputstream. // dum comment.
    public static String run(InputStream in) {
        return new StudentBudget().solve(in);
    }

    public String solve(InputStream in) {
        try (Scanner sc = new Scanner(in)) {
            int W = sc.nextInt();
            int n = sc.nextInt();

            int[] ws = new int[n];
            int[] vs = new int[n];
            for (int i = 0; i < n; i++) {
                ws[i] = sc.nextInt();
                vs[i] = sc.nextInt();
            }

            int[][] mem = new int[n][];
            return solve(ws, vs, n, W, mem);
        }
    }

    public String solve(int[] ws, int[] vs, int n, int W, int[][] mem) {
        for (int i = 0; i < n; i++) {
            mem[i] = new int[W + 1];
        }

        for (int i = 0; i < n; i++) {
            for (int w = 0; w <= W; w++) {
                int pv = i > 0 ? mem[i - 1][w] : 0;
                if (ws[i] > w) {
                    mem[i][w] = pv;
                } else {
                    int rv = i > 0 ? mem[i - 1][w - ws[i]] : 0;
                    mem[i][w] = Math.max(pv, vs[i] + rv);
                }
            }
        }
        return String.valueOf(mem[n - 1][W]);
    }
}
