package ru.patsiorin.otus.adapters;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.LinkedHashMap;
import java.util.Map;

public class ReflectiveTypeAdapterFactory implements TypeAdapterFactory {
    @Override
    public TypeAdapter create(Class<?> obj) {
        if (!Object.class.isAssignableFrom(obj)) {
            return null; // which means it's a primitive type
        }

        return new Adapter(getBoundFields(obj));
    }

    private Map<String, BoundField> getBoundFields(Class<?> obj) {
        Map<String, BoundField> boundFields = new LinkedHashMap<>();
        Field[] fields = obj.getDeclaredFields();
        for (Field f : fields) {
            f.setAccessible(true);
            if (isSerializable(f)) {
                boundFields.put(f.getName(), createBoundField(f));
            }
        }
        return boundFields;

    }

    private boolean isSerializable(Field f) {
        return (f.getModifiers() & Modifier.TRANSIENT) == 0
                && (f.getModifiers() & Modifier.STATIC) == 0;
    }

    private BoundField createBoundField(Field f) {
        return new BoundField(f) {
            @Override
            void write(JsonObjectBuilder builder, Object obj) {
                try {
                    Object o = f.get(obj);
                    if (o == null) return;
                    TypeAdapter adapter = TypeAdapters.getAdapter(o.getClass());
                    JsonValue value = adapter.getJsonValue(o);
                    builder.add(f.getName(), value);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    static final class Adapter implements TypeAdapter {
        private Map<String, BoundField> boundFields;
        private JsonObjectBuilder builder = Json.createObjectBuilder();

        public Adapter(Map<String, BoundField> boundFields) {
            this.boundFields = boundFields;
        }

        @Override
        public JsonValue getJsonValue(Object obj) {
            for (BoundField boundField : boundFields.values()) {
                boundField.write(builder, obj);
            }
            return builder.build();
        }
    }

    static abstract class BoundField {
        private Field field;

        public BoundField(Field field) {
            this.field = field;
        }

        abstract void write(JsonObjectBuilder builder, Object obj);
    }
}
