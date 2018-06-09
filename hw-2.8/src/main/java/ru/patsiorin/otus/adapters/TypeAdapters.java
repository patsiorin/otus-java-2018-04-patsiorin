package ru.patsiorin.otus.adapters;

import java.util.ArrayList;
import java.util.List;

public class TypeAdapters {
    private static final List<TypeAdapterFactory> factories = new ArrayList<>();
    static {
        factories.add(new ClassAdapterFactory());
        factories.add(new BooleanAdapterFactory());
        factories.add(new CharacterAdapterFactory());
        factories.add(new NumberAdapterFactory());
        factories.add(new StringAdapterFactory());
        factories.add(new MapAdapterFactory());
        factories.add(new ListAdapterFactory());
        factories.add(new ArrayTypeAdapterFactory());
        // ReflectiveTypeAdapter should be the last one in the list
        factories.add(new ReflectiveTypeAdapterFactory());
    }

    public static TypeAdapter getAdapter(Class<?> type) {
        for (TypeAdapterFactory factory : factories) {
            TypeAdapter candidate = factory.create(type);
            if (candidate != null) {
                return candidate;
            }
        }
        throw new IllegalArgumentException("This implementation does not support type " + type.getTypeName());
    }
}
