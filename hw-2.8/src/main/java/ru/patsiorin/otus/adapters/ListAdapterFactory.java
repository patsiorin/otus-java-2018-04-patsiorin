package ru.patsiorin.otus.adapters;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonValue;
import java.util.List;

public class ListAdapterFactory implements TypeAdapterFactory {
    @Override
    public TypeAdapter create(Class<?> type) {
        if (List.class.isAssignableFrom(type)) {
            return value -> {
                JsonArrayBuilder builder = Json.createArrayBuilder();
                for (Object obj : (List) value) {
                    if (obj == null) {
                        builder.add(JsonValue.NULL);
                    } else {
                        builder.add(TypeAdapters.getAdapter(obj.getClass()).getJsonValue(obj));
                    }
                }
                return builder.build();
            };
        }
        return null;
    }
}
