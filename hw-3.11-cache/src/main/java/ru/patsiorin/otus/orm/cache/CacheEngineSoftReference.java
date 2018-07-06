package ru.patsiorin.otus.orm.cache;

import org.apache.log4j.Logger;

import java.lang.ref.SoftReference;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Predicate;

public class CacheEngineSoftReference<K, V> implements CacheEngine<K, V> {
    private final Map<K, SoftReference<CacheElement<V>>> cache = new LinkedHashMap<>();
    private final int maxElements;
    private final long lifeTimeMs;
    private final long idleTimeMs;
    private final boolean isEternal;
    private int hits;
    private int misses;
    private final Timer timer = new Timer();

    private final static Logger logger = Logger.getLogger(CacheEngineSoftReference.class);

    public CacheEngineSoftReference(int maxElements, long lifeTimeMs, long idleTimeMs, boolean isEternal) {
        this.maxElements = maxElements;
        this.lifeTimeMs = lifeTimeMs;
        this.idleTimeMs = idleTimeMs;
        this.isEternal = isEternal;

        scheduleTasks(lifeTimeMs, idleTimeMs, isEternal);

    }

    private void scheduleTasks(long lifeTimeMs, long idleTimeMs, boolean isEternal) {
        if (!isEternal && lifeTimeMs > 0) {
            TimerTask lifeTimeTask = createTimerTask(cached -> cached == null ||
                    cached.getCreated().plus(lifeTimeMs, ChronoUnit.MILLIS).isBefore(LocalDateTime.now()));
            timer.schedule(lifeTimeTask, lifeTimeMs, lifeTimeMs);
        }

        if (!isEternal && idleTimeMs > 0) {
            TimerTask idleTimeTask = createTimerTask(cached -> cached == null ||
                    cached.getLastAccessed().plus(idleTimeMs, ChronoUnit.MILLIS).isBefore(LocalDateTime.now()));
            timer.schedule(idleTimeTask, idleTimeMs, idleTimeMs);
        }
    }

    private TimerTask createTimerTask(Predicate<CacheElement<V>> predicate) {
        return new TimerTask() {
            @Override
            public void run() {
                cache.entrySet().removeIf(entry -> {
                    CacheElement<V> cached = entry.getValue().get();
                    if (predicate.test(cached)) {
                        logger.info("Remove element " + cached + " from cache by TimerTask");
                        return true;
                    } else {
                        return false;
                    }
                });
            }
        };
    }

    @Override
    public void put(K key, V value) {
        if (cache.size() == maxElements) {
            K elementToRemove = cache.keySet().iterator().next();
            logger.info("Max elements hit. Remove " + elementToRemove);
            cache.remove(elementToRemove);
        }

        logger.info("Put element " + value + " to cache");
        SoftReference<CacheElement<V>> ref = new SoftReference<>(new CacheElement<>(value));
        cache.put(key, ref);
    }

    @Override
    public V get(K key) {
        SoftReference<CacheElement<V>> ref = cache.get(key);
        if (ref == null) return null;
        CacheElement<V> cacheElement = ref.get();
        if (cacheElement != null) {
            cacheElement.setLastAccessed();
            logger.info("Get cached element " + cacheElement + " from cache");
            hits++;
            return cacheElement.getValue();
        }

        misses++;
        return null;


    }

    @Override
    public int getHitCount() {
        return hits;
    }

    @Override
    public int getMissCount() {
        return misses;
    }

    @Override
    public void dispose() {
        timer.cancel();
    }
}
