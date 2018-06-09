package ru.patsiorin.otus.adapters;

import javax.json.*;

public class CharacterAdapterFactory implements TypeAdapterFactory {
    @Override
    public TypeAdapter create(Class<?> type) {
        if (!Character.class.isAssignableFrom(type)) {
            return null;
        }
        return value -> {
            JsonArrayBuilder builder = Json.createArrayBuilder();
            builder.add(String.valueOf(value));
            return builder.build().get(0);
        };
    }
}
