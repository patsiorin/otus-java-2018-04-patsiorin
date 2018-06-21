package ru.patsiorin.otus.orm;

import java.sql.ResultSet;

public interface ResultHandler<T extends DataSet> {
    T handle(ResultSet resultSet);
}
