package ru.patsiorin.otus.adapters;

import javax.json.Json;
import javax.json.JsonArrayBuilder;

public class StringAdapterFactory implements TypeAdapterFactory {
    @Override
    public TypeAdapter create(Class<?> type) {
        if (!String.class.isAssignableFrom(type)) {
            return null;
        }
        return value -> {
            JsonArrayBuilder builder = Json.createArrayBuilder();
            builder.add(value.toString());
            return builder.build().get(0);
        };
    }
}
