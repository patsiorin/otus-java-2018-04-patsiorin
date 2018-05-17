package ru.patsiorin.otus.tests;

import ru.patsiorin.otus.myunit.After;
import ru.patsiorin.otus.myunit.Assert;
import ru.patsiorin.otus.myunit.Before;
import ru.patsiorin.otus.myunit.Test;

public class TestSuccess {
    private String expected;
    private String actual;
    @Before
    public void before() {
        expected = "reflection rules";
        actual = "REFLECTION RULES".toLowerCase();
    }

    @Before
    public void moreBefore() {
        System.out.print(" -- before -- ");
    }

    @Test
    public void test1() {
        Assert.assertEquals("These are equal", 3, 3);
    }

    @Test
    public void test2() {
        Assert.assertEquals("Strings are equal", expected, actual);
    }

    @After
    public void after1() {
        System.out.print(" after1 ");
    }

    @After
    public void after2() {
        System.out.print(" after2 ");
    }
}
