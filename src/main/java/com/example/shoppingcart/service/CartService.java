package com.example.shoppingcart.service;

import com.example.shoppingcart.model.CartItem;
import com.example.shoppingcart.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class CartService {

    public void saveCartRecord(int totalItems, double totalCost, String language, List<CartItem> items) {
        String insertCartSql =
                "INSERT INTO cart_records (total_items, total_cost, language) VALUES (?, ?, ?)";

        String insertItemSql =
                "INSERT INTO cart_items (cart_record_id, item_number, price, quantity, subtotal) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection()) {
            connection.setAutoCommit(false);

            int cartRecordId;

            try (PreparedStatement cartStatement = connection.prepareStatement(
                    insertCartSql, PreparedStatement.RETURN_GENERATED_KEYS)) {

                cartStatement.setInt(1, totalItems);
                cartStatement.setDouble(2, totalCost);
                cartStatement.setString(3, language);
                cartStatement.executeUpdate();

                try (ResultSet keys = cartStatement.getGeneratedKeys()) {
                    if (keys.next()) {
                        cartRecordId = keys.getInt(1);
                    } else {
                        throw new SQLException("No cart record ID generated.");
                    }
                }
            }

            try (PreparedStatement itemStatement = connection.prepareStatement(insertItemSql)) {
                int itemNumber = 1;

                for (CartItem item : items) {
                    itemStatement.setInt(1, cartRecordId);
                    itemStatement.setInt(2, itemNumber++);
                    itemStatement.setDouble(3, item.getPrice());
                    itemStatement.setInt(4, item.getQuantity());
                    itemStatement.setDouble(5, item.getTotal());
                    itemStatement.addBatch();
                }

                itemStatement.executeBatch();
            }

            connection.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}