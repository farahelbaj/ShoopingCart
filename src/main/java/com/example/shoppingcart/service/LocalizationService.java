package com.example.shoppingcart.service;

import com.example.shoppingcart.util.ConnectionProvider;
import com.example.shoppingcart.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class LocalizationService {

    private final ConnectionProvider connectionProvider;

    public LocalizationService() {
        this.connectionProvider = new DatabaseConnection();
    }

    public LocalizationService(ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    public Map<String, String> getLocalizedStrings(String language) {
        Map<String, String> strings = new HashMap<>();

        String sql = "SELECT `key`, value FROM localization_strings WHERE language = ?";

        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, language);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    strings.put(
                            resultSet.getString("key"),
                            resultSet.getString("value")
                    );
                }
            }

        } catch (SQLException e) {
            System.err.println("Error loading localized strings: " + e.getMessage());
        }

        return strings;
    }
}