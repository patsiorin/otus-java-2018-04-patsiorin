package ru.patsiorin.otus.adapters;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonValue;

public class BooleanAdapterFactory implements TypeAdapterFactory {
    @Override
    public TypeAdapter create(Class<?> type) {
        if (Boolean.class.isAssignableFrom(type)) {
            return value -> {
                JsonArrayBuilder builder = Json.createArrayBuilder();
                builder.add((Boolean) value);
                return builder.build().get(0);
            };
        }
        return null;
    }
}
