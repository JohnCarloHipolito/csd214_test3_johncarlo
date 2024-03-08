package com.triosstudent.csd214_test3_johncarlo;

import java.sql.*;

public class DBConnection {

    private static DBConnection instance;
    private final Connection connection;

    private DBConnection() throws SQLException {
        connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/csd214_test3_johncarlo", "root", "pwd123!");
    }

    public static DBConnection getInstance() throws SQLException {
        if (instance == null) {
            instance = new DBConnection();
        } else if (instance.getConnection().isClosed()) {
            instance = new DBConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}