package ru.patsiorin.otus;

import java.util.*;
import java.util.concurrent.ForkJoinPool;

/**
 * For simplicity this solution uses ForkJoinPool with RecursiveAction and Arrays.sort()
 * to sort an array in parallel processes.
 *
 */

public class ParallelSort {
    static final int SORT_SEQUANTIALLY = 1024;
    private static final int PARALLELISM = 4;
    private static ForkJoinPool fjPool = new ForkJoinPool(PARALLELISM);

    public static <T extends Comparable<? super T>> void sort(T[] array) {
        if (array.length < SORT_SEQUANTIALLY) {
            Arrays.sort(array);
        } else {
            ForkJoinSorterAction<T> forkJoinSorterAction = new ForkJoinSorterAction<>(array);
            fjPool.invoke(forkJoinSorterAction);
        }
    }
}
