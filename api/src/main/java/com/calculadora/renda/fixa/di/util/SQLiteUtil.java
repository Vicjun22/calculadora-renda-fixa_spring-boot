package com.calculadora.renda.fixa.di.util;

import java.sql.*;

public class SQLiteUtil {

    private final String databaseUrl;

    public SQLiteUtil(String databaseFile) {
        this.databaseUrl = "jdbc:sqlite:" + databaseFile;
    }

    public Connection connect() throws SQLException {
        return DriverManager.getConnection(databaseUrl);
    }

    public void executeUpdate(String sql) throws SQLException {
        try (Connection connection = connect(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeUpdate();
        }
    }

    public ResultSet executeQuery(String sql) throws SQLException {
        Connection connection = connect();
        PreparedStatement statement = connection.prepareStatement(sql);
        return statement.executeQuery();
    }
}
