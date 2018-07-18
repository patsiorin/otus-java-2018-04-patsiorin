package ru.patsiorin.otus;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.patsiorin.otus.orm.cache.CacheEngine;
import ru.patsiorin.otus.orm.cache.CacheEngineSoftReference;

public class CacheEngineTest {
    private CacheEngine<Integer, Object> cacheEngine;

    @Before
    public void setUp() {
        this.cacheEngine = new CacheEngineSoftReference<>(10, 100, 100, false);
    }

    @Test
    public void testCacheEngine() throws InterruptedException {
        for (int i = 0; i < 1000; i++) {
            cacheEngine.put(i, new byte[1024 * 1024]);
            cacheEngine.get(i);
        }
        System.out.println("Hit count: " + cacheEngine.getHitCount());
        System.out.println("Miss count: " + cacheEngine.getMissCount());
    }

    @After
    public void after() {
        cacheEngine.dispose();
    }
}
