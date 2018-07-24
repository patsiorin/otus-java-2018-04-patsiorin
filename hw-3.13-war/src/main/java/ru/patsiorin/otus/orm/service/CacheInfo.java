package ru.patsiorin.otus.orm.service;

import java.util.Map;

public interface CacheInfo {
    Map<String,Object> getCacheParamsAndState();
}
