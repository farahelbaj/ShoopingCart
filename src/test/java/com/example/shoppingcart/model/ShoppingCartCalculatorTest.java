package com.example.shoppingcart.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ShoppingCartCalculatorTest {

    private final ShoppingCartCalculator calculator = new ShoppingCartCalculator();

    @Test
    void shouldCalculateItemTotalCorrectly() {
        double total = calculator.calculateItemTotal(10.0, 3);
        assertEquals(30.0, total, 0.0001);
    }

    @Test
    void shouldCalculateCartTotalCorrectly() {
        List<CartItem> items = List.of(
                new CartItem("Apple", 2.0, 4),   // 8
                new CartItem("Milk", 3.5, 2),    // 7
                new CartItem("Bread", 1.5, 3)    // 4.5
        );

        double total = calculator.calculateCartTotal(items);
        assertEquals(19.5, total, 0.0001);
    }

    @Test
    void shouldReturnZeroForEmptyCart() {
        double total = calculator.calculateCartTotal(List.of());
        assertEquals(0.0, total, 0.0001);
    }
}