package ru.patsiorin.otus.orm.service;

import ru.patsiorin.otus.orm.db.ConnectionSingleton;
import ru.patsiorin.otus.orm.db.Executor;
import ru.patsiorin.otus.orm.db.QueryWithData;
import ru.patsiorin.otus.orm.db.ReflectiveQueryBuilder;
import ru.patsiorin.otus.orm.handlers.ReflectiveResultHandler;
import ru.patsiorin.otus.orm.model.DataSet;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Implementation of the service by using java reflection.
 * Supports only primitive integer and String types.
 */
public class DBServiceReflectiveImpl implements DBService {
    @Override
    public <T extends DataSet> void save(T dataSet) {
        try (Connection connection = ConnectionSingleton.getConnection()) {
            Executor executor = new Executor(connection);
            ReflectiveQueryBuilder<T> queryBuilder = new ReflectiveQueryBuilder<>(dataSet);
            QueryWithData query = queryBuilder.buildInsertOrUpdateQuery();
            executor.execUpdate(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public <T extends DataSet> T load(long id, Class<T> dataSetClass) {
        try (Connection connection = ConnectionSingleton.getConnection()) {
            Executor executor = new Executor(connection);
            ReflectiveQueryBuilder<T> queryBuilder = new ReflectiveQueryBuilder<>(dataSetClass);
            QueryWithData query = queryBuilder.formSelectQuery(id);
            return executor.execQuery(query, new ReflectiveResultHandler<>(dataSetClass));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
