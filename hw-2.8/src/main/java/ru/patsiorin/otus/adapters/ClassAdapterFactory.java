package ru.patsiorin.otus.adapters;

public class ClassAdapterFactory implements TypeAdapterFactory {
    @Override
    public TypeAdapter create(Class<?> type) {
        if (Class.class.isAssignableFrom(type)) {
            throw new UnsupportedOperationException("Class type is not supported");
        }
        return null;
    }
}
