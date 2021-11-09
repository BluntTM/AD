package dynamic_programming.level4;

import java.util.function.BiFunction;

public class SequenceAligning {

    public static void main(String[] args) {
        System.out.println(solve("kitten", "sitting"));
    }

    public static int solve(String firstString, String secondString) {
        int d = 1;
        char[] X = toChars(firstString);
        char[] Y = toChars(secondString);

        int n = firstString.length();
        int m = secondString.length();
        int[][] mem = new int[n + 1][m + 1];

        return solve(X, n, Y, m, d, (x, y) -> x == y ? 0 : 1, mem);
    }

    private static char[] toChars(String str) {
        char[] chars = new char[str.length() + 1];
        System.arraycopy(str.toCharArray(), 0, chars, 1, str.length());
        return chars;
    }

    private static int solve(char[] X, int n, char[] Y, int m, int d, BiFunction<Character, Character, Integer> a, int[][] mem) {
        for (int i = 1; i <= n; i++) {
            mem[i][0] = i * d;
        }

        for (int j = 1; j <= m; j++) {
            mem[0][j] = j * d;
        }

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                mem[i][j] = Math.min(
                        a.apply(X[i], Y[i]) + mem[i - 1][j - 1],
                        Math.min(
                                d + mem[i - 1][j],
                                d + mem[i][j - 1]
                        )
                );
            }
        }
        return mem[n][m];
    }
}
