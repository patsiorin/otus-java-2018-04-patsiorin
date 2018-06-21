package ru.patsiorin.otus.orm;

/**
 * Superclass of all DataSets containing id.
 */
public abstract class DataSet {
    private final long id;

    protected DataSet(long id) {
        this.id = id;
    }

    protected long getId() {
        return id;
    }
}
