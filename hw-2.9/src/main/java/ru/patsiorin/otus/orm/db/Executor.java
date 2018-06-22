package ru.patsiorin.otus.orm.db;

import ru.patsiorin.otus.orm.handlers.ResultHandler;
import ru.patsiorin.otus.orm.model.DataSet;

import java.sql.*;

/**
 * Executor pattern class. Makes use of JDBC.
 */
public class Executor {
    private final Connection connection;

    public Executor(Connection connection) {
        this.connection = connection;
    }

    public void execUpdate(QueryWithData query) {
        try (PreparedStatement stmt = connection.prepareStatement(query.getQuery())) {
            setPreparedStatementValues(stmt, query.getValues());
            stmt.execute();
        } catch (SQLException e) {
            System.out.println("Query: " + query);
            e.printStackTrace();
        }
    }

    public <T extends DataSet> T execQuery(QueryWithData query, ResultHandler handler) {
        try (PreparedStatement stmt = connection.prepareStatement(query.getQuery())) {
            Object[] values = query.getValues();
            setPreparedStatementValues(stmt, values);
            //  ResultSet is AutoCloseable, so I put it inside try-with-resources block
            try (ResultSet resultSet = stmt.executeQuery()) {
                return (T) handler.handle(resultSet);
            }
        } catch (SQLException e) {
            System.out.println("Query: " + query);
            e.printStackTrace();
        }
        return null;
    }

    private void setPreparedStatementValues(PreparedStatement stmt, Object[] values) throws SQLException {
        for (int i = 1; i <= values.length; i++) {
            if (values[i - 1] instanceof Number) {
                stmt.setLong(i, Long.valueOf(values[i-1].toString()));
            } else if (values[i - 1] instanceof String) {
                stmt.setString(i, values[i - 1]. toString());
            } else {
                throw new UnsupportedOperationException("Type " + values[i - 1].getClass().getTypeName() + " is not supported");
            }
        }
    }
}
