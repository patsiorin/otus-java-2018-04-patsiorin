package ru.patsiorin.otus.orm.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionSingleton implements AutoCloseable {
    private final Connection connection;
    private static final ConnectionSingleton connectionSignleton = new ConnectionSingleton();
    private ConnectionSingleton() {
        try {
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
            String url = "jdbc:mysql://localhost:3306/orm?user=test&password=test&serverTimezone=UTC&useSSL=false";
            connection = DriverManager.getConnection(url);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection getConnection() {
        return connectionSignleton.connection;
    }

    @Override
    public void close() throws Exception {
        connection.close();
    }
}
