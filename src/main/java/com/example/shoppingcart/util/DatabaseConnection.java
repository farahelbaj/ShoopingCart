package com.example.shoppingcart.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection implements ConnectionProvider {

    private static final String URL =
            "jdbc:mariadb://localhost:3306/shopping_cart_localization";
    private static final String USER = "shopping_user";
    private static final String PASSWORD = "1234";

    @Override
    public Connection getConnection() throws SQLException {
        try {
            loadDriver();
        } catch (ClassNotFoundException e) {
            throw new SQLException("MariaDB JDBC Driver not found.", e);
        }

        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    protected void loadDriver() throws ClassNotFoundException {
        Class.forName("org.mariadb.jdbc.Driver");
    }
}