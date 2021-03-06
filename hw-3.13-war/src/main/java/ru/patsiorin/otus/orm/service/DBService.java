package ru.patsiorin.otus.orm.service;

import ru.patsiorin.otus.orm.model.DataSet;

import java.util.Map;

/**
 * Database service interface
 */
public interface DBService<T extends DataSet> {
    /**
     * Saves subclasses of DataSet to the database if id field is equals to zero
     * or updates rows if id is set.
     *
     * @param object inherited from DataSet
     * @param <T>    generic type of a DataSet subclass
     */
    void save(T object);

    /**
     * Queries the database for row of provided id and then returns corresponding object of type T.
     *
     * @param id  id of the row in the database
     * @param cl  class of DataSet
     * @param <T> generic type of a DataSet subclass
     * @return
     */
    T load(long id, Class<T> cl);

    void shutdown();

}
