package dynamic_programming.level1;

import static utils.Assertions.assertEquals;

public class Fibonacci {

    public static void main(String[] args) {
        Long[] mem = new Long[92];
        fib(91, mem);

        for (int i = 0; i < mem.length; i++) { // Largest non overflowing value for signed longs
            System.out.println("Fib(" + i + ") = " + mem[i]);
        }

        assertEquals(mem[91], 4660046610375530309L);
        assertEquals(mem[91], fibIterative(91));
    }

    public static long fib(int n) {
        return fib(n, new Long[n + 1]);
    }

    /**
     * DC Fibonacci.
     */
    public static long fib(int n, Long[] mem) {
        if (mem[n] != null) return mem[n];

        if (n == 0) {
            mem[n] = 0L;
        } else if (n == 1) {
            mem[n] = 1L;
        } else {
            mem[n] = fib(n - 1, mem) + fib(n - 2, mem);
        }
        return mem[n];
    }


    /**
     * Returns the n'th Fibonacci number
     */
    public static long fibIterative(int n) {
        // The array in which you must implement your memoization.
        long[] fibonacci = new long[n + 1];
        fibonacci[0] = 0;
        fibonacci[1] = 1;

        // After that, iterate through all fibonacci numbers from index 2 up to n.
        for (int i = 2; i <= n; i++) {
            fibonacci[i] = fibonacci[i - 1] + fibonacci[i - 2];
        }
        // Returning the obtained fibonacci value at index n.
        return fibonacci[n];
    }
}
