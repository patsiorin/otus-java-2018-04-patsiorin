package ru.patsiorin.otus;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.concurrent.RecursiveAction;

public class ForkJoinSorterAction<T extends Comparable<? super T>> extends RecursiveAction {
    private final T[] array;
    private final int left;
    private final int right;

    public ForkJoinSorterAction(T[] array) {
        this(array, 0, array.length);
    }

    public ForkJoinSorterAction(T[] array, int left, int right) {
        this.array = array;
        this.left = left;
        this.right = right;
    }

    @Override
    protected void compute() {
        if (right - left < ParallelSort.SORT_SEQUANTIALLY) {
            Arrays.sort(array, left, right);
        } else {
            int mid = (left + right) / 2;
            ForkJoinSorterAction<T> actionLeft = new ForkJoinSorterAction<>(array, left, mid);
            ForkJoinSorterAction<T> actionRight = new ForkJoinSorterAction<>(array, mid, right);

            invokeAll(actionLeft, actionRight);
            merge(array, left, mid, right);

        }
    }

    private void merge(T[] array, int left, int mid, int right) {
        T[] temp = (T[]) Array.newInstance(array.getClass().getComponentType(), right - left);
        int k = 0, i = left, j = mid;
        while(i < mid && j < right) {
            if (array[i].compareTo(array[j]) < 0) {
                temp[k++] = array[i++];
            } else {
                temp[k++] = array[j++];
            }
        }
        while (i < mid) {
            temp[k++] = array[i++];
        }
        while (j < right) {
            temp[k++] = array[j++];
        }

        System.arraycopy(temp, 0, array, left, right - left);
    }
}
