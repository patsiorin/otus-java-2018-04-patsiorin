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

    public void execUpdate(String query) {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(query);
        } catch (SQLException e) {
            System.out.println("Query: " + query);
            e.printStackTrace();
        }
    }

    public <T extends DataSet> T execQuery(String query, ResultHandler handler) {
        try (Statement stmt = connection.createStatement(); ResultSet resultSet = stmt.executeQuery(query)) {
            return (T) handler.handle(resultSet);
        } catch (SQLException e) {
            System.out.println("Query: " + query);
            e.printStackTrace();
        }
        return null;
    }
}
