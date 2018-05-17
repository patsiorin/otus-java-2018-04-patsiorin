package ru.patsiorin.otus.myunit;

public class Assert {
    private Assert() {}

    public static void assertTrue(String message, boolean condition) {
        if (!condition) {
            fail(message);
        }
    }

    public static void assertTrue(boolean condition) {
        assertTrue("", condition);
    }

    public static void assertFalse(String message, boolean condition) {
        assertTrue(message, !condition);
    }

    public static void assertFalse(boolean condition) {
        assertTrue(!condition);
    }

    public static void assertEquals(String message, long expected, long actual) {
        if (expected != actual) {
            failNotEquals(format(message, expected, actual));
        }
    }

    public static void assertEquals(String message, double expected, double actual, double delta) {
        if (Math.abs(expected - actual) > delta) {
            fail(format(message, expected, actual));
        }
    }

    public static void assertEquals(String message, Object expected, Object actual) {
        if (!expected.equals(actual)) {
            fail(format(message, expected, actual));
        }
    }

    private static void failNotEquals(String message) {
        fail("(Should be equal) " + message);
    }

    private static String format(String message, Object expected, Object actual) {
        return message + "\nexpected: " + expected + " but actual: " + actual + "";
    }

    private static void fail(String message) {
        if (message == null) {
            throw new AssertionError();
        } else {
            throw new AssertionError(message);
        }
    }
}
