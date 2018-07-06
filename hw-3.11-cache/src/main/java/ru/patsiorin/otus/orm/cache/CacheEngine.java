package ru.patsiorin.otus.orm.cache;

/**
 * Cache engine interface
 *
 * @param <K> key of the element to be cache (for example, id)
 * @param <V> value to be cached
 */
public interface CacheEngine<K, V> {
    /**
     * Puts a key-value in the cache
     * @param key
     * @param value
     */
    void put(K key, V value);

    /**
     * Retrieves cache element if present or returns null;
     * @param key
     * @return cache element or null
     */
    V get(K key);

    /**
     * Returns number of retrievals from cache
     * @return
     */
    int getHitCount();

    /**
     * Returns number of misses in cache
     * @return
     */
    int getMissCount();

    /**
     * Closes resources
     */
    void dispose();
}
