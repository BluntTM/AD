package dynamic_programming.level3;

import java.util.Arrays;

public class RNASecondaryStructure {

    public static void main(String[] args) {
        String rnaString = "ACCGGUAGU";
        char[] chars = rnaString.toCharArray();

        int n = chars.length;
        int[][] mem = new int[n + 1][n + 1];
        System.out.println(opt(chars, n, mem));

        for (int i = n; i >= 1; i--) {
            System.out.println(i + " " + Arrays.toString(mem[i]));
        }
    }

    public static boolean p(char b1, char b2) {
        return (b1 == 'A' && b2 == 'U')
                || (b1 == 'U' && b2 == 'A')
                || (b1 == 'C' && b2 == 'G')
                || (b1 == 'G' && b2 == 'C');
    }

    public static int opt(char[] chars, int n, int[][] mem) {
        for (int k = 5; k <= n - 1; k++) {
            for (int i = 1; i <= n - k; i++) {
                int j = i + k;

                int pv = mem[i][j - 1];
                for (int t = i; t < j - 4; t++) {
                    if (!p(chars[t - 1], chars[j - 1])) continue;
                    pv = Math.max(pv, 1 + mem[i][t - 1] + mem[t + 1][j - 1]);
                }
                mem[i][j] = pv;
            }
        }
        return mem[1][n];
    }
}
