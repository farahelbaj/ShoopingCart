package com.example.shoppingcart.service;

import com.example.shoppingcart.model.CartItem;
import com.example.shoppingcart.util.ConnectionProvider;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import static org.mockito.Mockito.*;

public class CartServiceTest {

    @Test
    void shouldSaveCartRecordAndItemsSuccessfully() throws Exception {
        ConnectionProvider connectionProvider = mock(ConnectionProvider.class);
        Connection connection = mock(Connection.class);
        PreparedStatement cartStatement = mock(PreparedStatement.class);
        PreparedStatement itemStatement = mock(PreparedStatement.class);
        ResultSet generatedKeys = mock(ResultSet.class);

        CartItem item1 = new CartItem("Apple", 10.0, 2);
        CartItem item2 = new CartItem("Milk", 5.5, 3);

        when(connectionProvider.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(
                "INSERT INTO cart_records (total_items, total_cost, language) VALUES (?, ?, ?)",
                PreparedStatement.RETURN_GENERATED_KEYS
        )).thenReturn(cartStatement);
        when(connection.prepareStatement(
                "INSERT INTO cart_items (cart_record_id, item_number, price, quantity, subtotal) VALUES (?, ?, ?, ?, ?)"
        )).thenReturn(itemStatement);
        when(cartStatement.getGeneratedKeys()).thenReturn(generatedKeys);
        when(generatedKeys.next()).thenReturn(true);
        when(generatedKeys.getInt(1)).thenReturn(101);

        CartService service = new CartService(connectionProvider);
        service.saveCartRecord(5, 36.5, "en", List.of(item1, item2));

        verify(connection).setAutoCommit(false);
        verify(cartStatement).executeUpdate();
        verify(itemStatement, times(2)).addBatch();
        verify(itemStatement).executeBatch();
        verify(connection).commit();
    }
}