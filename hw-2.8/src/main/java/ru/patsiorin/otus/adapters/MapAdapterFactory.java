package ru.patsiorin.otus.adapters;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import java.util.Map;

public class MapAdapterFactory implements TypeAdapterFactory {
    @Override
    public TypeAdapter create(Class<?> type) {
        if (Map.class.isAssignableFrom(type)) {
            return value -> {
                JsonObjectBuilder builder = Json.createObjectBuilder();
                for (Map.Entry<?,?> entry : ((Map<?,?>) value).entrySet()) {
                    Object val = entry.getValue();
                    String key = entry.getKey().toString();
                    if (val != null) {
                        builder.add(
                                key,
                                TypeAdapters.getAdapter(val.getClass()).getJsonValue(val));
                    }
                }
                return builder.build();
            };
        }
        return null;
    }
}
