package ru.patsiorin.otus.orm.model;

/**
 * Superclass of all DataSets containing id.
 */
public abstract class DataSet {
    private final long id;

    public DataSet(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
