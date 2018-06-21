package ru.patsiorin.otus.orm;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReflectiveResultHandler<T extends DataSet> implements ResultHandler {
    private  Class<T> dataSetClass;
    ReflectiveResultHandler(Class<T> aClass) {
        dataSetClass = aClass;
    }

    @Override
    public T handle(ResultSet resultSet) {
            try {
                resultSet.next();
                T result = dataSetClass.getConstructor(long.class).newInstance(resultSet.getInt("id"));
                for (Field field : dataSetClass.getDeclaredFields()) {
                    setField(resultSet, result, field);
                }
                return result;
            } catch (SQLException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
            }

        return null;

    }

    private void setField(ResultSet resultSet, T result, Field field) throws IllegalAccessException, SQLException {
        field.setAccessible(true);
        if (field.getType().equals(String.class)) {
            field.set(result, resultSet.getString(field.getName()));
        } else if (field.getType().equals(int.class)) {
            field.setInt(result, resultSet.getInt(field.getName()));
        } else {
            throw new UnsupportedOperationException("Field type '" + field.getType() + "' is not supported");
        }
        field.setAccessible(false);
    }
}
