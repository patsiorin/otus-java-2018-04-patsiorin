package ru.patsiorin.otus.orm.service;

import ru.patsiorin.otus.orm.cache.CacheEngine;
import ru.patsiorin.otus.orm.model.DataSet;

import java.util.Map;

@SuppressWarnings("unchecked")
public class DBServiceCachedImpl<T extends DataSet> implements DBService<T>, CacheInfo {
    private final CacheEngine<Long, T> cache;
    private final DBService service;

    public DBServiceCachedImpl(DBService service, CacheEngine<Long, T> cache) {
        this.cache = cache;
        this.service = service;
    }

    public void save(T object) {
        service.save(object);
        if (object.getId() >= 0) {
            cache.put(object.getId(), object);
        }
    }

    public  T load(long id, Class<T> cl) {
        T cached = cache.get(id);
        if (cached != null) {
            return cached;
        }
        return (T) service.load(id, cl);
    }

    public void shutdown() {
        cache.dispose();
        service.shutdown();
    }

    public Map<String, Object> getCacheParamsAndState() {
        return cache.getParamsAndState();
    }
}
