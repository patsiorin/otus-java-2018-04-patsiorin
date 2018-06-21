package ru.patsiorin.otus.orm;

/**
 * Implementation of the service by using java reflection.
 * Supports only primitive integer and String types.
 */
public class DBServiceReflectiveImpl implements DBService {
    @Override
    public <T extends DataSet> void save(T dataSet) {
        ReflectiveQueryBuilder<T> queryBuilder = new ReflectiveQueryBuilder<>(dataSet);
        String query = queryBuilder.buildInsertOrUpdateQuery();
        Executor executor = new Executor(ConnectionHelper.getConnection());
        executor.execUpdate(query);
    }

    @Override
    public <T extends DataSet> T load(long id, Class<T> dataSetClass) {
        ReflectiveQueryBuilder<T> queryBuilder = new ReflectiveQueryBuilder<>(dataSetClass);
        String query = queryBuilder.formSelectQuery(id);
        Executor executor = new Executor(ConnectionHelper.getConnection());
        return executor.execQuery(query, new ReflectiveResultHandler<>(dataSetClass));
    }
}
