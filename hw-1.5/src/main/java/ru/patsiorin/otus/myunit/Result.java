package ru.patsiorin.otus.myunit;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Result {
    private int runs;
    private Instant startInstant;
    private Instant endInstant;
    private List<Failure> failures = new ArrayList<>();

    Result(int runs) {
        this.runs = runs;
        startInstant = Instant.now();
    }

    void addFailure(Failure failure) {
        failures.add(failure);
    }

    String getResultString() {
        StringBuilder resultString = new StringBuilder();
        resultString.append("\n");
        resultString.append(String.format("Time: %.3fs\n", Duration.between(startInstant, endInstant).toMillis() / 1000.0));
        if (!isTestSuccessful()) {
            resultString.append(String.format("There were %d errors:\n", failures.size()));
            for (int i = 0; i < failures.size(); i++) {
                Failure failure = failures.get(i);
                resultString.append(String.format("%d) ", i + 1))
                        .append(String.format("%s(%s)\n", failure.getMethod().getName(), failure.getTestClass().getName()))
                        .append(failure.getError()).append("\n");
            }
        } else {
            resultString.append(String.format("OK (%d tests)\n", runs));
        }

        resultString.append("------------------------------------------------\n");
        return resultString.toString();
    }

    public boolean isTestSuccessful() {
        return failures.size() == 0;
    }

    public void setEndInstant(Instant endInstant) {
        this.endInstant = endInstant;
    }
}
