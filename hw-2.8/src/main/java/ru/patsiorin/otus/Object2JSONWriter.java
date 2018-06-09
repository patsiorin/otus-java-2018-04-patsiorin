package ru.patsiorin.otus;

import ru.patsiorin.otus.adapters.TypeAdapter;
import ru.patsiorin.otus.adapters.TypeAdapters;

import javax.json.*;

/**
 * This class is a simple object to json serializer.
 * Map and List interfaces are supported.
 */
public class Object2JSONWriter {
    /**
     *
     * @param obj Object to serialize
     * @return Json String
     */
    public String toJson(Object obj) {
        if (obj == null) return null;
        TypeAdapter adapter = TypeAdapters.getAdapter(obj.getClass());
        JsonValue result = adapter.getJsonValue(obj);
        return result.toString();
    }
}
