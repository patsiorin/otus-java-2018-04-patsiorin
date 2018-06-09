package ru.patsiorin.otus.adapters;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonValue;
import java.lang.reflect.Array;

public class ArrayTypeAdapterFactory implements TypeAdapterFactory {
    @Override
    public TypeAdapter create(Class<?> type) {
        if (type.isArray()) {
            return value -> {
                JsonArrayBuilder builder = Json.createArrayBuilder();

                int size = Array.getLength(value);
                for (int i = 0; i < size; i++) {
                    Object element = Array.get(value, i);
                    if (element == null) {
                        builder.add(JsonValue.NULL);
                    } else {
                        builder.add(TypeAdapters.getAdapter(element.getClass()).getJsonValue(element));
                    }
                }
                return builder.build();
            };
        }

        return null;
    }
}
