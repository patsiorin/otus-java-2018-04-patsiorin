package ru.patsiorin.otus.orm.cache;

import java.time.LocalDateTime;

class CacheElement<V> {
    private final V value;
    private final LocalDateTime created = LocalDateTime.now();
    private LocalDateTime lastAccessed = LocalDateTime.now();

    CacheElement(V value) {
        this.value = value;
    }

    V getValue() {
        return value;
    }

    void setLastAccessed() {
        this.lastAccessed = LocalDateTime.now();
    }

    LocalDateTime getCreated() {
        return created;
    }

    LocalDateTime getLastAccessed() {
        return lastAccessed;
    }

    @Override
    public String toString() {
        return "CacheElement{" +
                "value=" + value +
                ", created=" + created +
                ", lastAccessed=" + lastAccessed +
                '}';
    }
}
