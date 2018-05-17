package ru.patsiorin.otus.myunit;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MyUnitTest {
    @Test
    public void testSuccess() {
        assertTrue(MyUnit.run("ru.patsiorin.otus.tests.TestSuccess").isTestSuccessful());
    }

    @Test
    public void testFailure() {
        assertFalse(MyUnit.run("ru.patsiorin.otus.tests.TestFail").isTestSuccessful());
    }

}
