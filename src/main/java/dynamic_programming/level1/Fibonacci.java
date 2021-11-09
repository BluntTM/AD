package dynamic_programming.level1;

public class Fibonacci {

    public static void main(String[] args) {
        Long[] mem = new Long[92];
        fib(91, mem);

        for (int i = 0; i < mem.length; i++) { // Largest non overflowing value for signed longs
            System.out.println("Fib(" + i + ") = " + mem[i]);
        }
    }

    public static long fib(int n) {
        return fib(n, new Long[n + 1]);
    }

    /**
     * DC Fibonacci.
     */
    public static long fib(int n, Long[] mem) {
        if (mem[n] != null) return mem[n];

        if (n < 2) {
            mem[n] = 1L;
        } else {
            mem[n] = fib(n - 1, mem) + fib(n - 2, mem);
        }
        return mem[n];
    }
}
