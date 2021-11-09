package dynamic_programming.level2;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;

public class WordSegmentation {

    public static void main(String[] args) {
        Map<String, Integer> qualities = Map.of(
                "lawy", 1,
                "ersar", 1,
                "eawe", 1,
                "so", 2,
                "me", 3,
                "lawyers", 5,
                "are", 4,
                "awesome", 10
        );
        Function<String, Integer> qualityFunction = str -> qualities.getOrDefault(str, 0);

        String word = "lawyersareawesome";
        int[] mem = new int[word.length() + 1];
        System.out.println("Optimal Quality = " + computeOptimalQuality(word, qualityFunction, mem));
        System.out.println("Memory = " + Arrays.toString(mem));
        System.out.println();
        System.out.println("Optimal Word Split:");
        printOptimalWordSplit(word, qualityFunction, mem);
    }

    /**
     * Prints the optimal word split given the word, qualityFunction and initialized memory.
     */
    public static void printOptimalWordSplit(String word, Function<String, Integer> qualityFunction, int[] mem) {
        int s = printOptimalWordSplit(word, word.length(), qualityFunction, mem);
        if (s < 0) return;
        System.out.println(word.substring(s));
    }

    /**
     * Helper method to print word split recursively.
     */
    private static int printOptimalWordSplit(String word, int j, Function<String, Integer> qualityFunction, int[] mem) {
        if (j <= 0) return -1;
        int i = j - 1;
        while (i > 0 && qualityFunction.apply(word.substring(i, j)) + mem[i] != mem[j]) {
            i--;
        }

        int s = printOptimalWordSplit(word, i, qualityFunction, mem);
        if (s >= 0) System.out.println(word.substring(s, i));
        return i;
    }

    /**
     * Computes the optimal split quality of given word, qualityFunction and (empty) memory, iteratively.
     */
    public static long computeOptimalQuality(String word, Function<String, Integer> qualityFunction, int[] mem) {
        mem[0] = 0;
        for (int j = 1; j <= word.length(); j++) {
            int max = Integer.MIN_VALUE;
            for (int i = 0; i < j; i++) {
                max = Math.max(max, qualityFunction.apply(word.substring(i, j)) + mem[i]);
            }
            mem[j] = max;
        }
        return mem[word.length()];
    }
}
