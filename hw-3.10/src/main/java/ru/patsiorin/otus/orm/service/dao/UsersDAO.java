package ru.patsiorin.otus.orm.service.dao;

import ru.patsiorin.otus.orm.model.DataSet;

/**
 * Data Access Object
 * Is used to be the layer between app's logic and database connection
 */
public interface UsersDAO {
        <T extends DataSet> void save(T dataSet);
        <T extends DataSet> T load(long id, Class<T> cl);
}
