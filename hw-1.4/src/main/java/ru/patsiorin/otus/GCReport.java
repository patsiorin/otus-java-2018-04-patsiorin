package ru.patsiorin.otus;

import com.sun.management.GarbageCollectionNotificationInfo;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;

public class GCReport {
    private String GCName;
    private long youngGCCount;
    private long oldGCCount;
    private long totalYoungTime;
    private long totalOldTime;

    private Instant startTime;
    private Instant endTime;

    public void addEventInfo(GarbageCollectionNotificationInfo gcInfo) {
        String type = gcInfo.getGcAction();
        if (type.contains("minor")) {
            youngGCCount++;
            totalYoungTime += gcInfo.getGcInfo().getDuration();
        } else if (type.contains("major")) {
            oldGCCount++;
            totalOldTime += gcInfo.getGcInfo().getDuration();
        } else {
            throw new RuntimeException("GC type is unknown");
        }
    }

    private String makeReport() {
        double totalSeconds = Duration.between(startTime, endTime).toMillis() / 1000.0;
        double totalMinutes = totalSeconds / 60;

        StringBuilder report = new StringBuilder();
        report.append("GC ALGORITHM: " + GCName +"\n");

        report.append(String.format("Time spent from start to OOM error: %.3fs\n", totalSeconds));
        report.append("YOUNG GEN\n");
        report.append(String.format("\tCollections #: %d\n", youngGCCount));
        report.append(String.format("\tTime spent: %.3fs\n", totalYoungTime / 1000.0));
        report.append(String.format("\tAverage GC time: %.3f seconds per minute\n", totalYoungTime / (totalMinutes * 1000.0)));
        report.append("=====================\n");

        report.append("OLD GEN\n");
        report.append(String.format("\tCollections #: %d\n", oldGCCount));
        report.append(String.format("\tTime spent: %.3fs\n", totalOldTime / 1000.0));
        report.append(String.format("\tAverage GC time: %.3f seconds per minute\n", totalOldTime / (totalMinutes * 1000.0)));
        report.append("=====================");

        return report.toString();
    }

    public void setGCName(String GCName) {
        this.GCName = GCName;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public void writeToFile() {
        Path file = Paths.get(GCName.replace(' ', '_') + endTime.toEpochMilli() + ".txt");
        try (PrintWriter printWriter = new PrintWriter(file.toFile())) {
            printWriter.write(makeReport());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void print() {
        System.out.println(makeReport());
    }
}
