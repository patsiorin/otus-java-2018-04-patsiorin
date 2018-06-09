package ru.patsiorin.otus.adapters;

public interface TypeAdapterFactory {
    /**
     * Creates a TypeAdapter if the type is suitable
     * or returns null;
     * @param type Class object
     * @return Suitable TypeAdapter or null
     */
    TypeAdapter create(Class<?> type);
}
