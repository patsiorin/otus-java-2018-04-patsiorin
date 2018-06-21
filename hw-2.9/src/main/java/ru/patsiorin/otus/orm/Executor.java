package ru.patsiorin.otus.orm;

import java.sql.*;

/**
 * Executor pattern class. Makes use of JDBC.
 */
public class Executor {
    private final Connection connection;

    Executor(Connection connection) {
        this.connection = connection;
    }

    void execUpdate(String query) {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(query);
        } catch (SQLException e) {
            System.out.println("Query: " + query);
            e.printStackTrace();
        }
    }

    <T extends DataSet> T execQuery(String query, ResultHandler handler) {
        try (Statement stmt = connection.createStatement()) {
            ResultSet resultSet = stmt.executeQuery(query);
            return (T) handler.handle(resultSet);
        } catch (SQLException e) {
            System.out.println("Query: " + query);
            e.printStackTrace();
        }
        return null;
    }
}
