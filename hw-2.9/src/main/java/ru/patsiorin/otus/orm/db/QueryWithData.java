package ru.patsiorin.otus.orm.db;

import java.util.Arrays;

public class QueryWithData {
    private final String query;
    private final Object[] values;

    public QueryWithData(String query, Object[] values) {
        this.query = query;
        this.values = values;
    }

    public String getQuery() {
        return query;
    }

    public Object[] getValues() {
        return values;
    }

    @Override
    public String toString() {
        return "QueryWithData{" +
                "query='" + query + '\'' +
                ", values=" + Arrays.toString(values) +
                '}';
    }
}
