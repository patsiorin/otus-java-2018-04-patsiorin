package ru.patsiorin.otus.adapters;

import javax.json.*;
import java.math.BigDecimal;

public class NumberAdapterFactory implements TypeAdapterFactory {
    @Override
    public TypeAdapter create(Class<?> type) {
        if (!Number.class.isAssignableFrom(type)) {
            return null;
        }
        return value -> {
            JsonArrayBuilder builder = Json.createArrayBuilder();
            builder.add( new BigDecimal(value.toString()));
            return builder.build().get(0);
        };
    }
}
