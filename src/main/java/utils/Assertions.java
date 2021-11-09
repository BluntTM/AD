package utils;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class Assertions {

    private Assertions() {}

    private static final AtomicInteger counter = new AtomicInteger(0);

    public static void assertEquals(Object expected, Object actual) {
        if (Objects.equals(expected, actual)) {
            System.out.println("✔ Test " + counter.incrementAndGet() + " passed!");
        } else {
            System.out.println("✘ Expected <" + expected + ">, but was <" + actual + ">");
        }
    }
}
