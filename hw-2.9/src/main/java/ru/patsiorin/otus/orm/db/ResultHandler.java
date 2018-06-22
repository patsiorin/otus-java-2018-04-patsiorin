package ru.patsiorin.otus.orm.db;

import ru.patsiorin.otus.orm.model.DataSet;

import java.sql.ResultSet;

public interface ResultHandler<T extends DataSet> {
    T handle(ResultSet resultSet);
}
