package com.example.shoppingcart.model;

import com.example.shoppingcart.service.CartService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CartItemTest {

    @Test
    void shouldCalculateTotalCorrectly() {
        CartItem item = new CartItem("Apple", 2.5, 4);

        double total = item.getTotal();

        assertEquals(10.0, total, 0.0001);
    }
    @Test
    void shouldCreateCartServiceWithDefaultConstructor() {
        CartService service = new CartService();

        assertNotNull(service);
    }

    @Test
    void shouldReturnCorrectValues() {
        CartItem item = new CartItem("Milk", 3.0, 2);

        assertEquals("Milk", item.getName());
        assertEquals(3.0, item.getPrice());
        assertEquals(2, item.getQuantity());
    }

    @Test
    void shouldReturnZeroTotalWhenQuantityIsZero() {
        CartItem item = new CartItem("Bread", 1.5, 0);

        assertEquals(0.0, item.getTotal(), 0.0001);
    }

    @Test
    void shouldHandleZeroPrice() {
        CartItem item = new CartItem("Free Item", 0.0, 5);

        assertEquals(0.0, item.getTotal(), 0.0001);
    }
}