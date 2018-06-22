package ru.patsiorin.otus.orm.db;

import ru.patsiorin.otus.orm.model.DataSet;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class builds query strings by using java reflection.
 * Only supports int and String types.
 * @param <T>
 */
public class ReflectiveQueryBuilder<T extends DataSet> {
    private static final String CLASS_NAME_SUFFIX = "DataSet";
    private static final String JOINING_DELIMITER = ", ";
    private static final String INSERT_QUERY_TEMPLATE = "INSERT INTO `%s` (%s) VALUES (%s)";
    private static final String UPDATE_QUERY_TEMPLATE = "UPDATE `%s` SET %s WHERE id = ?";
    private static final String SELECT_QUERY_TEMPLATE = "SELECT * FROM `%s` WHERE id = ?";

    private final T dataSet;
    private final String tableName;
    private final List<ColumnValuePair> columnsAndValuesPairs;


    public ReflectiveQueryBuilder(T dataSet) {
        this.dataSet = dataSet;
        tableName = extractTableName(dataSet.getClass());
        columnsAndValuesPairs = getColumnValuesPairs(dataSet);
    }

    public ReflectiveQueryBuilder(Class<T> dataSetType) {
        this.dataSet = null;
        tableName = extractTableName(dataSetType);
        columnsAndValuesPairs = null;
    }

    public QueryWithData buildInsertOrUpdateQuery() {
        String queryString;
        List<Object> values = columnsAndValuesPairs.stream()
                .map(ColumnValuePair::getValue).collect(Collectors.toList());
        if (dataSet.getId() == 0) {
            queryString = buildInsertQuery();
        } else {
            queryString = buildUpdateQuery();
            values.add(dataSet.getId());
        }
        return new QueryWithData(queryString, values.toArray());
    }

    private String buildUpdateQuery() {
        String columnsEqualsValues = columnsAndValuesPairs.stream()
                .map(ColumnValuePair::getPairString).collect(Collectors.joining(JOINING_DELIMITER));

        return String.format(UPDATE_QUERY_TEMPLATE, tableName, columnsEqualsValues);
    }

    private String buildInsertQuery() {
        String columns = columnsAndValuesPairs.stream()
                .map(ColumnValuePair::getColumnString).collect(Collectors.joining(JOINING_DELIMITER));
        String values = columnsAndValuesPairs.stream()
                .map(v -> "?").collect(Collectors.joining(JOINING_DELIMITER));
        return String.format(INSERT_QUERY_TEMPLATE, tableName, columns, values);
    }

    private List<ColumnValuePair> getColumnValuesPairs(T dataSet) {
        List<ColumnValuePair> result = new ArrayList<>();
        for (Field field : dataSet.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                result.add(new ColumnValuePair(field.getName(), field.get(dataSet)));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            field.setAccessible(false);
        }
        return result;
    }

    private String extractTableName(Class<?> dataSetType) {
        String className = dataSetType.getSimpleName();
        if (className.length() <= CLASS_NAME_SUFFIX.length() || !className.contains(CLASS_NAME_SUFFIX)) {
            throw new IllegalArgumentException("Name of the class must be TableNameDataSet");
        }
        return className.substring(0, className.length() - CLASS_NAME_SUFFIX.length()).toLowerCase();
    }

    public QueryWithData formSelectQuery(long id) {
        return new QueryWithData(String.format(SELECT_QUERY_TEMPLATE, tableName), new Object[]{id});
    }


    static class ColumnValuePair {
        private String column;
        private Object value;

        ColumnValuePair(String column, Object value) {
            this.column = column;
            this.value = value;
        }

        Object getValue() {
            return value;
        }

        String getColumnString() {
            return column;
        }

        String getPairString() {
            return getColumnString() + " = ?" ;
        }
    }
}
