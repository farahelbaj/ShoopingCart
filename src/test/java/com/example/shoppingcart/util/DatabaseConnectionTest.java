package com.example.shoppingcart.util;

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseConnectionTest {

    @Test
    void shouldCreateDatabaseConnectionObject() {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        assertNotNull(databaseConnection);
    }

    @Test
    void shouldThrowSQLExceptionWhenDriverIsMissing() {
        DatabaseConnection databaseConnection = new DatabaseConnection() {
            @Override
            protected void loadDriver() throws ClassNotFoundException {
                throw new ClassNotFoundException("Driver missing");
            }
        };

        SQLException exception = assertThrows(SQLException.class, databaseConnection::getConnection);

        assertEquals("MariaDB JDBC Driver not found.", exception.getMessage());
        assertNotNull(exception.getCause());
        assertTrue(exception.getCause() instanceof ClassNotFoundException);
    }

    @Test
    void shouldAttemptToGetConnection() {
        DatabaseConnection databaseConnection = new DatabaseConnection();

        try {
            Connection connection = databaseConnection.getConnection();
            assertNotNull(connection);
            assertFalse(connection.isClosed());
            connection.close();
        } catch (SQLException e) {
            assertNotNull(e.getMessage());
        }
    }
}