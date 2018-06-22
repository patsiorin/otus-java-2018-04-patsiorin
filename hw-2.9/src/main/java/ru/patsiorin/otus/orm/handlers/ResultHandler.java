package ru.patsiorin.otus.orm.handlers;

import ru.patsiorin.otus.orm.model.DataSet;

import java.sql.ResultSet;

public interface ResultHandler<T extends DataSet> {
    T handle(ResultSet resultSet);
}
