package ru.patsiorin.otus;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;

public class ParallelSortTest {
    @Test
    public void testParralelSort() {
        Integer[] testArray  =  new Integer[2048];
        for (int i = 0; i < testArray.length; i++) {
            testArray[i] = (int)(Math.random() * 1000);
        }
        Integer[] sorted = Arrays.copyOf(testArray, testArray.length);
        Arrays.sort(sorted);
        ParallelSort.sort(testArray);
        assertArrayEquals(sorted, testArray);
    }
}
