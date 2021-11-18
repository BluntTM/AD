package utils;

import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BooleanSupplier;

public class Assertions {

    private Assertions() {}

    private static final AtomicInteger counter = new AtomicInteger(0);

    public static void assertEquals(Object expected, Object actual) {
        assertEquals(null, expected, actual);
    }

    public static void assertEquals(String msg, Object expected, Object actual) {
        assertBoolean(msg, () -> Objects.equals(expected, actual), expected, actual);
    }

    public static void assertEquals(double expected, double actual, double error) {
        assertEquals(null, expected, actual, error);
    }

    public static void assertEquals(String msg, double expected, double actual, double error) {
        assertBoolean(msg, () -> Math.abs(expected - actual) <= error, expected, actual);
    }

    public static void assertArrayEquals(int[] expected, int[] actual) {
        assertArrayEquals(null, expected, actual);
    }

    public static void assertArrayEquals(String msg, int[] expected, int[] actual) {
        assertBoolean(msg, () -> Arrays.equals(expected, actual), Arrays.toString(expected), Arrays.toString(actual));
    }

    public static void assertTrue(boolean actual) {
        assertTrue(null, actual);
    }

    public static void assertTrue(String msg, boolean actual) {
        assertBoolean(msg, () -> actual, true, actual);
    }

    public static void assertFalse(boolean actual) {
        assertFalse(null, actual);
    }

    public static void assertFalse(String msg, boolean actual) {
        assertBoolean(msg, () -> !actual, false, actual);
    }

    public static <T> void assertBoolean(BooleanSupplier booleanSupplier, T expected, T actual) {
        assertBoolean(null, booleanSupplier, expected, actual);
    }

    public static <T> void assertBoolean(String msg, BooleanSupplier booleanSupplier, T expected, T actual) {
        if (booleanSupplier.getAsBoolean()) {
            pass(msg);
        } else {
            fail(msg, expected, actual);
        }
    }

    private static void pass(String msg) {
        System.out.println("✔ Test " + counter.incrementAndGet() + " passed" + (msg == null ? "" : (": " + msg)) + "!");
    }

    private static <T> void fail(String msg, T expected, T actual) {
        System.out.println("✘ Test " + counter.incrementAndGet() + " failed, expected <" + expected + ">, but was <" + actual + ">" + (msg == null ? "" : (": " + msg)));
    }
}
