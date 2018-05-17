package ru.patsiorin.otus.tests;

import ru.patsiorin.otus.myunit.After;
import ru.patsiorin.otus.myunit.Assert;
import ru.patsiorin.otus.myunit.Before;
import ru.patsiorin.otus.myunit.Test;

public class TestFail {
    private int expected = 1234;
    private int actual;

    @Before
    public void setUp() {
        actual = 1200 + 33;
        System.out.print(" -- before -- ");
    }

    @Test
    public void test1() {
        Assert.assertEquals("Fails", expected, actual);
    }

    @Test
    public void test2() {
        Assert.assertEquals("Doesn't fail", expected - 1, actual);
    }

    @After
    public void after() {
        System.out.print(" -- after() -- ");
    }
}
