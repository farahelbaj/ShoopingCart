package com.example.shoppingcart.service;

import com.example.shoppingcart.util.ConnectionProvider;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LocalizationServiceTest {

    @Test
    void shouldReturnLocalizedStringsForGivenLanguage() throws Exception {
        ConnectionProvider connectionProvider = mock(ConnectionProvider.class);
        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);
        ResultSet resultSet = mock(ResultSet.class);

        when(connectionProvider.getConnection()).thenReturn(connection);
        when(connection.prepareStatement("SELECT `key`, value FROM localization_strings WHERE language = ?"))
                .thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true, true, false);
        when(resultSet.getString("key")).thenReturn("app.title", "button.calculate");
        when(resultSet.getString("value")).thenReturn("Shopping Cart", "Calculate");

        LocalizationService service = new LocalizationService(connectionProvider);

        Map<String, String> result = service.getLocalizedStrings("en");

        assertEquals(2, result.size());
        assertEquals("Shopping Cart", result.get("app.title"));
        assertEquals("Calculate", result.get("button.calculate"));

        verify(statement).setString(1, "en");
        verify(statement).executeQuery();
    }

    @Test
    void shouldReturnEmptyMapWhenSQLExceptionOccurs() throws Exception {
        ConnectionProvider connectionProvider = mock(ConnectionProvider.class);
        when(connectionProvider.getConnection()).thenThrow(new java.sql.SQLException("DB failed"));

        LocalizationService service = new LocalizationService(connectionProvider);

        Map<String, String> result = service.getLocalizedStrings("en");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}