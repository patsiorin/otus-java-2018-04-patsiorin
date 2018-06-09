package ru.patsiorin.otus.adapters;

import javax.json.JsonValue;

/**
 * TypeAdapter interface
 */
public interface TypeAdapter {
    /**
     *
     * @param value object to convert to JsonValue
     * @return JsonValue
     */
    JsonValue getJsonValue(Object value);
}
