package ru.patsiorin.otus;

import com.sun.management.GarbageCollectionNotificationInfo;

import javax.management.InstanceNotFoundException;
import javax.management.MBeanServer;
import javax.management.openmbean.CompositeData;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.time.Instant;

/**
 * <p>
 * This class registers listeners for Garbage Collection events and
 * then tries to flood the available memory by adding objects to a list indefinitely.
 * </p>
 *
 * <p>
 * When OutOfMemory Error occurs (in approximately 5 minutes on my machine) the app writes the report to stdout
 * and to a file in current working directory.
 * </p>
 */
public class GCWatcher {
    private static GCReport report = new GCReport();

    public static void main(String[] args) throws Exception {

        report.setStartTime(Instant.now());

        StringBuilder GCName = new StringBuilder();
        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        for (GarbageCollectorMXBean gc : ManagementFactory.getGarbageCollectorMXBeans()) {
            addGCListener(mBeanServer, gc);
            GCName.append(gc.getName().trim()).append("+");
        }
        report.setGCName(GCName.substring(0, GCName.length() - 1));

        startMemoryLeakAndWaitForOOM();
        report.print();
        report.writeToFile();
    }

    private static void addGCListener(MBeanServer mBeanServer, GarbageCollectorMXBean gc) throws InstanceNotFoundException {
        mBeanServer.addNotificationListener(gc.getObjectName(), (notification, handback) -> {
            if (notification.getType().equals(GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION)) {
                GarbageCollectionNotificationInfo gcInfo =
                        GarbageCollectionNotificationInfo.from((CompositeData) notification.getUserData());
                report.addEventInfo(gcInfo);
            }
        }, null, null);
    }

    private static void startMemoryLeakAndWaitForOOM() {
        LeakingList leakingList = new LeakingList();
        Thread t = new Thread(leakingList);
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        report.setEndTime(Instant.now());
        report.setLoopCount(leakingList.loopCount);
    }
}
