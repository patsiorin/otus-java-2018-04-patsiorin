package ru.patsiorin.otus;

import java.util.ArrayList;
import java.util.List;

public class LeakingList implements Runnable {
    private static final int INSERTIONS_NUMBER = 100_000;
    private static final int PAUSE_TIME_MILLIS = 200;
    long loopCount;

    private List<String> leakingList = new ArrayList<>();

    public void run() {
        int j = 0;
        try {
            do {
                for (int i = 0; i < INSERTIONS_NUMBER; i++) {
                    leakingList.add(new String("empty"));
                }

                for (int i = j; i < leakingList.size(); i += 2) {
                    leakingList.set(i, null);
                }

                j = leakingList.size();
                loopCount++;
                Thread.sleep(PAUSE_TIME_MILLIS);
            } while (true);
        } catch (OutOfMemoryError e) {
            System.out.println("We ran out of memory");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
