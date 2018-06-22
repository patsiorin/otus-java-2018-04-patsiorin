package ru.patsiorin.otus.orm.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionSingleton implements AutoCloseable {
    private Connection connection;
    private static ConnectionSingleton connectionSingleton;
    private final String dbUrl = "jdbc:mysql://localhost:3306/orm?user=test&password=test&serverTimezone=UTC&useSSL=false";

    static {
        try {
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private ConnectionSingleton() throws SQLException {
        connect();
    }

    public static Connection getConnection() throws SQLException {
        if (connectionSingleton == null) connectionSingleton = new ConnectionSingleton();
        if (connectionSingleton.connection == null || connectionSingleton.connection.isClosed()) {
            connectionSingleton.connect();
        }
        return connectionSingleton.connection;
    }

    private void connect() throws SQLException {
        connection = DriverManager.getConnection(dbUrl);
    }

    @Override
    public void close() throws Exception {
        connection.close();
    }
}
